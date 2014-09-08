%include {
	#include "globals.h"
	#include <assert.h>
}

%token_type {token_t}

%syntax_error
{
	fprintf(stderr, "Parse error at %s:%d.%d\n",
		json_string_value(current_filename),
		current_lineno, current_column);
}

start ::= statements(IN) .
	{
		json_dumpf(IN, stdout, JSON_INDENT(2));
	}

/* --- Primitives -------------------------------------------------------- */

%type identifier {json_t*}
identifier(RESULT) ::= IDENTIFIER(T) .
	{
		RESULT = simple_token(&T, "identifier");
		json_object_set(RESULT, "value", T.value);
	}

%type operator {json_t*}
operator(RESULT) ::= OPERATOR(T) .
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

/* --- Utilities --------------------------------------------------------- */

%type methodname {json_t*}
methodname(RESULT) ::= identifier(IN) .              { RESULT = IN; }
methodname(RESULT) ::= operator(IN) .                { RESULT = IN; }

/* --- Value expressions ------------------------------------------------- */

%left DOT .
%left OPERATOR .

%type expression {json_t*}
expression(RESULT) ::= identifier(IN) .              { RESULT = IN; }
expression(RESULT) ::= integer(IN) .                 { RESULT = IN; }
expression(RESULT) ::= real(IN) .                    { RESULT = IN; }
expression(RESULT) ::= OPEN_PARENTHESIS expression(IN) CLOSE_PARENTHESIS .  { RESULT = IN; }

expression(RESULT) ::= expression(LEFT) DOT methodname(OP) values(RIGHT) .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", OP);
		json_object_set(RESULT, "left", LEFT);
		json_object_set(RESULT, "right", RIGHT);
	}

expression(RESULT) ::= expression(LEFT) OPERATOR(OP) expression(RIGHT) .
	{
		RESULT = simple_token(&OP, "call");
		json_object_set(RESULT, "method", OP.value);
		json_object_set(RESULT, "left", LEFT);

		json_t* args = json_array();
		json_array_append(args, RIGHT);
		json_object_set(RESULT, "right", args);
	}

/* --- Lists of values --------------------------------------------------- */

%type values {json_t*}
values(RESULT) ::= OPEN_PARENTHESIS CLOSE_PARENTHESIS .
	{
		RESULT = json_array();
	}

/* --- Statements -------------------------------------------------------- */

%type statements {json_t*}
statements(RESULT) ::= statement(IN) .          { RESULT = IN; }
statements(RESULT) ::= statements(LEFT) statement(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

%type statement {json_t*}
statement(RESULT) ::= expression(IN) SEMICOLON .
	{
		RESULT = composite_token(IN, "expression");
		json_object_set(RESULT, "left", IN);
	}

