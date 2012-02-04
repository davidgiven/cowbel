/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Map;
import java.util.TreeMap;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.errors.TypeNotFound;
import com.cowlark.cowbel.types.Type;

public class TypeContext implements Comparable<TypeContext>
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private Node _node;
	private TypeContext _parent;
	private TreeMap<IdentifierNode, Type> _types =
		new TreeMap<IdentifierNode, Type>(IdentifierNode.valueComparator);
	
	public TypeContext(Node node, TypeContext parent)
	{
		_node = node;
		_parent = parent;
	}
	
	public Node getNode()
    {
	    return _node;
    }
	
	public TypeContext getParent()
    {
	    return _parent;
    }
	
	@Override
	public String toString()
	{
		return "TypeContext" + _id;
	}
	
	@Override
    public int compareTo(TypeContext other)
	{
		if (_id < other._id)
			return -1;
		if (_id == other._id)
			return 0;
		return 1;
	}
	
	private void add_assignments_to_map(Map<String, Type> map)
	{
		if (_parent != null)
			_parent.add_assignments_to_map(map);
		
		for (Map.Entry<IdentifierNode, Type> e : _types.entrySet())
			map.put(e.getKey().getText(), e.getValue());
	}
	
	public Map<String, Type> getTypeAssignments()
	{
		Map<String, Type> map = new TreeMap<String, Type>();
		add_assignments_to_map(map);
		return map;
	}
	
	public String getSignature()
	{
		StringBuilder sb = new StringBuilder();
		
		boolean first = true;
		for (Map.Entry<String, Type> e : getTypeAssignments().entrySet())
		{
			if (!first)
				sb.append(' ');
			first = false;
			
			sb.append(e.getKey());
			sb.append("=");
			sb.append(e.getValue().getCanonicalTypeName());
		}
		
		return sb.toString();
	}
	
	public void addType(IdentifierNode identifier, Type type)
		throws CompilationException
	{
		if (_types.containsKey(identifier))
		{
			for (Map.Entry<IdentifierNode, Type> e : _types.entrySet())
			{
				if (IdentifierNode.valueComparator.compare(e.getKey(), identifier) == 0)
					throw new MultipleDefinitionException(e.getKey(), identifier);
			}
		}
			
		_types.put(identifier, type);
	}
	
	public Type lookupType(IdentifierNode identifier) throws CompilationException
	{
		TypeContext tc = this;
		
		do
		{
			Type t = tc._types.get(identifier);
			if (t != null)
				return t;

			tc = tc._parent;
		}
		while (tc != null);
		
		throw new TypeNotFound(this, identifier);
	}
}
