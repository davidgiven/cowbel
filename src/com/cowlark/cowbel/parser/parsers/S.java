/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

public class S
{
	static char[] SingleCharOperators = new char[]
    {
    	'.',
    	',',
    	':',
    	';',
    	'(',
    	')',
    	'{',
    	'}',
    	'[',
    	']',
    	'<',
    	'>',
    	'=',
    };
	
	static String[] Keywords = new String[]
    {
		"true",
		"false",
    	"var",
    	"function",
    	"return",
    	"if",
    	"else",
    	"goto",
    	"string",
    	"integer",
    	"boolean",
    	"break",
    	"continue",
    	"while",
    	"do",
    	"for"
    };
}
