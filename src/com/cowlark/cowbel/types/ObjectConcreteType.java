/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.io.PrintStream;
import java.util.Collection;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.Function;
import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.core.Implementation;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.core.Method;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;

public class ObjectConcreteType
		extends AbstractConcreteType
{
	public static TreeSet<ObjectConcreteType> _allObjectTypes =
		new TreeSet<ObjectConcreteType>();
	
	public static Collection<ObjectConcreteType> getAllObjectTypes()
	{
		return _allObjectTypes;
	}

	private Implementation _implementation;
	private TreeSet<InterfaceConcreteType> _downcasts =
		new TreeSet<InterfaceConcreteType>();
	
	public ObjectConcreteType(BlockExpressionNode node)
    {
		super(node);
		_implementation = node.getBlock().getImplementation();
		
		if (getClass().equals(ObjectConcreteType.class))
			_allObjectTypes.add(this);
    }
	
	public Implementation getImplementation()
    {
	    return _implementation;
    }
	
	public TreeSet<InterfaceConcreteType> getDowncasts()
    {
	    return _downcasts;
    }
	
	@Override
	public Collection<Interface> getSupportedInterfaces()
	{
		return _implementation.getInterfaces();
	}
	
	@Override
    public Callable getCallable(FunctionTemplateSignature fts, IsCallNode node)
			throws CompilationException
	{
		Callable callable = _implementation.getMethod(fts, node);
		assert(callable != null);
		return callable;
	}
	
	public Function getFunctionForMethod(Method method)
	{
		Callable callable = _implementation.getMethod(method.getSignature());
		assert(callable instanceof Function);
		return (Function) callable;
	}
	
	@Override
	public void supportsInterface(InterfaceConcreteType ctype)
	{
		_downcasts.add(ctype);
	}
	
	@Override
    public void dump(PrintStream ps)
	{
		super.dump(ps);
		if (!_downcasts.isEmpty())
		{
			ps.println("  downcasted to:");
			for (AbstractConcreteType act : _downcasts)
			{
				ps.print("    ");
				ps.println(act.toString());
			}
		}
	}
}
