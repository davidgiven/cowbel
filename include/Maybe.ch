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

#ifndef COWBEL_MAYBE
#define COWBEL_MAYBE

#include "Application.ch"

/** Represents a container that may or may not contain a single value.
 **
 ** These objects perform the same task as null pointers in other language.
 ** A Maybe can either point at an object of some type; or else it can point
 ** at nothing. They are useful for constructing data structures such as
 ** lists or trees.
 **/
 
type Maybe<T> =
{
	/** Returns whether this Maybe contains a value.
	 **/
	 
	function valid(): boolean;
	
	/** Returns the value this Maybe is pointing at. If no value is present,
	 ** produces a run-time error.
	 **/
	 
	function get(): T;
};

/** Constructs an immutable empty Maybe.
 **/
 
function Maybe<T>(): Maybe<T>
	return
	{
		implements Maybe<T>;
		
		function valid(): boolean
			return false;
			
		function get(): T
			AbortInvalidObjectState();
	};

/** Construct an immutable Maybe with the specified value.
 **/
 
function Maybe<T>(value: T): Maybe<T>
	return
	{
		implements Maybe<T>;
		
		function valid(): boolean
			return true;
			
		function get(): T
			return value;
	};

#endif
