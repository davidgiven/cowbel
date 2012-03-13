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

	private Collection<Interface> _interfaces;
	private TreeSet<Method> _methods =
		new TreeSet<Method>();
	private TreeSet<AbstractConcreteType> _parents =
		new TreeSet<AbstractConcreteType>();
	private TreeSet<InterfaceConcreteType> _downcasts =
		new TreeSet<InterfaceConcreteType>();
	
	public InterfaceConcreteType(IsNode node, Collection<Interface> interfaces)
    {
		super(node);
		_interfaces = interfaces;
		
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
	
	public void addParents(Collection<AbstractConcreteType> parents)
	{
		for (AbstractConcreteType ctype : parents)
		{
			if (!_parents.contains(ctype))
			{
				_parents.add(ctype);
				ctype.supportsInterface(this);
			}
		}
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
