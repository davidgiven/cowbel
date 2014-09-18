/* © 2014 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

%include {
	#include "globals.h"
	#include <assert.h>
}

%token_type {token_t}

%syntax_error
{
	syntax_error = true;
}

start ::= optional_statements(IN) END_OF_FILE .
	{
		if (!syntax_error)
		{
			json_t* p = composite_token(IN, "object");
			json_object_set(p, "statements", IN);
			json_dumpf(p, stdout, JSON_INDENT(2) | JSON_ENSURE_ASCII);
		}
	}

%left OR .
%left AND .
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
operator(RESULT) ::= OPEN_ANGLE(T) .
	{
		RESULT = simple_token(&T, "identifier");
		json_object_set(RESULT, "value", json_string("<"));
	}
operator(RESULT) ::= CLOSE_ANGLE(T) .
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

%type string {json_t*}
string(RESULT) ::= STRING(T) .
	{
		RESULT = simple_token(&T, "string");
		json_object_set(RESULT, "value", T.value);
	}
string(RESULT) ::= string(LEFT) STRING(T) .
	{
		RESULT = composite_token(LEFT, "string");
		const char* s1 = json_string_value(json_object_get(LEFT, "value"));
		const char* s2 = json_string_value(T.value);
		char s[strlen(s1) + strlen(s2) + 1];
		sprintf(s, "%s%s", s1, s2);
		json_object_set(RESULT, "value", json_string(s));
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
expression_0(RESULT) ::= string(IN) .                { RESULT = IN; }
expression ::= OPEN_PARENTHESIS expression error .
	{ parse_error("expected ')'"); }
expression_0(RESULT) ::= OPEN_PARENTHESIS expression(IN) CLOSE_PARENTHESIS .
                                                     { RESULT = IN; }
expression ::= OPEN_BRACE optional_statements error .
	{ parse_error("expected '}' or statement"); }
expression_0(RESULT) ::= OPEN_BRACE(T) optional_statements(BODY) CLOSE_BRACE .
	{ RESULT = simple_token(&T, "block");
	  json_object_set(RESULT, "body", BODY); }

%type expression_1 {json_t*}
expression_1(RESULT) ::= expression_0(IN) .          { RESULT = IN; }
expression_1(RESULT) ::= expression_0(LEFT) DOT methodname(OP)
			bracketed_typerefs(TYPES)
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
expression_2 ::= operator(O) error .
	{ parse_error("failed to parse right hand of '%s' operator",
			json_string_value(json_object_get(O, "value"))); }
expression_2(RESULT) ::= operator(OP) expression_1(LEFT) .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", json_object_get(OP, "value"));
		json_object_set(RESULT, "receiver", LEFT);
		json_object_set(RESULT, "parameters", json_array());
	}

%type expression_3 {json_t*}
expression_3(RESULT) ::= expression_2(IN) .           { RESULT = IN; }
expression ::= expression_3 error .
	{ parse_error("invalid operator"); }
expression_3(RESULT) ::= expression_3(LEFT) operator(OP) expression_2(RIGHT) .
	{
		RESULT = composite_token(OP, "call");
		json_object_set(RESULT, "method", json_object_get(OP, "value"));
		json_object_set(RESULT, "receiver", LEFT);
		json_object_set(RESULT, "parameters", json_array_single(RIGHT));
	}

%type expression_4 {json_t*}
expression_4(RESULT) ::= expression_3(IN) .           { RESULT = IN; }
expression_4 ::= expression_4 OR error .
	{ parse_error("failed to parse right hand of 'or' operator"); }
expression_4(RESULT) ::= expression_4(LEFT) OR(OP) expression_4(RIGHT) .
	{
		RESULT = simple_token(&OP, "or");
		json_object_set(RESULT, "left", LEFT);
		json_object_set(RESULT, "right", RIGHT);
	}
expression_4 ::= expression_4 AND error .
	{ parse_error("failed to parse right hand of 'and' operator"); }
expression_4(RESULT) ::= expression_4(LEFT) AND(OP) expression_4(RIGHT) .
	{
		RESULT = simple_token(&OP, "and");
		json_object_set(RESULT, "left", LEFT);
		json_object_set(RESULT, "right", RIGHT);
	}
expression_4 ::= NOT error .
	{ parse_error("failed to parse right hand of 'not' operator"); }
expression_4(RESULT) ::= NOT(OP) expression_3(LEFT) .
	{
		RESULT = simple_token(&OP, "not");
		json_object_set(RESULT, "left", LEFT);
	}

%type expression {json_t*}
expression(RESULT) ::= expression_4(IN) .             { RESULT = IN; }

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

%type optional_typerefs {json_t*}
optional_typerefs(RESULT) ::= .                 { RESULT = json_array(); }
optional_typerefs(RESULT) ::= typerefs(IN) .   { RESULT = IN; }

%type typerefs {json_t*}
typerefs(RESULT) ::= typeref(IN) .             { RESULT = json_array_single(IN); }
typerefs(RESULT) ::= typerefs(LEFT) COMMA typeref(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

%type bracketed_typerefs {json_t*}
bracketed_typerefs(RESULT) ::= .                { RESULT = json_array(); }
bracketed_typerefs(RESULT) ::= OPEN_ANGLE optional_typerefs(IN) CLOSE_ANGLE .
	{
		RESULT = IN;
	}

/* --- Type expressions -------------------------------------------------- */

