%include {
	#include "globals.h"
	#include <assert.h>
}

%token_type {token_t}

%syntax_error
{
	fprintf(stderr, "Parse error!\n");
}

start ::= leaves(in) .
	{
		json_dumpf(in, stdout, JSON_INDENT(2));
	}

%type leaves {json_t*}
leaves(RESULT) ::= expression(IN) .
	{
		RESULT = json_array();
		json_array_append(RESULT, IN);
	}
leaves(RESULT) ::= leaves(LIST) expression(ITEM) .
	{
		RESULT = LIST;
		json_array_append(RESULT, ITEM);
	}

%type leaf {json_t*}
leaf(RESULT) ::= identifier(IN) .              { RESULT = IN; }
leaf(RESULT) ::= integer(IN) .                 { RESULT = IN; }
leaf(RESULT) ::= real(IN) .                    { RESULT = IN; }
leaf(RESULT) ::= OPEN_PARENTHESIS leaf(IN) CLOSE_PARENTHESIS .
	{
		RESULT = IN;
	}

%type expression {json_t*}
expression(RESULT) ::= leaf(IN) .              { RESULT = IN; }
expression(RESULT) ::= binary_operator(IN) .   { RESULT = IN; }

%type binary_operator {json_t*}
binary_operator(RESULT) ::= expression(LEFT) OPERATOR(OP) leaf(RIGHT) .
	{
		RESULT = simple_token(&OP, "call");
		json_object_set(RESULT, "method", OP.value);
		json_object_set(RESULT, "left", LEFT);

		json_t* args = json_array();
		json_array_append(args, RIGHT);
		json_object_set(RESULT, "right", args);
	}

%type identifier {json_t*}
identifier(RESULT) ::= IDENTIFIER(T) .
	{
		RESULT = simple_token(&T, "identifier");
		json_object_set(RESULT, "value", T.value);
	}

%type integer {json_t*}
integer(RESULT) ::= INTEGER(T) .
	{
		long value = strtol(json_string_value(T.value), NULL, 0);

		RESULT = simple_token(&T, "integer");
		json_object_set(RESULT, "value", json_integer(value));
	}

%type real {json_t*}
real(RESULT) ::= REAL(T) .
	{
		long value = strtod(json_string_value(T.value), NULL);

		RESULT = simple_token(&T, "real");
		json_object_set(RESULT, "value", json_real(value));
	}

