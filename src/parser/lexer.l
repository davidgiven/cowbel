%{
	/* © 2014 David Given
	 * This file is made available under the terms of the two-clause BSD
	 * license. See the file COPYING.BSD in the distribution directory for the
	 * full license text.
	 */

	#include "globals.h"
	#include <unistr.h>
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
%x LINESPEC
%x ENDOFLINESPEC

IDSTART            [A-Za-z_$]
IDCONTINUED        {IDSTART}|[0-9]
IDENTIFIER         {IDSTART}{IDCONTINUED}*
OPERATOR           [-!#$%^&*_+=:<>/?|\\.,~]+

%%

<INITIAL>{
	"/*"                       { BEGIN(COMMENT); }
	"//"[^\n]*$                {}

	^#                         { BEGIN(LINESPEC); }

	\"                         { BEGIN(DOUBLE_QUOTED); }
	\'                         { BEGIN(SINGLE_QUOTED); }

	";"                        { return SEMICOLON; }
	":"                        { return COLON; }
	"."                        { return DOT; }
	","                        { return COMMA; }
	"("                        { return OPEN_PARENTHESIS; }
	")"                        { return CLOSE_PARENTHESIS; }
	"{"                        { return OPEN_BRACE; }
	"}"                        { return CLOSE_BRACE; }

	"var"                      { return VAR; }
	"if"                       { return IF; }
	"else"                     { return ELSE; }
	"while"                    { return WHILE; }
	"true"                     { return TRUE; }
	"false"                    { return FALSE; }
	"do"                       { return DO; }
	"and"                      { return AND; }
	"or"                       { return OR; }
	"not"                      { return NOT; }
	"break"                    { return BREAK; }
	"continue"                 { return CONTINUE; }
	"return"                   { return RETURN; }
	"function"                 { return FUNCTION; }
	"overriding"               { return OVERRIDING; }
	"implements"               { return IMPLEMENTS; }
	"type"                     { return TYPE; }
	"extern"                   { return EXTERN; }

	/* =, < and > are special; if they occur *on their own* they're
	 * specific tokens. Otherwise they're just part of OPERATOR. */
	[=<>]{OPERATOR}+           { return OPERATOR; }
	"="                        { return ASSIGN; }
	"<"                        { return OPEN_ANGLE; }
	">"                        { return CLOSE_ANGLE; }
	{OPERATOR}+                { return OPERATOR; }

	[0-9]+                     { return INTEGER; }
	0x[0-9a-fA-F]+           { return INTEGER; }
	[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)? { return REAL; }
	[Nn][Aa][Nn]               { return REAL; }
	[-+][Ii][Nn][Ff]           { return REAL; }
	{IDSTART}{IDCONTINUED}*    { return IDENTIFIER; }

	[ \t\r\n]                  {}
}
<COMMENT>{
	"*/"                       { BEGIN(INITIAL); }
	[^*\n]*                    {}
	"*"                        {}
	"\n"                       {}
}

<SINGLE_QUOTED>{
	"'"                        { parsed_string = json_string("");
                                 BEGIN(INITIAL); return STRING; }
	[^\\'\n]+                  { parsed_string = json_string(yytext); return STRING; }
}

<DOUBLE_QUOTED>{
	"\""                       { parsed_string = json_string("");
                                 BEGIN(INITIAL); return STRING; }
	[^\\"\n]+                  { parsed_string = json_string(yytext); return STRING; }
}

<SINGLE_QUOTED,DOUBLE_QUOTED>{
	\\'                        { parsed_string = json_string("'"); return STRING; }
	\\\"                       { parsed_string = json_string("\""); return STRING; }
	\\n                        { parsed_string = json_string("\n"); return STRING; }
	\\t                        { parsed_string = json_string("\r"); return STRING; }
	\\u[0-9a-fA-F]+            { uint8_t s[8];
	                             uint32_t c = strtol(yytext+2, NULL, 16);
								 size_t size;
								 u32_to_u8(&c, 1, s, &size);
								 s[size] = '\0';
								 parsed_string = json_string(s); return STRING; }
	\\.                        { printf("Bad string escape <%s>!\n", yytext); }
}

<LINESPEC>{
	[0-9]+                     { yylineno = strtol(yytext, NULL, 0)-1; }
	\"[^\"]*                   { current_filename = json_string(yytext+1);
                                 BEGIN(ENDOFLINESPEC);
	                           }

	[ \t]*                     {}
}

<ENDOFLINESPEC>{
	.*                         {}
	"\n"                       { BEGIN(INITIAL); }
}

.                              { printf("Bad char <%s>!\n", yytext); }

%%

