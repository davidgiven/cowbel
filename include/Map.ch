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

#ifndef COWBEL_MAP
#define COWBEL_MAP

#include <Application.ch>
#include <Maybe.ch>

/** Represents a (key,value) storage structure.
 **/
 
type Map<K, V> =
{
	/** Inserts an item into the map. If the item already exists, its value
	 ** is replaced.
	 **/
	 
	function put(key: K, value: V);
	
	/** Retrieves an item from the map. If the item does not exist, a runtime
	 ** error occurs.
	 **/
	 
	function get(key: K): V;
	
	/** Determines whether the value exists in the map.
	 **/
	 
	function contains(key: K): boolean;
	
	/** Removes an item from the map.
	 **/
	 
	function remove(key: K);
};

/** Creates a new map based on an AA tree.
 ** 
 ** Keys must be comparable with == and <.
 **/
 
function Map<K, V>(): Map<K, V>
{
	/* This implementation was taken from here:
	 *
	 * http://www.eternallyconfuzzled.com/tuts/datastructures/jsw_tut_andersson.aspx
	 */
	 
	type Node =
	{
			function level(): int;
			function setLevel(level: int);
			function left(): Node;
			function setLeft(left: Node);
			function right(): Node;
			function setRight(right: Node);
			function key(): K;
			function setKey(key: K);
			function value(): V;
			function setValue(value: V);
	};

	var sentinel: Node =
	{
		implements Node;
		
		function level(): int
			return 0;
		function setLevel(level: int)
			AbortInvalidObjectState();
		function left(): Node
			return sentinel;
		function setLeft(left: Node)
			AbortInvalidObjectState();
		function right(): Node
			return sentinel;
		function setRight(right: Node)
			AbortInvalidObjectState();
		function key(): K
			AbortInvalidObjectState();
		function setKey(key: K)
			AbortInvalidObjectState();
		function value(): V
			AbortInvalidObjectState();
		function setValue(value: V)
			AbortInvalidObjectState();
	};
	
	function _makenode(_key: K, _value: V): Node
	{
		var _level = 1;
		var _left = sentinel;
		var _right = sentinel;
		
		return
		{
			implements Node;
			
			function level(): int
				return _level;
			function setLevel(level: int)
				_level = level;
			function left(): Node
				return _left;
			function setLeft(left: Node)
				_left = left;
			function right(): Node
				return _right;
			function setRight(right: Node)
				_right = right;
			function key(): K
				return _key;
			function setKey(key: K)
				_key = key;
			function value(): V
				return _value;
			function setValue(value: V)
				_value = value;
		};
	}
	 	
	function _skew(root: Node): Node
	{
		var rootlevel = root.level();
		if (rootlevel != 0)
		{
			if (root.left().level() == rootlevel)
			{
				var t = root;
				root = root.left();
				t.setLeft(root.right());
				root.setRight(t);
			}
			
			root.setRight(_skew(root.right()));
		}
		
		return root;
	}
	
	function _split(root: Node): Node
	{
		if (root.level() != 0)
		{
			if (root.right().right().level() == root.level())
			{
				var t = root;
				root = root.right();
				t.setRight(root.left());
				root.setLeft(t);
				root.setLevel(root.level() + 1);
				root.setRight(_split(root.right()));
			}
		}
		
		return root;
	}
	
	function _insert(root: Node, key: K, value: V): Node
	{
		if (root.level() == 0)
			root = _makenode(key, value);
		else
		{
			if (root.key() == key)
				root.setValue(value);
			else if (root.key() < key)
				root.setLeft(_insert(root.left(), key, value));
			else
				root.setRight(_insert(root.right(), key, value));
			
			root = _skew(root);
			root = _split(root);
		}
		
		return root;
	}
	
	function _remove(root: Node, key: K): Node
	{
		if (root.level() != 0)
		{
			if (root.key() == key)
			{
				if (root.left().level() != 0)
				{
					if (root.right().level() != 0)
					{
						var heir = root.left();
						
						while (heir.right().level() != 0)
							heir = heir.right();
							
						root.setKey(heir.key());
						root.setValue(heir.value());
						root.setLeft(_remove(root.left(), key));
					}
				}
				else
				{
					if (root.left().level() != 0)
						root = root.left();
					else
						root = root.right();
				}
			}
			else
			{
				if (root.key() < key)
					root.setLeft(_remove(root.left(), key));
				else
					root.setRight(_remove(root.right(), key));
			}
		}
		
		if ((root.left().level() < (root.level()-1)) |
		    (root.right().level() < (root.level()-1)))
		{
			root.setLevel(root.level() - 1);
			if (root.right().level() > root.level())
				root.right().setLevel(root.level());
			
			root = _skew(root);
			root = _split(root);
		}
		
		return root;
	}
	
	function _get(root: Node, key: K): V
	{
		if (root.level() == 0)
			Abort("item not in map");
			
		if (root.key() == key)
			return root.value();
		if (root.key() < key)
			return _get(root.left(), key);
		else
			return _get(root.right(), key);
	}
	
	function _contains(root: Node, key: K): boolean
	{
		if (root.level() == 0)
			return false;
			
		if (root.key() == key)
			return true;
		if (root.key() < key)
			return _contains(root.left(), key);
		else
			return _contains(root.right(), key);
	}
	
	var root = sentinel;
	
	return
	{
		implements Map<K, V>;
		
		function put(key: K, value: V)
			root = _insert(root, key, value);
		
		function get(key: K): V
			return _get(root, key);
			
		function contains(key: K): boolean
			return _contains(root, key);
			
		function remove(key: K)
			root = _remove(root, key);
	};
}

#endif
