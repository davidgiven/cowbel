/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.MethodTemplate;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.InterfaceTypeNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.methods.VirtualMethod;

public class InterfaceType extends Type
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
	
	private InterfaceType(InterfaceTypeNode node)
    {
		_node = node;
    }
	
	public InterfaceTypeNode getNode()
    {
	    return _node;
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "interface" + getId();
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
		throw new TypesNotCompatibleException(node, this, other);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		assert(false);
		throw null;
		//InterfaceType type = (InterfaceType) node.getMethodReceiver().getType().getRealType();
		//BlockScopeConstructorNode block = type.getBlock();
		//return block.lookupMethod(node);
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
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
