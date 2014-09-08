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
OPERATOR           [-!#$%^&*_+=:<>/?|\\]+

%%

<INITIAL>{
	"/*"                       { BEGIN(COMMENT); }
	[//[^\n]*]                 {}

	";"                        { return SEMICOLON; }
	"."                        { return DOT; }
	"("                        { return OPEN_PARENTHESIS; }
	")"                        { return CLOSE_PARENTHESIS; }

	{OPERATOR}+                { return OPERATOR; }
	[0-9]+                     { return INTEGER; }
	0x[0-9a-fA-F]+           { return INTEGER; }
	[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)? { return REAL; }
	[Nn][Aa][Nn]               { return REAL; }
	[-+][Ii][Nn][Ff]           { return REAL; }
	{IDSTART}{IDCONTINUED}*    { return IDENTIFIER; }


	[ \t\r]                    {}
	\n                         {}
}
<COMMENT>{
	"*/"                       { BEGIN(INITIAL); }
	[^*\n]*                    {}
	"*"                        {}
	\n                         {}
}

.                              { printf("Bad char <%s>!\n", yytext); }

%%

