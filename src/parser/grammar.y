%include {
	#include <stdlib.h>
	#include <stdio.h>
	#include <assert.h>
	#include <gc/cord.h>
}

%token_type {CORD}

%syntax_error
{
	fprintf(stderr, "Parse error!\n");
}

start ::= .

