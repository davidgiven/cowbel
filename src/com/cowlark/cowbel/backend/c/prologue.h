/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <stdarg.h>
#include <string.h>

typedef bool s_boolean_t;
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
	((type*) malloc(sizeof(type)))

#define S_CONSTRUCT_CONSTANT_STRING(data) \
	((s_string_t

static void s_throw(const char* message)
{
	fflush(stdout);
	fprintf(stderr, "Runtime error: %s\n", message);
	exit(1);
}

/* Boolean methods */

#define S_METHOD_BOOLEAN__NOT(b, z) (*z) = !(b)
#define S_METHOD_BOOLEAN__OR(a, b, z) (*z) = (a) | (b)

/* Integer methods */

#define S_METHOD_INTEGER__ADD(a, b, z) (*z) = (a) + (b)
#define S_METHOD_INTEGER__SUB(a, b, z) (*z) = (a) - (b)
#define S_METHOD_INTEGER__EQUALS(a, b, z) (*z) = (a) == (b)
#define S_METHOD_INTEGER__NOTEQUALS(a, b, z) (*z) = (a) != (b)
#define S_METHOD_INTEGER__GT(a, b, z) (*z) = (a) > (b)
#define S_METHOD_INTEGER__LT(a, b, z) (*z) = (a) < (b)

static void S_METHOD_INTEGER_TOSTRING(int value, s_string_t** result)
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
	putchar('\n');
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

static void S_METHOD_STRING__EQUALS(s_string_t* left, s_string_t* right,
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

/* Array methods */

static s_array_t* s_construct_array(unsigned itemsize, unsigned length)
{
	s_array_t* array = (s_array_t*) malloc(sizeof(s_array_t));
	array->length = array->allocedlength = length;
	array->itemsize = itemsize;
	array->data = (char*) malloc(itemsize * length);
	memset(array->data, 0, itemsize * length);
	return array;
}

#define S_CONSTRUCT_ARRAY(type, length) \
	((type*) s_construct_array(sizeof(type), length))

template <class T> T* S_INIT_ARRAY(T* a, ...)
{
	s_array_t* array = (s_array_t*) a;
	va_list ap;

	va_start(ap, a);
	T* data = (T*) array->data;
	for (unsigned i = 0; i < array->length; i++)
	{
		T value = va_arg(ap, T);
		data[i] = value;
	}

	return a;
}

static void* s_array_get_lvalue(s_array_t* array, unsigned index)
{
	if (index >= array->length)
		s_throw("Array access out of bounds");

	return (void*) (array->data + index*array->itemsize);
}

template <class T> void S_METHOD_ARRAY_GET(T* array, unsigned index, T* value)
{
	*value = *(T*) s_array_get_lvalue((s_array_t*) array, index);
}

template <class T> void S_METHOD_ARRAY_SET(T* array, unsigned index, T value)
{
	(*(T*) s_array_get_lvalue((s_array_t*) array, index)) = value;
}

template <class T> void S_METHOD_ARRAY_RESIZE(T* a, unsigned newlength, T value)
{
	s_array_t* array = (s_array_t*) a;

	if (newlength < array->length)
	{
		array->length = newlength;
		return;
	}

	if (newlength > array->allocedlength)
	{
		array->data = (char*) realloc(array->data, newlength * array->itemsize);
		array->allocedlength = newlength;
	}

	T* data = (T*) array->data;
	for (unsigned i = array->length; i < newlength; i++)
		data[i] = value;

	array->length = newlength;
}
