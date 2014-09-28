/* © 2014 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

%{
	#include "globals.h"
	#include <assert.h>
%}

%define api.pure full
%define api.push-pull push
%define parse.error verbose
%define parse.lac full

%expect 1

%union {
	token_t token;
	json_t* node;
}

%token END 0 "end of file"
%token <token> AND "and"
%token <token> ASSIGN "="
%token <token> BREAK "break"
%token <token> CLOSE_ANGLE ">"
%token <token> CLOSE_BRACE "}"
%token <token> CLOSE_PARENTHESIS ")"
%token <token> COLON ":"
%token <token> COMMA ","
%token <token> CONTINUE "continue"
%token <token> DO "do"
%token <token> DOT "."
%token <token> ELSE "else"
%token <token> EXTERN "extern"
%token <token> FALSE "false"
%token <token> FUNCTION "function"
%token <token> IDENTIFIER "identifier"
%token <token> IF "if"
%token <token> IMPLEMENTS "implements"
%token <token> INTEGER "integer"
%token <token> NOT "not"
%token <token> OPEN_ANGLE "<"
%token <token> OPEN_BRACE "{"
%token <token> OPEN_PARENTHESIS "("
%token <token> OPERATOR "operator"
%token <token> OR "or"
%token <token> OVERRIDING "overriding"
%token <token> REAL "real"
%token <token> RETURN "return"
%token <token> SEMICOLON ";"
%token <token> STRING "string"
%token <token> TRUE "true"
%token <token> TYPE "type"
%token <token> VAR "var"
%token <token> WHILE "while"
%type <node> boolean
%type <node> bracketed_type_parameters
%type <node> bracketed_typerefs
%type <node> bracketed_typed_var_parameters
%type <node> expression
%type <node> expression_0
%type <node> expression_1
%type <node> expression_2
%type <node> expression_3
%type <node> expression_4
%type <node> functionspec
%type <node> identifier
%type <node> input_parameters
%type <node> integer
%type <node> is_overriding
%type <node> maybetyped_var_parameter
%type <node> maybetyped_var_parameters
%type <node> methodcall
%type <node> methodname
%type <node> multiassign
%type <node> multideclare
%type <node> operator
%type <node> optional_statements
%type <node> optional_typerefs
%type <node> optional_values
%type <node> real
%type <node> return_parameters
%type <node> statement
%type <node> statements
%type <node> string
%type <node> type_parameters
%type <node> typed_var_parameter
%type <node> typed_var_parameters
%type <node> typedef
%type <node> typeref
%type <node> typerefs
%type <node> typestatement
%type <node> typestatements
%type <node> untyped_var_parameter
%type <node> untyped_var_parameters
%type <node> values

%right ELSE IF
%left OR
%left AND
%left NOT

%%

start:
	optional_statements 
		{
			if (yynerrs == 0)
			{
				json_t* p = composite_token($1, "object");
				json_object_set(p, "statements", $1);
				json_dumpf(p, stdout, JSON_INDENT(2) | JSON_ENSURE_ASCII);
			}
		}
	;

/* --- Primitives -------------------------------------------------------- */

identifier:
	IDENTIFIER
		{
			$$ = simple_token(&$1, "identifier");
			json_object_set($$, "value", $1.value);
		}
	;

operator:
	OPERATOR
		{
			$$ = simple_token(&$1, "identifier");
			json_object_set($$, "value", $1.value);
		}
	| "<"
		{
			$$ = simple_token(&$1, "identifier");
			json_object_set($$, "value", json_string("<"));
		}
	| ">"
		{
			$$ = simple_token(&$1, "identifier");
			json_object_set($$, "value", json_string("<"));
		}
	;

integer:
	INTEGER
		{
			long value = strtol(json_string_value($1.value), NULL, 0);

			$$ = simple_token(&$1, "integer");
			json_object_set($$, "value", json_integer(value));
		}
	;

real:
	REAL
		{
			double value = strtod(json_string_value($1.value), NULL);

			$$ = simple_token(&$1, "real");
			json_object_set($$, "value", json_real(value));
		}
	;

