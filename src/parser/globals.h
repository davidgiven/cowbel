#ifndef GLOBALS_H
#define GLOBALS_H

#include <stdlib.h>
#include <stdio.h>
#include <jansson.h>

typedef struct token {
	json_t* filename;
	int lineno;
	int column;
	json_t* value;
} token_t;

extern json_t* current_filename;
extern int current_lineno;
extern int current_column;

extern json_t* simple_token(token_t* token, const char* kind);

#endif

