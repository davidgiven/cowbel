package com.cowlang2.parser.tokens;

import java.util.Vector;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.Token;

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
