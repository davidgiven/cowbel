/* cowbel test suite
 *
 * Written in 2012 by David Given.
 *
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 *
 * Please see the file COPYING.CC0 in the distribution package for more
 * information.
 */

#ifndef COWBEL_SIMPLEIO
#define COWBEL_SIMPLEIO

#include <Stdlib.ch>

/** Prints a string to stdout.
 **/
 
function print(s: string)
{
	extern 'S_METHOD_STRING_PRINT(${s});';
}

/** Prints a newline-terminated string to stdout.
 **/
 
function println(s: string)
{
	print(s);
	printc(10);
}

/** Prints a single character to stdout.
 **/
 
function printc(i: int)
{
	extern 'putchar(${i});';
}

/** Prints a decimal integer to stdout.
 **/
 
function printi(i: int)
{
	extern 'printf("%d", ${i});';
}

/** Prints a real to stdout.
 **/
 
function printr(r: real)
{
	extern 'printf("%f", ${r});';
}

#endif