boolean:
	TRUE
		{
			$$ = simple_token(&$1, "boolean");
			json_object_set($$, "value", json_true());
		}
	| FALSE
		{
			$$ = simple_token(&$1, "boolean");
			json_object_set($$, "value", json_false());
		}
	;

string:
	STRING
		{
			$$ = simple_token(&$1, "string");
			json_object_set($$, "value", $1.value);
		}
	| string STRING
		{
			$$ = composite_token($1, "string");
			const char* s1 = json_string_value(json_object_get($1, "value"));
			const char* s2 = json_string_value($2.value);
			char s[strlen(s1) + strlen(s2) + 1];
			sprintf(s, "%s%s", s1, s2);
			json_object_set($$, "value", json_string(s));
		}
	;


/* --- Utilities --------------------------------------------------------- */

methodname:
	identifier
		{ $$ = $1; }
	| operator
		{ $$ = $1; }
	;

methodcall:
	expression_1[expr] "."[t] methodname bracketed_typerefs "("
	optional_values ")"
		{
			$$ = simple_token(&$t, "call");
			json_object_set($$, "method", $methodname);
			json_object_set($$, "interfaces", $bracketed_typerefs);
			json_object_set($$, "receiver", $expr);
			json_object_set($$, "parameters", $optional_values);
		}
	;

/* --- Value expressions ------------------------------------------------- */

expression_0:
	identifier
		{ $$ = $1; }
	| integer
		{ $$ = $1; }
	| real
		{ $$ = $1; }
	| boolean
		{ $$ = $1; }
	| string
		{ $$ = $1; }
	| "(" expression ")"
		{ $$ = $2; }
	| "{" optional_statements "}"
		{
			$$ = simple_token(&$1, "block");
			json_object_set($$, "body", $2);
		}
	| EXTERN string "{" optional_statements "}"
		{
			$$ = simple_token(&$3, "block");
			json_object_set($$, "nativetype", $2);
			json_object_set($$, "body", $4);
		}
	;

expression_1:
	expression_0
		{ $$ = $1; }
	| methodcall
		{ $$ = $1; }
	;

expression_2:
	expression_1
		{ $$ = $1; }
	| operator expression_1
		{
			$$ = composite_token($1, "call");
			json_object_set($$, "method", json_object_get($1, "value"));
			json_object_set($$, "receiver", $2);
			json_object_set($$, "parameters", json_array());
		}
	;

expression_3:
	expression_2
		{ $$ = $1; }
	| expression_3 operator expression_2
		{
			$$ = composite_token($2, "call");
			json_object_set($$, "method", json_object_get($2, "value"));
			json_object_set($$, "receiver", $1);
			json_object_set($$, "parameters", json_array_single($3));
		}
	;

expression_4:
	expression_3
		{ $$ = $1; }
	| expression_4 OR expression_4
		{
			$$ = simple_token(&$2, "or");
			json_object_set($$, "left", $1);
			json_object_set($$, "right", $3);
		}
	| expression_4 AND expression_4
		{
			$$ = simple_token(&$2, "and");
			json_object_set($$, "left", $1);
			json_object_set($$, "right", $3);
		}
	| NOT expression_4
		{
			$$ = simple_token(&$1, "not");
			json_object_set($$, "left", $2);
		}
	;

expression:
	expression_4
		{ $$ = $1; }
	;


/* --- Lists of values --------------------------------------------------- */

optional_values:
	%empty
		{ $$ = json_array(); }
	| values
		{ $$ = $1; }
	;

values:
	expression
		{ $$ = json_array_single($1); }
	| values "," expression
		{
			$$ = $1;
			json_array_append($$, $3);
		}
	;

/* --- Lists of type names ----------------------------------------------- */

optional_typerefs:
	%empty
		{ $$ = json_array(); }
	| typerefs
		{ $$ = $1; }
	;

typerefs:
	typeref
		{ $$ = json_array_single($1); }
	| typerefs "," typeref
		{
			$$ = $1;
			json_array_append($$, $3);
		}
	;

