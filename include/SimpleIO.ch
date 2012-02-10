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

function print(s: string)
{
	extern 'S_METHOD_STRING_PRINT(${s});';
}

function printc(i: integer)
{
	extern 'putchar(${i});';
}

function printi(i: integer)
{
	extern 'printf("%d", ${i});';
}

function println(s: string)
{
	print(s);
	printc(10);
}

#endif
