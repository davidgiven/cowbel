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

#ifndef COWBEL_SET
#define COWBEL_SET

#include <Stdlib.ch>
#include <Map.ch>

/** Represents a simple set.
 **/
 
type Set<V> =
{
	/** Inserts an item into the set. If the item already exists, nothing
	 ** happens.
	 **/
	 
	function add(value: V);
	
	/** Determines whether the item is in the set.
	 **/
	 
	function contains(value: V): boolean;
	
	/** Removes an item from the set.
	 **/
	 
	function remove(value: V);
};

var Set =
{
	/** Creates a new set.
	 ** 
	 ** Values must be comparable with == and <.
	 **/
	 
	function New<V>(): Set<V>
	{
		var map = Map.New<V, boolean>();
		
		return
		{
			implements Set<V>;
			
			function add(value: V)
			{
				map.put(value, true);
			}
			
			function contains(value: V): boolean
			{
				return map.contains(value);
			}
			
			function remove(value: V)
			{
				map.remove(value);
			}
		};
	}
};

#endif
