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

#ifndef COWBEL_BUFFER
#define COWBEL_BUFFER

#include <Stdlib.ch>
#include <Array.ch>

/** Represents a mutable, resizeable, one-dimension array of bytes (in the
 ** range [0, 255]).
 **/
 
type Buffer =
{
	/** Returns the value at a particular index.
	 **/
	 
	function get(i: int): int;
	
	/** Sets the value at a particular index.
	 **/
	 
	function set(i: int, value: int);
	
	/** Returns the bounds of the array as [low, high); low is always zero.
	 **/
	 
	function bounds(): (low: int, high: int);
	
	/** Resizes the buffer to a particular length. New bytes are initialised
	 ** to 0. */
	 
	function resize(length: int);
	
	/** Append a byte to the buffer, resizing it if necessary. */
	
	function append(byte: int);
	
	/** Return a copy of this buffer as a UTF-8 string. */
	
	function toString(): string;
};

var Buffer =
{
	/** Creates a 0-based one-dimensional array of bytes.
	 **
	 ** All entries in the array are initialised to 0.
	 **/
	 
	function New(size: int): Buffer
	{
		var capacity = size;
		var ptr: __extern = extern(__extern);
		extern "${ptr} = S_ALLOC_DATA(${size});";
		
		return
		{
			implements Array<int>;
			implements Buffer;
	
			function _boundscheck(i: int)
			{
				if (i < 0)
					Application.AbortOutOfBounds();
				else if (i >= size)
					Application.AbortOutOfBounds(); 
			}
					
			function get(i: int): (result: int)
			{
				_boundscheck(i);
				result = extern(int);
				extern "${result} = ((uint8_t*)${ptr})[${i}];";
			}
			
			function set(i: int, value: int)
			{
				_boundscheck(i);
				extern "((uint8_t*)${ptr})[${i}] = ${value};";
			}
			
			function bounds(): (low: int, high: int)
			{
				low = 0;
				high = size;
			}
			
			function resize(newsize: int)
			{
				if (newsize <= capacity)
				{
					size = newsize;
					return;
				}
	
				var newcapacity = capacity + 1;
				while (newcapacity < newsize)
					newcapacity = newcapacity * 2;
				
				var newptr: __extern = extern(__extern);
				extern '${newptr} = S_REALLOC_DATA(${ptr}, ${newcapacity});';
				if (newptr.isNull())
					Application.AbortOutOfMemory();
				
				var delta = newcapacity - capacity;
				extern 'memset(${newptr} + ${capacity}, 0, ${delta});';
				
				ptr = newptr;
				capacity = newcapacity;
				size = newsize;
			}
			
			function append(value: int)
			{
				var o = size;
				resize(size+1);
				set(o, value);
			}
			
			function toString(): (result: string)
			{
				result = extern(string);
				extern '${result} = s_create_string_val(${ptr}, ${size});';
			}
		};
	}
	
	/** Creates an immutable Buffer pointing at the UTF-8 bytes of a string. */
	
	function NewFromString(value: string): Buffer
	{
		var size = extern(int);
		var ptr = extern(__extern);
		extern '${size} = ${value}->totallength;';
		extern '${ptr} = (void*) s_string_cdata(${value});';
		
		return
		{
			implements Array<int>;
			implements Buffer;
	
			function _boundscheck(i: int)
			{
				if (i < 0)
					Application.AbortOutOfBounds();
				else if (i >= size)
					Application.AbortOutOfBounds(); 
			}
					
			function get(i: int): (result: int)
			{
				_boundscheck(i);
				result = extern(int);
				extern "${result} = ((uint8_t*)${ptr})[${i}];";
			}
			
			function set(i: int, value: int)
			{
				Application.AbortOperationNotSupported();
			}
			
			function bounds(): (low: int, high: int)
			{
				low = 0;
				high = size;
			}
			
			function resize(newsize: int)
			{
				Application.AbortOperationNotSupported();
			}
			
			function append(value: int)
			{
				Application.AbortOperationNotSupported();
			}
			
			function toString(): (result: string)
			{
				return value;
			}
		};
	}
};

#endif
