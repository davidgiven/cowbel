/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.AbstractTypeExpressionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.interfaces.HasMethods;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.IsCallNode;
import com.cowlark.cowbel.utils.DeterministicObject;

public class Interface extends DeterministicObject<Interface> 
		implements HasNode, HasMethods
{
	private static TreeSet<Interface> _allInterfaces =
		new TreeSet<Interface>();
	
	public static Set<Interface> getAllInterfaces()
	{
		return _allInterfaces;
	}
	
	private AbstractTypeExpressionNode _node;
	private InterfaceContext _context;
	private String _nameHint;
	private TreeMap<FunctionTemplateSignature, MethodTemplate> _templates =
		new TreeMap<FunctionTemplateSignature, MethodTemplate>();
	private TreeMap<FunctionSignature, Method> _methods =
		new TreeMap<FunctionSignature, Method>();
	private TreeSet<Implementation> _implementations =
		new TreeSet<Implementation>();
	
	public Interface(InterfaceContext context, AbstractTypeExpressionNode node)
	{
		_context = context;
		_node = node;
		
		_allInterfaces.add(this);
	}
	
	@Override
    public AbstractTypeExpressionNode getNode()
	{
		return _node;
	}
	
	public String getNameHint()
    {
		if (_nameHint == null)
			return toString();
	    return _nameHint;
    }
	
	public void setNameHint(String nameHint)
    {
	    _nameHint = nameHint;
    }
	
	public void addImplementation(Implementation implementation)
	{
		_implementations.add(implementation);
	}
	
	public Collection<Implementation> getImplementations()
	{
		return _implementations;
	}
	
	public Collection<Method> getMethods()
    {
	    return _methods.values();
    }
	
	public void addMethodTemplate(MethodTemplate template)
			throws CompilationException
	{
		FunctionTemplateSignature signature = new FunctionTemplateSignature(template.getNode());
		if (_templates.containsKey(signature))
			throw new MultipleDefinitionException(_templates.get(signature), template);
		
		_templates.put(signature, template);
	}
	
	/** Attempts to find an already instantiated function on this interface.
	 * Returns null if one does not exist. */
	
    public Callable getMethod(FunctionTemplateSignature signature, IsCallNode node)
    		throws CompilationException
    {
		MethodTemplate template = _templates.get(signature);
		if (template == null)
			return null;

		FunctionSignature methodSignature = new FunctionSignature(signature,
				template, node);
		Method method = _methods.get(methodSignature);
		if (method == null)
			return null;
		
		return method;
    }
    
    /** Attempts to find an already instantiated function on this interface.
     * Instantiates it if it does not exist. */
    
	@Override
	public Callable lookupMethod(FunctionTemplateSignature signature,
	        IsCallNode node)
			throws CompilationException
	{
		MethodTemplate template = _templates.get(signature);
		if (template == null)
			return null;

		FunctionSignature methodSignature = new FunctionSignature(signature,
				template, node);
		Method method = _methods.get(methodSignature);
		if (method == null)
		{
			method = template.instantiate(methodSignature, node,
					methodSignature.getTypeAssignments());
			_methods.put(methodSignature, method);
		}
		
		return method;
	};
	
	public void instantiateMethods()
			throws CompilationException
	{
		for (Method fs : _methods.values())
		{
			FunctionHeaderNode fhn = fs.getNode();
			FunctionTemplateSignature fts = new FunctionTemplateSignature(fhn);
			
			for (AbstractImplementation impl : _implementations)
			{
				Callable c = impl.lookupMethod(fts, fs.getRepresentativeMethodCall());
				c.wireTypesToHeader(fhn);
			}
		}
	}
}
