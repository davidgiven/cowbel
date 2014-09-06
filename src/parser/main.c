#include <stdlib.h>
#include <stdio.h>
#include <gc/gc.h>
#include <gc/cord.h>
#include "grammar.h"
#include "lexer.h"

extern void* ParseAlloc(void* (*allocProc)(size_t));
extern void* Parse(void*, int, const char*);
extern void* ParseFree(void*, void(*freeProc)(void*));

int main(int argc, const char* argv[])
{
	GC_INIT();

	yyscan_t scanner;
	yylex_init(&scanner);
	yyset_in(stdin, scanner);

	void* parser = ParseAlloc(GC_malloc);

	ParseFree(parser, GC_free);
	yylex_destroy(scanner);

	return 0;
}

