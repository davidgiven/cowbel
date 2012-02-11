/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.HasInterfaces;
import com.cowlark.cowbel.types.InterfaceType;

public class Constructor implements Comparable<Constructor>, HasInterfaces
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private AbstractScopeConstructorNode _node;
	private boolean _persistent;
	private TreeSet<Variable> _registerVariables = new TreeSet<Variable>();
	private TreeSet<Variable> _stackVariables = new TreeSet<Variable>();
	private TreeSet<Constructor> _parentConstructors = new TreeSet<Constructor>();
	private TreeSet<InterfaceType> _interfaces = new TreeSet<InterfaceType>();
	private TreeMap<VirtualMethod, Function> _virtualMethods =
		new TreeMap<VirtualMethod, Function>();
	
	public Constructor(AbstractScopeConstructorNode node)
	{
		_node = node;
	}
	
	public AbstractScopeConstructorNode getNode()
	{
		return _node;
	}
	
	public int getId()
    {
	    return _id;
    }
	
	public boolean isPersistent()
	{
		return _persistent;
	}
	
	public void setPersistent(boolean persistent)
	{
		_persistent = persistent;
	}
	
	@Override
	public String toString()
	{
		return "Constructor" + _id + ": " +
			(_persistent ? "persistent" : "volatile");
	}
	
	@Override
    public int compareTo(Constructor other)
	{
		if (_id < other._id)
			return -1;
		if (_id == other._id)
			return 0;
		return 1;
	}
	
	public Constructor getParentConstructor()
	{
		AbstractScopeConstructorNode parentscope = _node.getScope();
		if (parentscope == null)
			return null;
		return parentscope.getConstructor();
	}
	
	public void addConstructor(Constructor constructor)
	{
		Constructor c = this;
		
		while (c != constructor)
		{
			AbstractScopeConstructorNode parentscope = c.getNode().getScope();
			if (parentscope == null)
				break;
			c = parentscope.getConstructor();
			
			_parentConstructors.add(c);
		}
	}
	
	public void addVariable(Variable variable)
	{
		AbstractScopeConstructorNode scope = variable.getScope();
		if (scope.isSymbolExportedToDifferentFunction(variable))
			_stackVariables.add(variable);
		else
			_registerVariables.add(variable);
	}
	
	public void addInterface(InterfaceType itype)
	{
		_interfaces.add(itype);
	}
	
	public Set<Variable> getStackVariables()
	{
		return Collections.unmodifiableSet(_stackVariables);
	}
	
	public Set<Variable> getRegisterVariables()
	{
		return Collections.unmodifiableSet(_registerVariables);
	}
	
	public boolean isStackVariable(Variable var)
	{
		return _stackVariables.contains(var);
	}
	
	public Set<Constructor> getParentConstructors()
	{
		return Collections.unmodifiableSet(_parentConstructors);
	}
	
	@Override
	public Collection<InterfaceType> getInterfaces()
	{
	    return Collections.unmodifiableCollection(_interfaces);
	}

	public void addVirtualMethod(VirtualMethod method, Function function)
	{
		_virtualMethods.put(method, function);
	}
	
	public Function getFunctionForVirtualMethod(VirtualMethod method)
	{
		return _virtualMethods.get(method);
	}
	
	public void dumpDetails()
	{
		System.out.println(_node.toString());
		
		if (_parentConstructors.size() > 0)
		{
			System.out.println("parent constructors:");
			
			for (Constructor c : _parentConstructors)
			{
				System.out.print("  ");
				System.out.println(c.toString());
			}
		}
		
		if (_interfaces.size() > 0)
		{
			System.out.println("interfaces:");
			
			for (InterfaceType i : _interfaces)
			{
				System.out.print("  ");
				System.out.println(i.toString());
			}
		}
		
		System.out.println("virtual methods:");
		for (Map.Entry<VirtualMethod, Function> e : _virtualMethods.entrySet())
		{
			System.out.print("  ");
			System.out.print(e.getKey().toString());
			System.out.print(" -> ");
			System.out.println(e.getValue().toString());
		}
		
		System.out.println("register variables:");
		for (Variable v : _registerVariables)
		{
			System.out.print("  ");
			System.out.println(v.toString());
		}
		
		System.out.println("stack variables:");
		for (Variable v : _stackVariables)
		{
			System.out.print("  ");
			System.out.println(v.toString());
		}
	}
}
