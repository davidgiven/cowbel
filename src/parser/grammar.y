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
	syntax_error = true;
}

start ::= optional_statements(IN) .
	{
		json_t* p = composite_token(IN, "object");
		json_object_set(p, "statements", IN);
		json_dumpf(p, stdout, JSON_INDENT(2));
	}

%left DOT .
%left PREFIX .
%left INFIX OPERATOR .
%left IF .
%left ELSE .

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
operator(RESULT) ::= LEFT_ANGLE(T) .
	{
		RESULT = simple_token(&T, "identifier");
		json_object_set(RESULT, "value", json_string("<"));
	}
operator(RESULT) ::= RIGHT_ANGLE(T) .
	{
		RESULT = simple_token(&T, "identifier");
		json_object_set(RESULT, "value", json_string(">"));
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
		double value = strtod(json_string_value(T.value), NULL);

		RESULT = simple_token(&T, "real");
		json_object_set(RESULT, "value", json_real(value));
	}

%type boolean {json_t*}
boolean(RESULT) ::= TRUE(T) .
	{
		RESULT = simple_token(&T, "boolean");
		json_object_set(RESULT, "value", json_true());
	}
boolean(RESULT) ::= FALSE(T) .
	{
		RESULT = simple_token(&T, "boolean");
		json_object_set(RESULT, "value", json_false());
	}

/* --- Utilities --------------------------------------------------------- */

%type methodname {json_t*}
methodname(RESULT) ::= identifier(IN) .              { RESULT = IN; }
methodname(RESULT) ::= operator(IN) .                { RESULT = IN; }

/* --- Value expressions ------------------------------------------------- */

%type expression_0 {json_t*}
expression_0(RESULT) ::= identifier(IN) .            { RESULT = IN; }
expression_0(RESULT) ::= integer(IN) .               { RESULT = IN; }
expression_0(RESULT) ::= real(IN) .                  { RESULT = IN; }
expression_0(RESULT) ::= boolean(IN) .               { RESULT = IN; }
expression_0(RESULT) ::= OPEN_PARENTHESIS expression(IN) CLOSE_PARENTHESIS .
                                                     { RESULT = IN; }

%type expression_1 {json_t*}
expression_1(RESULT) ::= expression_0(IN) .          { RESULT = IN; }
expression_1(RESULT) ::= expression_0(LEFT) DOT methodname(OP)
			bracketed_typenames(TYPES)
			OPEN_PARENTHESIS optional_values(RIGHT) CLOSE_PARENTHESIS .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", OP);
		json_object_set(RESULT, "types", TYPES);
		json_object_set(RESULT, "receiver", LEFT);
		json_object_set(RESULT, "parameters", RIGHT);
	}

%type expression_2 {json_t*}
expression_2(RESULT) ::= expression_1(IN) .           { RESULT = IN; }
expression_2(RESULT) ::= operator(OP) expression_1(LEFT) .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", json_object_get(OP, "value"));
		json_object_set(RESULT, "receiver", LEFT);
		json_object_set(RESULT, "parameters", json_array());
	}

%type expression_3 {json_t*}
expression_3(RESULT) ::= expression_2(IN) .           { RESULT = IN; }
expression_3(RESULT) ::= expression_3(LEFT) operator(OP) expression_2(RIGHT) .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", json_object_get(OP, "value"));
		json_object_set(RESULT, "receiver", LEFT);
		json_object_set(RESULT, "parameters", json_array_single(RIGHT));
	}

%type expression {json_t*}
expression(RESULT) ::= expression_3(IN) .             { RESULT = IN; }

/* --- Type expressions -------------------------------------------------- */

%type typename {json_t*}
typename(RESULT) ::= identifier(IN) .                { RESULT = IN; }

/* --- Lists of values --------------------------------------------------- */

%type optional_values {json_t*}
optional_values(RESULT) ::= .
	{
		RESULT = json_array();
	}
optional_values(RESULT) ::= values(IN) .         { RESULT = IN; }

%type values {json_t*}
values(RESULT) ::= expression(IN) .
	{
		RESULT = json_array();
		json_array_append(RESULT, IN);
	}