%type typeref {json_t*}
typeref(RESULT) ::= identifier(ID) bracketed_typerefs(TYPES) .
	{
		RESULT = composite_token(ID, "typeref");
		json_object_set(RESULT, "identifier", ID);
		json_object_set(RESULT, "typeargs", TYPES);
	}

%type typestatements {json_t*}
typestatements(RESULT) ::= .                     { RESULT = json_array(); }
typestatements(RESULT) ::= typestatements(LEFT) typestatement(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(RESULT, RIGHT);
	}

%type typestatement {json_t*}
typestatement(RESULT) ::= IMPLEMENTS(T) typeref(TYPE) SEMICOLON .
	{
		RESULT = simple_token(&T, "typeimplements");
		json_object_set(RESULT, "interface", TYPE);
	}
typestatement(RESULT) ::= functionspec(FUNC) SEMICOLON .
	{
		RESULT = composite_token(FUNC, "functiondef");
		json_object_set(RESULT, "functionspec", FUNC);
	}

%type typespec {json_t*}
typespec(RESULT) ::= typeref(IN) .               { RESULT = IN; }
typespec ::= OPEN_BRACE typestatements error .
	{ parse_error("expected '}' or interface declaration statement"); }
typespec(RESULT) ::= OPEN_BRACE(T) typestatements(STMTS) CLOSE_BRACE .
	{
		RESULT = simple_token(&T, "interfacedef");
		json_object_set(RESULT, "body", STMTS);
	}

/* --- Function parameters ----------------------------------------------- */

%type type_parameters {json_t*}
type_parameters(RESULT) ::= identifier(RIGHT) .  { RESULT = json_array_single(RIGHT); }
type_parameters(RESULT) ::= type_parameters(LEFT) COMMA identifier(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(LEFT, RIGHT);
	}

%type bracketed_type_parameters {json_t*}
bracketed_type_parameters(RESULT) ::= .          { RESULT = json_array(); }
bracketed_type_parameters(RESULT) ::= OPEN_ANGLE CLOSE_ANGLE . { RESULT = json_array(); }
bracketed_type_parameters(RESULT) ::= OPEN_ANGLE type_parameters(IN) CLOSE_ANGLE .
	{
		RESULT = IN;
	}

%type var_parameter {json_t*}
var_parameter(RESULT) ::= identifier(ID) COLON typeref(TYPE) .
	{
		RESULT = composite_token(ID, "parameter");
		json_object_set(RESULT, "identifier", ID);
		json_object_set(RESULT, "type", TYPE);
	}

%type var_parameters {json_t*}
var_parameters(RESULT) ::= var_parameter(LEFT) . { RESULT = json_array_single(LEFT); }
var_parameters(RESULT) ::= var_parameters(LEFT) COMMA var_parameter(RIGHT) .
	{
		RESULT = LEFT;
		json_array_append(LEFT, RIGHT);
	}

%type bracketed_var_parameters {json_t*}
bracketed_var_parameters(RESULT) ::= .          { RESULT = json_array(); }
bracketed_var_parameters(RESULT) ::= OPEN_PARENTHESIS CLOSE_PARENTHESIS .
												{ RESULT = json_array(); }
bracketed_var_parameters(RESULT) ::= OPEN_PARENTHESIS var_parameters(IN)
		CLOSE_PARENTHESIS .
	{
		RESULT = IN;
	}

/* --- Bits of statement ------------------------------------------------- */

/* Used in assignments and declarations. */

%type multiassign {json_t*}
multiassign ::= identifiers ASSIGN error .
	{ parse_error("expected list of expressions", NULL); }
multiassign(RESULT) ::= identifiers(LEFT) ASSIGN(T) values(RIGHT) .
	{ RESULT = simple_token(&T, "assign");
	  json_object_set(RESULT, "names", LEFT);
	  json_object_set(RESULT, "values", RIGHT); }

%type functionspec {json_t*}
functionspec(RESULT) ::= FUNCTION(T) is_overriding(OVERRIDING) methodname(NAME)
			bracketed_type_parameters(TYPES) bracketed_var_parameters(INS)
			return_types(OUTS) .
	{ RESULT = simple_token(&T, "function");
	  json_object_set(RESULT, "overriding", OVERRIDING ? json_true() : json_false());
	  json_object_set(RESULT, "identifier", NAME);
	  json_object_set(RESULT, "typeparams", TYPES);
	  json_object_set(RESULT, "inparams", INS);
	  json_object_set(RESULT, "outparams", OUTS); }

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

/* Single-token statements */

statement ::= BREAK error .
	{ missing_semicolon(); }
statement(RESULT) ::= BREAK(T) SEMICOLON .
	{ RESULT = simple_token(&T, "break"); }
statement ::= CONTINUE error .
	{ missing_semicolon(); }
statement(RESULT) ::= CONTINUE(T) SEMICOLON .
	{ RESULT = simple_token(&T, "continue"); }
