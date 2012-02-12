/* BEGIN cowbel runtime library
 *
 * Written in 2012 by David Given.
 *
 * To the extent possible under law, the author of the cowbel runtime
 * library (of which this code, up to the string 'END cowbel runtime library',
 * is part), has dedicated all copyright and related and neighboring rights
 * to this software to the public domain worldwide. This software is
 * distributed without any warranty.
 *
 * Please see the file COPYING.CC0 in the distribution package for more
 * information.
 */

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <stdarg.h>
#include <string.h>
#include <locale.h>
#include <gc.h>

typedef int s_boolean_t;
typedef int s_int_t;
typedef double s_real_t;
typedef struct s_string s_string_t;

#define S_ALLOC_CONSTRUCTOR(type) \
	((sizeof(type) > 0) ? ((type*) GC_MALLOC(sizeof(type))) : NULL)

static int s_argc;
static s_string_t** s_argv;

/* -------------------------------------------------------------------- */
/*                           STRING MANIPULATION                        */
/* -------------------------------------------------------------------- */

static const uint32_t utf8_read_offsets[4] =
{
    0x00000000UL, 0x00003080UL, 0x000E2080UL, 0x03C82080UL
};

static const char utf8_trailing_bytes[256] =
{
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 0
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 1
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 2
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 3
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 4
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 5
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 6
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  // 7
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 8
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 9
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // A
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // B
     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  // C
     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,  // D
     2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,  // E
     3, 3, 3, 3, 3, 3, 3, 3,-1,-1,-1,-1,-1,-1,-1,-1,  // F
};

static int utf8_read(const unsigned char** srcp)
{
    const unsigned char* src = *srcp;
	int nb = utf8_trailing_bytes[*src];

	int ch = 0;
	switch (nb)
	{
		/* these fall through deliberately */
		case 3: ch += *src++; ch <<= 6;
		case 2:	ch += *src++; ch <<= 6;
		case 1:	ch += *src++; ch <<= 6;
		case 0:	ch += *src++;
	}

	ch -= utf8_read_offsets[nb];
	*srcp = src;
	return ch;
}

static void utf8_write(unsigned char** destp, int ch)
{
	unsigned char* dest = *destp;

	if (ch < 0x80)
	{
		*dest++ = (char) ch;
	}
	else if (ch < 0x800)
	{
		*dest++ = (ch >> 6) | 0xC0;
		*dest++ = (ch & 0x3F) | 0x80;
	}
	else if (ch < 0x10000)
	{
		*dest++ = (ch >> 12) | 0xE0;
		*dest++ = ((ch >> 6) & 0x3F) | 0x80;
		*dest++ = (ch & 0x3F) | 0x80;
	}
	else
	{
		*dest++ = (ch >> 18) | 0xF0;
		*dest++ = ((ch >> 12) & 0x3F) | 0x80;
		*dest++ = ((ch >> 6) & 0x3F) | 0x80;
		*dest++ = (ch & 0x3F) | 0x80;
	}

	*destp = dest;
}

struct s_string
{
	s_string_t* prev;
	s_string_t* next;
	const char* data;
	unsigned seglength;
	unsigned totallength;
	const char* cdata;
};

typedef void s_string_traverse_cb(s_string_t* s, void* user);

static void s_string_traverse(s_string_t* s, s_string_traverse_cb* cb, void* user)
{
	if (s->prev)
		s_string_traverse(s->prev, cb, user);
	cb(s, user);
	if (s->next)
		s_string_traverse(s->next, cb, user);
}

static void s_string_cdata_cb(s_string_t* s, void* user)
{
	char** pout = (char**) user;
	memcpy(*pout, s->data, s->seglength);
	(*pout) += s->seglength;
}

static const char* s_string_cdata(s_string_t* s)
{
	if (s->cdata)
		return s->cdata;

	char* outputbuffer = GC_MALLOC_ATOMIC(s->totallength + 1);

	char* pout = outputbuffer;
	s_string_traverse(s, s_string_cdata_cb, &pout);
	*pout = '\0';

	s->cdata = outputbuffer;
	return outputbuffer;
}

static s_string_t* s_create_string_constant(const char* data)
{
	s_string_t* s = GC_MALLOC(sizeof(s_string_t));
	s->next = s->prev = NULL;
	s->data = s->cdata = data;
	s->seglength = s->totallength = strlen(data);
	return s;
}

/* -------------------------------------------------------------------- */
/*                                 METHODS                              */
/* -------------------------------------------------------------------- */


/* Boolean methods */

static s_string_t s_true_label =
	{ NULL, NULL, "true", 4, 4, NULL };

static s_string_t s_false_label =
	{ NULL, NULL, "false", 5, 5, NULL };

#define S_METHOD_BOOLEAN__EQ(a, b, z) (*z) = (a) == (b)
#define S_METHOD_BOOLEAN__NE(a, b, z) (*z) = (a) != (b)
#define S_METHOD_BOOLEAN__NOT(b, z) (*z) = !(b)
#define S_METHOD_BOOLEAN__AND(a, b, z) (*z) = (a) & (b)
#define S_METHOD_BOOLEAN__OR(a, b, z) (*z) = (a) | (b)
#define S_METHOD_BOOLEAN__XOR(a, b, z) (*z) = (a) ^ (b)
#define S_METHOD_BOOLEAN_TOSTRING(a, z) (*z) = (a) ? &s_true_label : &s_false_label

