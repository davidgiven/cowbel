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

#include <Stdlib.ch>
#include <Application.ch>

/** Represents a mutable one-dimensional array.
 **/
 
type Array<T> =
{
	/** Returns the value at a particular index.
	 **/
	 
	function get(i: int): T;
	
	/** Sets the value at a particular index.
	 **/
	 
	function set(i: int, value: T);
	
	/** Returns the bounds of the array as [low, high).
	 **/
	 
	function bounds(): (low: int, high: int);
};

/** Creates a 0-based one-dimensional array.
 **
 ** All entries in the array are initialised to the same, specified, value.
 **/
 
function Array<T>(size: int, initialiser: T): Array<T>
{
	var ptr: __extern = extern(__extern);
	extern "${ptr} = calloc(${size}, sizeof(${initialiser}));";
	
	return
	{
		implements Array<T>;

		function _boundscheck(i: int)
		{
			if (i < 0)
				AbortOutOfBounds();
			else if (i >= size)
				AbortOutOfBounds(); 
		}
				
		function get(i: int): (result: T)
		{
			_boundscheck(i);
			result = extern(initialiser);
			extern "${result} = ((typeof(${result})*)${ptr})[${i}];";
		}
		
		function set(i: int, value: T)
		{
			_boundscheck(i);
			extern "((typeof(${value})*)${ptr})[${i}] = ${value};";
		}
		
		function bounds(): (low: int, high: int)
		{
			low = 0;
			high = size;
		}
	};
}

/** Represents a mutable two-dimensional array.
 **/
 
type Array2D<T> =
{
	/** Returns the value at a particular (x, y) pair.
	 **/
	 
	function get(x: int, y: int): T;
	
	/** Sets the value at a particular (x, y) pair.
	 **/
	  
	function set(x: int, y: int, value: T);
	
	/** Returns the bounds of the array as two [low, high) pairs.
	 **/
	 
	function bounds(): (lowx: int, highx: int, lowy: int, highy: int);
};

/** Creates a 0-based two-dimensional array.
 **
 ** All entries in the array are initialised to the same, specified, value.
 **/
 
function Array2D<T>(width: int, height: int, initialiser: T): Array2D<T>
{
	var ptr: __extern = extern(__extern);
	extern "${ptr} = calloc(${width}*${height}, sizeof(${initialiser}));";
	
	return
	{
		implements Array2D<T>;

		function _boundscheck(x: int, y: int)
		{
			if (x < 0)
				AbortOutOfBounds();
			if (x >= width)
				AbortOutOfBounds();
			if (y < 0)
				AbortOutOfBounds();
			if (y >= height)
				AbortOutOfBounds();
		}
				
		function get(x: int, y: int): (result: T)
		{
			_boundscheck(x, y);
			result = extern(initialiser);
			extern "${result} = ((typeof(${result})*)${ptr})[${x} + ${y}*${width}];";
		}
		
		function set(x: int, y: int, value: T)
		{
			_boundscheck(x, y);
			extern "((typeof(${value})*)${ptr})[${x} + ${y}*${width}] = ${value};";
		}
		
		function bounds(): (lowx: int, highx: int, lowy: int, highy: int)
		{
			lowx = 0;
			highx = width;
			lowy = 0;
			highy = height;
		}
	};
}

#endif