bracketed_typerefs:
	%empty
		{ $$ = json_array(); }
	| "<" optional_typerefs ">"
		{ $$ = $2; }
	;

/* --- Type expressions -------------------------------------------------- */

typeref:
	identifier bracketed_typerefs
		{
			$$ = composite_token($1, "typeref");
			json_object_set($$, "identifier", $1);
			json_object_set($$, "typeargs", $2);
		}
	;

typestatements:
	%empty
		{ $$ = json_array(); }
	| typestatements[left] typestatement[right]
		{
			$$ = $left;
			json_array_append($$, $right);
		}
	;

typestatement:
	IMPLEMENTS typeref ";"
		{
			$$ = simple_token(&$IMPLEMENTS, "typeimplements");
			json_object_set($$, "interface", $typeref);
		}
	| functionspec ";"
		{
			$$ = composite_token($functionspec, "typefunction");
			json_object_set($$, "functionspec", $functionspec);
		}
	;

typedef:
	typeref
		{ $$ = $typeref; }
	| "{"[t] typestatements "}"
		{
			$$ = simple_token(&$t, "interfacedef");
			json_object_set($$, "body", $typestatements);
		}
	;

/* --- Function parameters ----------------------------------------------- */

type_parameters:
	identifier
		{ $$ = json_array_single($identifier); }
	| type_parameters "," identifier
		{
			$$ = $1;
			json_array_append($$, $identifier);
		}
	;

bracketed_type_parameters:
	%empty
		{ $$ = json_array(); }
	| "<" ">"
		{ $$ = json_array(); }
	| "<" type_parameters ">"
		{ $$ = $type_parameters; }
	;

typed_var_parameter:
	identifier ":" typeref
		{
			$$ = composite_token($identifier, "parameter");
			json_object_set($$, "identifier", $identifier);
			json_object_set($$, "interface", $typeref);
		}
	;

untyped_var_parameter:
	identifier
		{
			$$ = composite_token($identifier, "parameter");
			json_object_set($$, "identifier", $identifier);
		}
	;

maybetyped_var_parameter:
	typed_var_parameter
		{ $$ = $1; }
	| untyped_var_parameter
		{ $$ = $1; }
	;

typed_var_parameters:
	typed_var_parameter
		{ $$ = json_array_single($1); }
	| typed_var_parameters[left] "," typed_var_parameter[right]
		{
			$$ = $left;
			json_array_append($$, $right);
		}
	;

maybetyped_var_parameters:
	maybetyped_var_parameter
		{ $$ = json_array_single($1); }
	| maybetyped_var_parameters[left] "," maybetyped_var_parameter[right]
		{
			$$ = $left;
			json_array_append($$, $right);
		}
	;

untyped_var_parameters:
	untyped_var_parameter
		{ $$ = json_array_single($1); }
	| untyped_var_parameters[left] "," untyped_var_parameter[right]
		{
			$$ = $left;
			json_array_append($$, $right);
		}
	;

bracketed_typed_var_parameters:
	"(" ")"
		{ $$ = json_array(); }
	| "(" typed_var_parameters ")"
		{ $$ = $typed_var_parameters; }
	;

/* --- Bits of statement ------------------------------------------------- */

/* Used in assignments and declarations. */

multiassign:
	untyped_var_parameters[left] "="[t] values[right]
		{
			$$ = simple_token(&$t, "assign");
			json_object_set($$, "variables", $left);
			json_object_set($$, "values", $right);
		}
	;

multideclare:
	VAR[t] maybetyped_var_parameters[left] "=" values[right]
		{
			$$ = simple_token(&$t, "declare");
			json_object_set($$, "variables", $left);
			json_object_set($$, "values", $right);
		}
	;

functionspec:
	FUNCTION is_overriding methodname bracketed_type_parameters input_parameters
	return_parameters
		{
			$$ = simple_token(&$FUNCTION, "function");
			json_object_set($$, "overriding", $is_overriding);
			json_object_set($$, "identifier", $methodname);
			json_object_set($$, "typeparams", $bracketed_type_parameters);
			json_object_set($$, "inparams", $input_parameters);
			json_object_set($$, "outparams", $return_parameters);
		}
	;