statement ::= RETURN error .
	{ parse_error("expected ';' or expression", NULL); }
statement(RESULT) ::= RETURN(T) SEMICOLON .
	{ RESULT = simple_token(&T, "return"); }
 
/* Return with a single value */

statement ::= RETURN expression error .
	{ missing_semicolon(); }
statement(RESULT) ::= RETURN(T) expression(VAL) SEMICOLON .
	{ RESULT = simple_token(&T, "return");
	  json_object_set(RESULT, "value", VAL); }

/* Constructor */

statement ::= OPEN_BRACE optional_statements error .
	{ parse_error("expected '}' or statement", NULL); }
statement ::= OPEN_BRACE error .
	{ parse_error("expected '}' or statement", NULL); }
statement(RESULT) ::= OPEN_BRACE(T) optional_statements(BODY) CLOSE_BRACE .
	{ RESULT = simple_token(&T, "block");
	  json_object_set(RESULT, "body", BODY); }

/* Assignment */

statement ::= multiassign error .
	{ missing_semicolon(); }
statement(RESULT) ::= multiassign(LEFT) SEMICOLON .
	{ RESULT = LEFT; }

/* Variable declaratio and assignment */

statement(RESULT) ::= VAR(T) multiassign(LEFT) SEMICOLON .
	{
		RESULT = simple_token(&T, "declare");
		json_object_set(RESULT, "assignment", LEFT);
	}
statement ::= VAR multiassign error .
	{ parse_error("expected ';'", NULL); }

/* if...else */

statement(RESULT) ::= IF(T) OPEN_PARENTHESIS expression(LEFT) CLOSE_PARENTHESIS
			statement(IFTRUE) ELSE statement(IFFALSE) .
	{
		RESULT = simple_token(&T, "ifelse");
		json_object_set(RESULT, "condition", LEFT);
		json_object_set(RESULT, "iftrue", IFTRUE);
		json_object_set(RESULT, "iffalse", IFFALSE);
	}

/* if without else */

statement(RESULT) ::= IF(T) OPEN_PARENTHESIS expression(LEFT) CLOSE_PARENTHESIS
			statement(IFTRUE) .
	{
		RESULT = simple_token(&T, "ifelse");
		json_object_set(RESULT, "condition", LEFT);
		json_object_set(RESULT, "iftrue", IFTRUE);
	}

/* while {} */

statement(RESULT) ::= WHILE(T) OPEN_PARENTHESIS expression(COND) CLOSE_PARENTHESIS
			statement(BODY) .
	{
		RESULT = simple_token(&T, "while");
		json_object_set(RESULT, "condition", COND);
		json_object_set(RESULT, "body", BODY);
	}

/* do ... while */

statement(RESULT) ::= DO(T) statement(BODY) WHILE
			OPEN_PARENTHESIS expression(COND) CLOSE_PARENTHESIS SEMICOLON .
	{
		RESULT = simple_token(&T, "dowhile");
		json_object_set(RESULT, "condition", COND);
		json_object_set(RESULT, "body", BODY);
	}

/* extern statement */

statement(RESULT) ::= EXTERN(T) string(TEXT) SEMICOLON .
	{
		RESULT = simple_token(&T, "extern");
		json_object_set(RESULT, "text",
			json_object_get(TEXT, "value"));
	}

/* Function definition */

%type is_overriding {bool}
is_overriding(RESULT) ::= .                           { RESULT = false; }
is_overriding(RESULT) ::= OVERRIDING .                { RESULT = true; }

%type return_types {json_t*}
return_types(RESULT) ::= .                            { RESULT = json_array(); }
return_types(RESULT) ::= COLON bracketed_var_parameters(IN) . { RESULT = IN; }

statement ::= functionspec error .
	{ parse_error("invalid function body (probably unterminated"); }
statement(RESULT) ::= functionspec(FUNC) statement(BODY) .
	{
		RESULT = composite_token(FUNC, "function");
		json_object_set(RESULT, "functionspec", FUNC);
		json_object_set(RESULT, "body", BODY);
	}

/* Object implements interface */

%type delegates {json_t*}
delegates(RESULT) ::= .                               { RESULT = NULL; }
delegates(RESULT) ::= OPEN_PARENTHESIS expression(IN) CLOSE_PARENTHESIS .
                                                      { RESULT = IN; }

statement(RESULT) ::= IMPLEMENTS(T) typeref(TYPE) delegates(DELEGATES) SEMICOLON .
	{
		RESULT = simple_token(&T, "objectimplements");
		json_object_set(RESULT, "interface", TYPE);
		if (DELEGATES)
			json_object_set(RESULT, "delegates", DELEGATES);
	}

/* Type definition */

statement(RESULT) ::= TYPE(T) identifier(ID) bracketed_type_parameters(TYPES)
		ASSIGN typespec(TYPESPEC) SEMICOLON .
	{
		RESULT = simple_token(&T, "typedef");
		json_object_set(RESULT, "identifier", ID);
		json_object_set(RESULT, "typeparams", TYPES);
		json_object_set(RESULT, "body", TYPESPEC);
	}