/* Integer methods */

#define S_METHOD_INT__ADD(a, b, z) (*z) = (a) + (b)
#define S_METHOD_INT__SUB(a, b, z) (*z) = (a) - (b)
#define S_METHOD_INT__MULTIPLY(a, b, z) (*z) = (a) * (b)
#define S_METHOD_INT__DIVIDE(a, b, z) (*z) = (a) / (b)
#define S_METHOD_INT__MODULUS(a, b, z) (*z) = (a) % (b)
#define S_METHOD_INT__EQ(a, b, z) (*z) = (a) == (b)
#define S_METHOD_INT__NE(a, b, z) (*z) = (a) != (b)
#define S_METHOD_INT__GE(a, b, z) (*z) = (a) >= (b)
#define S_METHOD_INT__GT(a, b, z) (*z) = (a) > (b)
#define S_METHOD_INT__LE(a, b, z) (*z) = (a) <= (b)
#define S_METHOD_INT__LT(a, b, z) (*z) = (a) < (b)
#define S_METHOD_INT__SHL(a, b, z) (*z) = (a) << (b)
#define S_METHOD_INT__SHR(a, b, z) (*z) = (a) >> (b)

static void S_METHOD_INT_TOSTRING(int value, s_string_t** result)
{
	s_string_t* s = (s_string_t*) GC_MALLOC(sizeof(s_string_t));
	s->prev = s->next = NULL;

	char* buffer = (char*) GC_MALLOC_ATOMIC(32);
    sprintf(buffer, "%d", value);

    s->data = s->cdata = buffer;
    s->seglength = s->totallength = strlen(buffer);
    *result = s;
}

#define S_METHOD_INT_TOREAL(a, z) (*z) = (s_real_t)a


/* Real methods */

#define S_METHOD_REAL__ADD(a, b, z) (*z) = (a) + (b)
#define S_METHOD_REAL__SUB(a, b, z) (*z) = (a) - (b)
#define S_METHOD_REAL__MULTIPLY(a, b, z) (*z) = (a) * (b)
#define S_METHOD_REAL__DIVIDE(a, b, z) (*z) = (a) / (b)
#define S_METHOD_REAL__EQ(a, b, z) (*z) = (a) == (b)
#define S_METHOD_REAL__NE(a, b, z) (*z) = (a) != (b)
#define S_METHOD_REAL__GE(a, b, z) (*z) = (a) >= (b)
#define S_METHOD_REAL__GT(a, b, z) (*z) = (a) > (b)
#define S_METHOD_REAL__LE(a, b, z) (*z) = (a) <= (b)
#define S_METHOD_REAL__LT(a, b, z) (*z) = (a) < (b)

static void S_METHOD_REAL_TOSTRING(s_real_t value, s_string_t** result)
{
	s_string_t* s = (s_string_t*) GC_MALLOC(sizeof(s_string_t));
	s->prev = s->next = NULL;

	char* buffer = (char*) GC_MALLOC_ATOMIC(32);
    sprintf(buffer, "%f", value);

    s->data = s->cdata = buffer;
    s->seglength = s->totallength = strlen(buffer);
    *result = s;
}

#define S_METHOD_REAL_TOINT(a, z) (*z) = (s_int_t)a

/* String methods */

static void s_method_string_print_cb(s_string_t* s, void* user)
{
	fwrite(s->data, 1, s->seglength, stdout);
}

static void S_METHOD_STRING_PRINT(s_string_t* s)
{
	s_string_traverse(s, s_method_string_print_cb, NULL);
}

static void S_METHOD_STRING__ADD(s_string_t* left, s_string_t* right,
		s_string_t** result)
{
	s_string_t* newstring = (s_string_t*) GC_MALLOC(sizeof(s_string_t));
	newstring->prev = left;
	newstring->next = right;
	newstring->seglength = 0;
	newstring->totallength = left->totallength + right->totallength;
	newstring->data = newstring->cdata = NULL;
	*result = newstring;
}

static void S_METHOD_STRING__EQ(s_string_t* left, s_string_t* right,
		s_boolean_t* result)
{
	int count;
	const char* pleft = NULL;
	int lseg = 0;
	const char* pright = NULL;
	int rseg = 0;

	if (left == right)
		goto success;

	if (left->totallength != right->totallength)
		goto fail;

	count = left->totallength;

	lseg = left->seglength;
	pleft = left->data;

	rseg = right->seglength;
	pright = right->data;

	while (count--)
	{
		while (lseg == 0)
		{
			left = left->next;
			lseg = left->seglength;
			pleft = left->data;
		}

		while (rseg == 0)
		{
			right = right->next;
			rseg = right->seglength;
			pright = right->data;
		}

		if (*pleft++ != *pright++)
			goto fail;

		lseg--;
		rseg--;
	}

success:
	*result = 1;
	return;

fail:
	*result = 0;
	return;
}

#define S_METHOD_STRING__NE(l, r) !S_METHOD_STRING__EQ(l, r)

/* END cowbel runtime library */
