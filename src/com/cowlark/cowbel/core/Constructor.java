/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.ObjectConcreteType;
import com.cowlark.cowbel.utils.DeterministicObject;

public class Constructor extends DeterministicObject<Constructor>
{
	private static TreeSet<Constructor> _allConstructors =
		new TreeSet<Constructor>();
	
	public static Collection<Constructor> getAllConstructors()
	{
		return _allConstructors;
	}
	
	private AbstractScopeConstructorNode _node;
	private boolean _persistent;
	private TreeSet<Variable> _registerVariables = new TreeSet<Variable>();
	private TreeSet<Variable> _stackVariables = new TreeSet<Variable>();
	private TreeSet<Constructor> _parentConstructors = new TreeSet<Constructor>();
	private TreeSet<ObjectConcreteType> _objects = new TreeSet<ObjectConcreteType>();
	
	public Constructor(AbstractScopeConstructorNode node)
	{
		_node = node;
		_allConstructors.add(this);
	}
	
	public AbstractScopeConstructorNode getNode()
	{
		return _node;
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
		return "Constructor" + getId() + ": " +
			(_persistent ? "persistent" : "volatile");
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
	
	public void addObject(ObjectConcreteType ctype)
	{
		assert(!_objects.contains(ctype));
		_objects.add(ctype);
	}
	
	public void addVariable(Variable variable)
	{
		AbstractScopeConstructorNode scope = variable.getScope();
		if (scope.isSymbolExportedToDifferentFunction(variable))
			_stackVariables.add(variable);
		else
			_registerVariables.add(variable);
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
	
	public TreeSet<ObjectConcreteType> getObjects()
    {
	    return _objects;
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
		
		if (_objects.size() > 0)
		{
			System.out.println("objects:");
			
			for (ObjectConcreteType oct : _objects)
			{
				System.out.print("  ");
				System.out.println(oct.toString());
			}
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
