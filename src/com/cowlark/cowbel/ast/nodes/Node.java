package com.cowlark.cowbel.ast.nodes;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.Token;

public abstract class Node extends Token implements Iterable<Node>
{
	private Vector<Node> _children = new Vector<Node>();
	private Node _parent;
	private ScopeConstructorNode _scope;
	
	public Node(Location start, Location end)
    {
	    super(start, end);
    }
	
	public void addChild(Node child)
	{
		_children.add(child);
		child.setParent(this);
	}
	
	public Node getChild(int i)
	{
		return _children.get(i);
	}

	public void addChildren(List<? extends Node> children)
	{
		for (Node n : children)
			addChild(n);
	}
	
	public void addChildren(Node... children)
	{
		for (Node n : children)
			addChild(n);
	}
	
	public int getNumberOfChildren()
	{
		return _children.size();
	}
	
	public Iterator<Node> iterator()
	{
		return _children.iterator();
	}
	
	public void setParent(Node parent)
	{
		_parent = parent;
	}
	
	public Node getParent()
	{
		return _parent;
	}
	
	public String getNodeName()
	{
		return getClass().getSimpleName();
	}
	
	public String getShortDescription()
	{
		return "";
	}
	
	public void dump()
	{
		dump(0);
	}
	
	protected void spaces(int n)
	{
		for (int i = 0; i < n; i++)
			System.out.print(" ");
	}
	
	public void dump(int indent)
	{
		spaces(indent);
		System.out.print(getNodeName());
		System.out.print(" ");
		System.out.println(getShortDescription());

		dumpDetails(indent+2);
		
		for (Node n : _children)
			n.dump(indent+1);
	}
	
	public void dumpDetails(int indent)
	{
	}
	
	public ScopeConstructorNode getScope()
	{
		if (_scope == null)
		{
			Node n = this;
			
			for (;;)
			{
				n = n.getParent();
				if (n == null)
					return null;
				if (n instanceof ScopeConstructorNode)
				{
					_scope = (ScopeConstructorNode) n;
					break;
				}
			}
		}
			
		return _scope;
	}
	
	public void setScope(ScopeConstructorNode scope)
	{
		_scope = scope;
	}
	
	public boolean isLoopingNode()
	{
		return false;
	}
	
	public abstract void visit(Visitor visitor) throws CompilationException;
}
