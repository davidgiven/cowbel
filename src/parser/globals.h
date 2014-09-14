/* © 2014 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

#ifndef GLOBALS_H
#define GLOBALS_H

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
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
extern bool syntax_error;

extern json_t* simple_token(token_t* token, const char* kind);
extern json_t* composite_token(json_t* proto, const char* kind);

extern json_t* json_array_single(json_t* item);

#endif

