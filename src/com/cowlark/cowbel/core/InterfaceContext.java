/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Map;
import java.util.TreeMap;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InterfaceListNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.TypeVariableNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.errors.TypeNotFound;
import com.cowlark.cowbel.errors.TypeParameterMismatch;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.HasTypeVariables;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.utils.ById;
import com.cowlark.cowbel.utils.DeterministicObject;

public class InterfaceContext extends DeterministicObject<InterfaceContext>
{	
	private IsNode _node;
	private InterfaceContext _parent;
	private TreeMap<TypeSignature, Interface> _instantiatedTemplates =
		new TreeMap<TypeSignature, Interface>();
	private TreeMap<TypeTemplateSignature, InterfaceTemplate> _interfaceTemplates =
		new TreeMap<TypeTemplateSignature, InterfaceTemplate>();
	
	public InterfaceContext(IsNode node, InterfaceContext parent)
	{
		_node = node;
		_parent = parent;
	}
	
	public InterfaceContext(IsNode node, InterfaceContext parent,
			TypeAssignment ta)
				throws CompilationException
	{
		this(node, parent);
		
		for (Map.Entry<ById<IdentifierNode>, Interface> e : ta.entrySet())
		{
			TypeSignature signature = new TypeSignature(e.getKey().get());
			_instantiatedTemplates.put(signature, e.getValue());
		}
	}
	
	public InterfaceContext(IsNode node, InterfaceContext parent,
			HasTypeVariables hastypevars, HasTypeArguments hastypeargs)
				throws CompilationException
	{
		this(node, parent);
		
		IdentifierListNode typevars = hastypevars.getTypeVariables();
		InterfaceListNode typeargs = hastypeargs.getTypeArguments();

		if (typevars.getNumberOfChildren() != typeargs.getNumberOfChildren())
			throw new TypeParameterMismatch(node, parent._node, typevars, typeargs);

		for (int i = 0; i < typevars.getNumberOfChildren(); i++)
		{
			TypeSignature signature = new TypeSignature(typevars.getIdentifier(i));
			Interface type = typeargs.getType(i).getInterface();
			_instantiatedTemplates.put(signature, type);
		}
	}
	
	public IsNode getNode()
    {
	    return _node;
    }
	
	public InterfaceContext getParent()
    {
	    return _parent;
    }
	
	public void addTypeTemplate(TypeAssignmentNode node)
		throws CompilationException
	{
		TypeTemplateSignature signature = new TypeTemplateSignature(node);

		if (_interfaceTemplates.containsKey(signature))
		{
			for (Map.Entry<TypeTemplateSignature, InterfaceTemplate> e : _interfaceTemplates.entrySet())
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
		
		_interfaceTemplates.put(signature, new InterfaceTemplate(this, node));
	}
	
	public InterfaceTemplate lookupTemplate(TypeVariableNode typevar)
		throws CompilationException
	{
		TypeTemplateSignature signature = new TypeTemplateSignature(typevar);
		
		InterfaceContext context = this;
		do
		{
			InterfaceTemplate template = context._interfaceTemplates.get(signature);
			if (template != null)
				return template;
			context = context.getParent();
		}
		while (context != null);
		
		throw new TypeNotFound(this, typevar);
	}
	
	public Interface instantiateType(InterfaceTemplate template,
			Node node) throws CompilationException
	{
		assert(template.getInterfaceContext() == this);
		
		HasTypeArguments typeargs = null;
		if (node instanceof HasTypeArguments)
			typeargs = (HasTypeArguments) node;
		
		TypeSignature ts = new TypeSignature(template, typeargs);
		
		Interface type = _instantiatedTemplates.get(ts);
		if (type != null)
			return type;
		
		type = template.instantiate(node, ts.getTypeAssignments());
		_instantiatedTemplates.put(ts, type);
		return type;
	}
	
	public Interface lookupType(TypeVariableNode node)
				throws CompilationException
	{
		InterfaceTemplate template = lookupTemplate(node);
		return instantiateType(template, node);
	}
	
	public Interface lookupRawType(String name)
	{
		TypeSignature signature = new TypeSignature(name);
		return _instantiatedTemplates.get(signature);
	}
}
