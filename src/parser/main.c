/* © 2014 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

#include "globals.h"
#include "grammar.h"
#include "lexer.h"

extern void* ParseAlloc(void* (*allocProc)(size_t));
extern void Parse(void*, int, token_t);
extern void* ParseFree(void*, void(*freeProc)(void*));
extern void ParseTrace(FILE *TraceFILE, char *zTracePrompt);

json_t* current_filename;
int current_lineno;
int current_column;
bool syntax_error;
json_t* parsed_string;

json_t* simple_token(token_t* token, const char* kind)
{
	json_t* t = json_object();
	json_object_set(t, "type", json_string(kind));
	json_object_set(t, "filename", token->filename);
	json_object_set(t, "lineno", json_integer(token->lineno));
	json_object_set(t, "column", json_integer(token->column));
	return t;
}

json_t* composite_token(json_t* proto, const char* kind)
{
	json_t* t = json_object();
	json_object_set(t, "type", json_string(kind));
	json_object_set(t, "filename", json_object_get(proto, "filename"));
	json_object_set(t, "lineno", json_object_get(proto, "lineno"));
	json_object_set(t, "column", json_object_get(proto, "column"));
	return t;
}

json_t* json_array_single(json_t* item)
{
	json_t* t = json_array();
	json_array_append(t, item);
	return t;
}

int main(int argc, const char* argv[])
{
	yyscan_t scanner;
	yylex_init(&scanner);
	yyset_in(stdin, scanner);
	yyset_debug(1, scanner);

	current_filename = json_string("<stdin>");
	current_lineno = 1;
	current_column = 1;
	syntax_error = false;

	void* parser = ParseAlloc(malloc);
	//ParseTrace(stdout, ":");

	int token;
	token_t tokeninfo;
	for (;;)
	{
        token = yylex(scanner);
		if (token == 0)
			break;

		tokeninfo.filename = current_filename;
		tokeninfo.lineno = current_lineno;
		tokeninfo.column = current_column;
		if (token == STRING)
			tokeninfo.value = parsed_string;
		else
			tokeninfo.value = json_string(yyget_text(scanner));

        Parse(parser, token, tokeninfo);
    }
	Parse(parser, 0, tokeninfo);

	ParseFree(parser, free);
	yylex_destroy(scanner);

	return syntax_error ? 1 : 0;
}

