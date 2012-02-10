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

#ifndef COWBEL_ARRAY
#define COWBEL_ARRAY

type Array<T> =
{
	function get(i: integer): T;
	function set(i: integer, value: T);
	function length(): integer;
};

function Array<T>(size: integer, initialiser: T): Array<T>
{
	var ptr: __extern = 0;
	extern "${ptr} = calloc(${size}, sizeof(${initialiser}));";
	
	return
	{
		implements Array<T>;
		
		function get(i: integer): (result: T)
		{
			extern "${result} = ((typeof(${result})*)${ptr})[${i}];";
		}
		
		function set(i: integer, value: T)
		{
			extern "((typeof(${value})*)${ptr})[${i}] = ${value};";
		}
		
		function length(): integer
		{
			return size;
		}
	};
}

#endif
