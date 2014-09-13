%{
	#include "globals.h"
	#include "grammar.h"

	#define YY_USER_ACTION \
		current_column = yycolumn; \
		current_lineno = yylineno; \
		yycolumn += yyget_leng(yyscanner);
%}

%option 8bit reentrant never-interactive
%option ecs yylineno
%option noyywrap
%option warn nodefault

%x COMMENT
%x SINGLE_QUOTED
%x DOUBLE_QUOTED

IDSTART            [A-Za-z_$]
IDCONTINUED        {IDSTART}|[0-9]
IDENTIFIER         {IDSTART}{IDCONTINUED}*
OPERATOR           [-!#$%^&*_+=:<>/?|\\.,]+

%%

<INITIAL>{
	"/*"                       { BEGIN(COMMENT); }
	[//[^\n]*]                 {}

	";"                        { return SEMICOLON; }
	"."                        { return DOT; }
	","                        { return COMMA; }
	"("                        { return OPEN_PARENTHESIS; }
	")"                        { return CLOSE_PARENTHESIS; }
	"<"                        { return LEFT_ANGLE; }
	">"                        { return RIGHT_ANGLE; }

	":="                       { return ASSIGN; }
	"var"                      { return VAR; }
	"if"                       { return IF; }
	"else"                     { return ELSE; }
	"while"                    { return WHILE; }
	"true"                     { return TRUE; }
	"false"                    { return FALSE; }
	"do"                       { return DO; }

	{OPERATOR}+                { return OPERATOR; }
	[0-9]+                     { return INTEGER; }
	0x[0-9a-fA-F]+           { return INTEGER; }
	[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)? { return REAL; }
	[Nn][Aa][Nn]               { return REAL; }
	[-+][Ii][Nn][Ff]           { return REAL; }
	{IDSTART}{IDCONTINUED}*    { return IDENTIFIER; }

	[ \t\r]                    {}
}
<COMMENT>{
	"*/"                       { BEGIN(INITIAL); }
	[^*\n]*                    {}
	"*"                        {}
}

\n                             {}
.                              { printf("Bad char <%s>!\n", yytext); }

%%