is_overriding:
	%empty
		{ $$ = json_false(); }
	| OVERRIDING
		{ $$ = json_true(); }
	;

input_parameters:
	%empty
		{ $$ = json_array(); }
	| bracketed_typed_var_parameters
		{ $$ = $1; }
	;

return_parameters:
	%empty
		{ $$ = json_array(); }
	| ":" bracketed_typed_var_parameters
		{ $$ = $bracketed_typed_var_parameters; }
	| ":" typeref
		{
			json_t* param = composite_token($typeref, "parameter");
			json_object_set(param, "identifier", json_string("__return"));
			json_object_set(param, "interface", $typeref);
			$$ = json_array_single(param);
		}
	;

/* --- Statements -------------------------------------------------------- */

optional_statements:
	%empty
		{ $$ = json_array(); }
	| statements
		{ $$ = $1; }
	;

statements:
	statement
		{ $$ = json_array_single($1); }
	| statements statement
		{
			$$ = $1;
			json_array_append($$, $2);
		}
	;

statement:
/* Single-token statements */
	BREAK ";"
		{ $$ = simple_token(&$1, "break"); }
	| CONTINUE ";"
		{ $$ = simple_token(&$1, "continue"); }
	| RETURN ";"
		{ $$ = simple_token(&$1, "return"); }
/* Return with a single value */
	| RETURN expression ";"
		{
			$$ = simple_token(&$1, "return");
		    json_object_set($$, "value", $2);
		}
/* Statement constructor */
	| "{" optional_statements "}"
		{
			$$ = simple_token(&$1, "block");
			json_object_set($$, "body", $2);
		}
/* Void method call */
	| methodcall ";"
		{ $$ = $1; }
/* Assignment */
	| multiassign ";"
		{ $$ = $1; }
/* Declaration (subtly different semantically from assignment) */
	| multideclare ";"
		{ $$ = $1; }
/* if...else */
	| IF "(" expression ")" statement ELSE statement
	%prec ELSE
		{
			$$ = simple_token(&$1, "ifelse");
			json_object_set($$, "condition", $3);
			json_object_set($$, "iftrue", $5);
			json_object_set($$, "iffalse", $7);
		}
/* if without else */
	| IF "(" expression ")" statement
	%prec IF
		{
			$$ = simple_token(&$1, "ifelse");
			json_object_set($$, "condition", $3);
			json_object_set($$, "iftrue", $5);
		}
/* while {} */
	| WHILE "(" expression ")" statement
		{
			$$ = simple_token(&$1, "while");
			json_object_set($$, "condition", $3);
			json_object_set($$, "body", $5);
		}
/* do ... while */
	| DO statement WHILE "(" expression ")" ";"
		{
			$$ = simple_token(&$1, "dowhile");
			json_object_set($$, "condition", $5);
			json_object_set($$, "body", $2);
		}
/* extern statement */
	| EXTERN string ";"
		{
			$$ = simple_token(&$1, "extern");
			json_object_set($$, "text",
				json_object_get($2, "value"));
		}
/* Function definition */
	| functionspec statement[body]
		{
			$$ = composite_token($functionspec, "function");
			json_object_set($$, "functionspec", $functionspec);
			json_object_set($$, "body", $body);
		}
/* Object implements interface */
	| IMPLEMENTS typeref ";"
		{
			$$ = simple_token(&$1, "objectimplements");
			json_object_set($$, "interface", $2);
		}
	| IMPLEMENTS typeref "(" expression ")" ";"
		{
			$$ = simple_token(&$1, "objectimplements");
			json_object_set($$, "interface", $2);
			json_object_set($$, "delegates", $4);
		}
/* Type definition */
	| TYPE identifier bracketed_type_parameters "=" typedef ";"
		{
			$$ = simple_token(&$TYPE, "typedef");
			json_object_set($$, "identifier", $identifier);
			json_object_set($$, "typeparams", $bracketed_type_parameters);
			json_object_set($$, "body", $typedef);
		}
	;
/* Error recovery */
	| error ";"
		{ yyerrok; }
%%

