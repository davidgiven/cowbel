/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.Collection;
import java.util.TreeSet;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.Method;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;
import com.cowlark.cowbel.interfaces.IsNode;

public class InterfaceConcreteType
		extends AbstractConcreteType
{
	public static TreeSet<InterfaceConcreteType> _allInterfaceTypes =
		new TreeSet<InterfaceConcreteType>();
	
	public static Collection<InterfaceConcreteType> getAllInterfaceTypes()
	{
		return _allInterfaceTypes;
	}

	private IsNode _node;
	private Collection<Interface> _interfaces;
	private TreeSet<Method> _methods =
		new TreeSet<Method>();
	private Collection<AbstractConcreteType> _parents;
	private TreeSet<InterfaceConcreteType> _downcasts =
		new TreeSet<InterfaceConcreteType>();
	
	public InterfaceConcreteType(IsNode node, Collection<Interface> interfaces,
			Collection<AbstractConcreteType> parents)
    {
		_node = node;
		_interfaces = interfaces;
		_parents = parents;
		
		for (AbstractConcreteType c : parents)
			c.supportsInterface(this);
		
		for (Interface i : interfaces)
		{
			for (Method m : i.getMethods())
				_methods.add(m);
		}
		
		_allInterfaceTypes.add(this);
    }
	
	@Override
	public Collection<Interface> getSupportedInterfaces()
	{
		return _interfaces;
	}
	
	public Collection<Method> getMethods()
    {
	    return _methods;
    }
	
	@Override
	public Callable getCallable(FunctionTemplateSignature fts, IsCallNode node)
	        throws CompilationException
	{
		for (Interface i : _interfaces)
		{
			Callable callable = i.getMethod(fts, node);
			if (callable != null)
				return callable;
		}
		assert(false);
		throw null;
	}
	
	@Override
	public void supportsInterface(InterfaceConcreteType ctype)
	{
		_downcasts.add(ctype);
	}
}
