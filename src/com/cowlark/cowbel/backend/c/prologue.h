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

typedef int s_boolean_t;
typedef int s_int_t;

typedef struct s_string s_string_t;
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

typedef struct s_array s_array_t;
struct s_array
{
	unsigned length;
	unsigned allocedlength;
	unsigned itemsize;
	char* data;
};

#define S_ALLOC_CONSTRUCTOR(type) \
	((sizeof(type) > 0) ? ((type*) calloc(1, sizeof(type))) : NULL)

#define S_CONSTRUCT_CONSTANT_STRING(data) \
	((s_string_t

static void s_throw(const char* message)
{
	fflush(stdout);
	fprintf(stderr, "Runtime error: %s\n", message);
	exit(1);
}

/* Boolean methods */

#define S_METHOD_BOOLEAN__EQ(a, b, z) (*z) = (a) == (b)
#define S_METHOD_BOOLEAN__NE(a, b, z) (*z) = (a) != (b)
#define S_METHOD_BOOLEAN__NOT(b, z) (*z) = !(b)
#define S_METHOD_BOOLEAN__AND(a, b, z) (*z) = (a) & (b)
#define S_METHOD_BOOLEAN__OR(a, b, z) (*z) = (a) | (b)
#define S_METHOD_BOOLEAN__XOR(a, b, z) (*z) = (a) ^ (b)

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

static void S_METHOD_INT_TOSTRING(int value, s_string_t** result)
{
	s_string_t* s = (s_string_t*) malloc(sizeof(s_string_t));
	s->prev = s->next = NULL;

	char* buffer = (char*) malloc(32);
    sprintf(buffer, "%d", value);

    s->data = s->cdata = buffer;
    s->seglength = s->totallength = strlen(buffer);
    *result = s;
}

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
	s_string_t* newstring = (s_string_t*) malloc(sizeof(s_string_t));
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
