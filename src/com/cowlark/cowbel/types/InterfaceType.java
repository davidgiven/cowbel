/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.MethodTemplate;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.InterfaceTypeNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.errors.NoSuchMethodException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.IsMethod;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.methods.VirtualMethod;

public class InterfaceType extends Type implements HasInterfaces
{
	public static InterfaceType create(InterfaceTypeNode block)
	{
		return new InterfaceType(block);
	}
	
	private InterfaceTypeNode _node;
	private TreeSet<MethodTemplate> _methodTemplates =
		new TreeSet<MethodTemplate>();
	private TreeMap<String, VirtualMethod> _methods =
		new TreeMap<String, VirtualMethod>();
	private TreeSet<AbstractScopeConstructorNode> _implementations =
		new TreeSet<AbstractScopeConstructorNode>();
	
	private InterfaceType(InterfaceTypeNode node)
    {
		_node = node;
    }
	
	public InterfaceTypeNode getNode()
    {
	    return _node;
    }
	
	public Collection<VirtualMethod> getMethods()
    {
	    return Collections.unmodifiableCollection(_methods.values());
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "interface" + getId();
	}
	
	@Override
	public Collection<InterfaceType> getInterfaces()
	{
	    return Collections.singleton(this);
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type src)
	        throws CompilationException
	{
		if (src instanceof HasInterfaces)
		{
			HasInterfaces hi = (HasInterfaces) src;
			
			for (InterfaceType i : hi.getInterfaces())
			{
				if (i == this)
					return;
			}
		}
		
		throw new TypesNotCompatibleException(node, this, src);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(node.getIdentifier().getText());
		sb.append("<");
		sb.append(node.getTypeArguments().getNumberOfChildren());
		sb.append(">(");
		sb.append(node.getInputs().getNumberOfChildren());
		sb.append(")");
		String signature = sb.toString();

		for (MethodTemplate mt : _methodTemplates)
		{
			if (mt.getSignature().equals(signature))
				return mt.instantiate(node, node.getTypeArguments());
		}
		
		throw new NoSuchMethodException(node, this, id);
	}

	public void addMethodTemplate(MethodTemplate template)
			throws CompilationException
	{
		String signature = template.getSignature();
		for (MethodTemplate ft : _methodTemplates)
		{
			if (ft.getSignature().equals(signature))
				throw new MultipleDefinitionException(ft, template);
		}
		
		_methodTemplates.add(template);
	}
	
	public VirtualMethod lookupVirtualMethod(String signature)
	{
		return _methods.get(signature);
	}
	
	public void addVirtualMethod(String signature, VirtualMethod method)
	{
		_methods.put(signature, method);
	}

	public void addImplementation(AbstractScopeConstructorNode implementation)
	{
		_implementations.add(implementation);
	}
	
	public void instantiateMethods() throws CompilationException
	{
		for (AbstractScopeConstructorNode implementation : _implementations)
			for (VirtualMethod m : _methods.values())
				implementation.lookupMethod(getNode(), m);
	}
	
	public void dumpDetails()
	{
		for (AbstractScopeConstructorNode i : _implementations)
			System.out.println(i.toString());
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
