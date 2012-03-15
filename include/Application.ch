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

#ifndef COWBEL_APPLICATION
#define COWBEL_APPLICATION

#include <Stdlib.ch>
#include <Array.ch>

/** Returns an immutable array of command line arguments.
 **/

function ApplicationArguments(): Array<string>
	return
	{
		implements Array<string>;
		
		function get(i: int): string
		{
			var argc = extern(int);
			extern "${argc} = s_argc;";
			if (i >= argc)
				AbortOutOfBounds();
			
			var result = extern(string);
			extern "${result} = s_argv[${i}];";
			return result;
		}
		
		function set(i: int, value: string)
			AbortOperationNotSupported();
		
		function bounds(): (low: int, high: int)
		{
			low = 0;
			high = extern(int);
			extern "${high} = s_argc;";
		}
	};

/** Stops execution.
 **/
 
function Exit(result: int)
{
	extern 'exit(${result});';
}

/** Stops execution with a runtime error.
 **/
	
function Abort(message: string)
{
	extern 'fprintf(stderr, "Runtime error: %s\\n", s_string_cdata(${message}));';
	extern 'exit(1);'; 
}

/** Stops execution with an 'Out of bounds access' error.
 **/
 
function AbortOutOfBounds()
 	Abort("out of bounds access");
 	
/** Stops execution with an 'Operation not supported' error.
 **/
 
function AbortOperationNotSupported()
	Abort("operation not supported");
	
/** Stops execution with an 'Invalid object state' error.
 **/
 
function AbortInvalidObjectState()
	Abort("invalid object state");
	
/** Stops execution with an 'Out of memory' error.
 **/
 
function AbortOutOfMemory()
	Abort("out of memory");
	
#endif