values(RESULT) ::= values(LEFT) COMMA expression(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

/* --- Lists of identifiers ---------------------------------------------- */

%type identifiers {json_t*}
identifiers(RESULT) ::= identifier(ID) .
	{
		RESULT = json_array();
		json_array_append(RESULT, ID);
	}
identifiers(RESULT) ::= identifiers(LEFT) COMMA identifier(ID) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, ID);
	}

/* --- Lists of type names ----------------------------------------------- */

%type optional_typenames {json_t*}
optional_typenames(RESULT) ::= .                 { RESULT = json_array(); }
optional_typenames(RESULT) ::= typenames(IN) .   { RESULT = IN; }

%type typenames {json_t*}
typenames(RESULT) ::= typename(IN) .             { RESULT = json_array_single(IN); }
typenames(RESULT) ::= typenames(LEFT) COMMA typename(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

%type bracketed_typenames {json_t*}
bracketed_typenames(RESULT) ::= .                { RESULT = json_array(); }
bracketed_typenames(RESULT) ::= LEFT_ANGLE optional_typenames(IN) RIGHT_ANGLE .
	{
		RESULT = IN;
	}

/* --- Bits of statement ------------------------------------------------- */

/* Used in assignments and declarations. */

%type multiassign {json_t*}
multiassign(RESULT) ::= identifiers(LEFT) ASSIGN(T) values(RIGHT) .
	{
		RESULT = simple_token(&T, "assign");
		json_object_set(RESULT, "names", LEFT);
		json_object_set(RESULT, "values", RIGHT);
	}

/* --- Statements -------------------------------------------------------- */

%type optional_statements {json_t*}
optional_statements(RESULT) ::= .                { RESULT = json_array(); }
optional_statements(RESULT) ::= statements(IN) . { RESULT = IN; }

%type statements {json_t*}
statements(RESULT) ::= statement(IN) .
	{
		RESULT = json_array();
		json_array_append(RESULT, IN);
	}
statements(RESULT) ::= statements(LEFT) statement(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

%type statement {json_t*}
statement(RESULT) ::= OPEN_BRACE(T) optional_statements(BODY) CLOSE_BRACE .
	{
		RESULT = simple_token(&T, "object");
		json_object_set(RESULT, "body", BODY);
	}
statement(RESULT) ::= multiassign(LEFT) SEMICOLON .
	{
		RESULT = LEFT;
	}
statement(RESULT) ::= VAR(T) multiassign(LEFT) SEMICOLON .
	{
		RESULT = simple_token(&T, "declare");
		json_object_set(RESULT, "assignment", LEFT);
	}
statement(RESULT) ::= IF(T) OPEN_PARENTHESIS expression(LEFT) CLOSE_PARENTHESIS
			statement(IFTRUE) ELSE statement(IFFALSE) .
	{
		RESULT = simple_token(&T, "ifelse");
		json_object_set(RESULT, "condition", LEFT);
		json_object_set(RESULT, "iftrue", IFTRUE);
		json_object_set(RESULT, "iffalse", IFFALSE);
	}
statement(RESULT) ::= IF(T) OPEN_PARENTHESIS expression(LEFT) CLOSE_PARENTHESIS
			statement(IFTRUE) .
	{
		RESULT = simple_token(&T, "ifelse");
		json_object_set(RESULT, "condition", LEFT);
		json_object_set(RESULT, "iftrue", IFTRUE);
	}
statement(RESULT) ::= WHILE(T) OPEN_PARENTHESIS expression(COND) CLOSE_PARENTHESIS
			statement(BODY) .
	{
		RESULT = simple_token(&T, "while");
		json_object_set(RESULT, "condition", COND);
		json_object_set(RESULT, "body", BODY);
	}
statement(RESULT) ::= DO(T) statement(BODY) WHILE
			OPEN_PARENTHESIS expression(COND) CLOSE_PARENTHESIS SEMICOLON .
	{
		RESULT = simple_token(&T, "dowhile");
		json_object_set(RESULT, "condition", COND);
		json_object_set(RESULT, "body", BODY);
	}

