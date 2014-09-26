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

var Array =
{
	/** Creates a 0-based one-dimensional array.
	 **
	 ** All entries in the array are initialised to the same, specified, value.
	 **/
	 
	function New<T>(size: int, initialiser: T): Array<T>
	{
		var ptr = __extern;
		extern "${ptr} = calloc(${size}, sizeof(${initialiser}));";
		
		return
		{
			implements Array<T>;
	
			function _boundscheck(i: int)
			{
				if (i < 0)
					Application.AbortOutOfBounds();
				else if (i >= size)
					Application.AbortOutOfBounds(); 
			}
					
			function get(i: int): (result: T)
			{
				_boundscheck(i);
				result = initialiser;
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
	
	/** Creates a 0-based two-dimensional array.
	 **
	 ** All entries in the array are initialised to the same, specified, value.
	 **/
	 
	function New2D<T>(width: int, height: int, initialiser: T): Array2D<T>
	{
		var ptr: __extern = extern(__extern);
		extern "${ptr} = calloc(${width}*${height}, sizeof(${initialiser}));";
		
		return
		{
			implements Array2D<T>;
	
			function _boundscheck(x: int, y: int)
			{
				if (x < 0)
					Application.AbortOutOfBounds();
				if (x >= width)
					Application.AbortOutOfBounds();
				if (y < 0)
					Application.AbortOutOfBounds();
				if (y >= height)
					Application.AbortOutOfBounds();
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
	
	/** Sorts a 1D array.
	 **/
	 
	function Sort<T>(array: Array<T>)
	{
		function swap(a: int, b: int)
		{
			var t = array.get(a);
			array.set(a, array.get(b));
			array.set(b, t);
		}
		
		function sortlet(left: int, right: int)
		{
		    if (left < right)
		    {
				var pivot = array.get((left + right) / 2);
				var ln = left;
				var rn = right;
 
				do
				{
					while (array.get(ln) < pivot)
						ln = ln + 1;
					while (pivot < array.get(rn))
						rn = rn - 1;
						
					if (ln <= rn)
					{
						swap(ln, rn);
						ln = ln + 1;
						rn = rn - 1;
					}
				}
				while (ln <= rn);

				sortlet(left, rn);
				sortlet(ln, right);
			}
		}
		
		var lo, hi = array.bounds();
		sortlet(lo, hi-1); 
	}
};

#endif
