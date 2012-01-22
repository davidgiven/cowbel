package com.cowlark.sake;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.Variable;

public class Constructor implements Comparable<Constructor>
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private ScopeConstructorNode _node;
	private boolean _persistent;
	private TreeSet<Variable> _registerVariables = new TreeSet<Variable>();
	private TreeSet<Variable> _stackVariables = new TreeSet<Variable>();
	private TreeSet<Function> _directFunctions = new TreeSet<Function>();
	private TreeSet<Constructor> _parentConstructors = new TreeSet<Constructor>();
	
	public Constructor(ScopeConstructorNode node)
	{
		_node = node;
	}
	
	public ScopeConstructorNode getNode()
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
		return "Constructor" + _id + ": " +
			(_persistent ? "persistent" : "volatile");
	}
	
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
		ScopeConstructorNode parentscope = _node.getScope();
		if (parentscope == null)
			return null;
		return parentscope.getConstructor();
	}
	
	public void addConstructor(Constructor constructor)
	{
		Constructor c = this;
		
		do
		{
			ScopeConstructorNode parentscope = c.getNode().getScope();
			if (parentscope == null)
				break;
			c = parentscope.getConstructor();
			
			_parentConstructors.add(c);
		}
		while (c != constructor);
	}
	
	public void addVariable(Variable variable)
	{
		ScopeConstructorNode scope = variable.getScope();
		if (scope.isSymbolExportedToDifferentFunction(variable))
			_stackVariables.add(variable);
		else
			_registerVariables.add(variable);
	}
	
	public void addFunction(Function function)
	{
		ScopeConstructorNode scope = function.getScope();
		_directFunctions.add(function);
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
		
		System.out.println("direct functions:");
		for (Function f : _directFunctions)
		{
			System.out.print("  ");
			System.out.println(f.toString());
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
