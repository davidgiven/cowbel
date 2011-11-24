package com.cowlark.sake.parser.tokens;

import java.util.List;
import java.util.Vector;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.Token;

public class Node extends Token
{
	private Vector<Node> _children = new Vector<Node>();
	
	public Node(Location start, Location end)
    {
	    super(start, end);
    }
	
	public void addChild(Node child)
	{
		_children.add(child);
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
	
	public Iterable getChildren()
	{
		return _children;
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
		
		for (Node n : _children)
			n.dump(indent+1);
	}
}
