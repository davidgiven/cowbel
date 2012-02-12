/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import com.cowlark.cowbel.ast.AbstractTypeNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.errors.TypeNotFound;
import com.cowlark.cowbel.types.Type;

public class TypeContext implements Comparable<TypeContext>
{
	private static int _globalid = 0;
	
	private static class InstantiationData
	{
		Type type;
		IdentifierNode identifier;
		List<Type> typeassignments;
	};
	
	private int _id = _globalid++;
	private Node _node;
	private TypeContext _parent;
	private String _signature;
	private TreeMap<String, InstantiationData> _types =
		new TreeMap<String, InstantiationData>();
	private TreeMap<String, TypeTemplate> _typeTemplates =
		new TreeMap<String, TypeTemplate>();
	
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
	
	public String getSignature()
	{
		if (_signature == null)
		{
			TreeMap<String, Type> types = new TreeMap<String, Type>();
			
			TypeContext tc = this;
			while (tc != null)
			{
				for (InstantiationData id : _types.values())
				{
					String s = id.identifier.getText();
					if (!types.containsKey(s))
						types.put(s, id.type);
				}
				
				tc = tc.getParent();
			}
			
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Type> e : types.entrySet())
			{
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue().getCanonicalTypeName());
				sb.append(" ");
			}

			_signature = sb.toString();
		}
		
		return _signature;
	}
	
	public void addTypeTemplate(TypeAssignmentNode node)
		throws CompilationException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(node.getIdentifier().getText());
		sb.append('<');
		sb.append(node.getTypeVariables().getNumberOfChildren());
		sb.append('>');
		String signature = sb.toString();

		if (_typeTemplates.containsKey(signature))
		{
			for (Map.Entry<String, TypeTemplate> e : _typeTemplates.entrySet())
			{
				if (e.getKey().equals(signature))
				{
					if (e.getValue() != null)
						throw new MultipleDefinitionException(
								e.getValue().getNode(), node);
					else
						throw new MultipleDefinitionException(
								e.getValue().getNode(),	null);
				}
			}
		}
		
		_typeTemplates.put(signature, new TypeTemplate(this, node));
	}
	
	public void addType(IdentifierNode identifier, Type type)
		throws CompilationException
	{
		assert(_signature == null);
		
		/* Add an instantiation for this type. */
		
		String signature = identifier.getText() + "<>";
		
		InstantiationData id = _types.get(signature);
		if (id != null)
			throw new MultipleDefinitionException(id.identifier, identifier);
		
		id = new InstantiationData();
		id.identifier = identifier;
		id.type = type;
		id.typeassignments = Collections.emptyList();
		_types.put(signature, id);
		
		type.setNameHint(identifier.getText());
		
		/* Add a dummy template to prevent redefinition. */
		
		_typeTemplates.put(identifier.getText() + "<0>", null);
	}
	
	public Type lookupType(TypeVariableNode typevar) throws CompilationException
	{
		List<Type> types = new Vector<Type>();
		for (Node n : typevar.getTypeAssignments())
		{
			AbstractTypeNode atn = (AbstractTypeNode) n;
			Type type = atn.getType();
			types.add(type);
		}
		
		/* Calculate the type instantiation signature. */
		
		StringBuilder sb = new StringBuilder();
		sb.append(typevar.getIdentifier().getText());
		sb.append('<');
		
		boolean first = true;
		for (Type t : types)
		{
			if (!first)
				sb.append(", ");
			first = false;
			
			sb.append(t.getId());
		}
		sb.append('>');
		
		String instantiationsignature = sb.toString();
		
		/* Calculate the type template signature. */
		
		sb = new StringBuilder();
		sb.append(typevar.getIdentifier().getText());
		sb.append('<');
		sb.append(typevar.getTypeAssignments().getNumberOfChildren());
		sb.append('>');
		
		String templatesignature = sb.toString();
		
		/* Now search up the type context chain looking for either a type
		 * instantiation or a type template that matches. */
		
		TypeContext tc = this;
		
		do
		{
			InstantiationData id = tc._types.get(instantiationsignature);
			if (id != null)
				return id.type;
			
			TypeTemplate tt = tc._typeTemplates.get(templatesignature);
			if (tt != null)
			{
				id = new InstantiationData();
				id.identifier = tt.getNode().getIdentifier();
				id.typeassignments = types;
				id.type = tt.instantiate(typevar, types);
				tc._types.put(instantiationsignature, id);
				return id.type;
			}

			tc = tc._parent;
		}
		while (tc != null);
		
		throw new TypeNotFound(this, typevar);
	}
}
