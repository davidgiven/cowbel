/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.TypeContext;
import com.cowlark.cowbel.ast.HasNode;
import com.cowlark.cowbel.ast.HasScope;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.Token;

public abstract class Node extends Token implements Iterable<Node>, HasNode,
		HasScope
{
	private Vector<Node> _children = new Vector<Node>();
	private Node _parent;
	private AbstractScopeConstructorNode _scope;
	private TypeContext _typecontext;
	
	public Node(Location start, Location end)
    {
	    super(start, end);
    }
	
	@Override
	public Node getNode()
	{
	    return this;
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
	
	@Override
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
	}
	
	public void dumpDetails(int indent)
	{
	}
	
	@Override
    public AbstractScopeConstructorNode getScope()
	{
		if (_scope == null)
		{
			Node n = this;
			
			for (;;)
			{
				n = n.getParent();
				if (n == null)
					return null;
				if (n instanceof AbstractScopeConstructorNode)
				{
					_scope = (AbstractScopeConstructorNode) n;
					break;
				}
			}
		}
			
		return _scope;
	}
	
	public void setScope(AbstractScopeConstructorNode scope)
	{
		_scope = scope;
	}
	
	public TypeContext getTypeContext()
	{
		if (_typecontext == null)
		{
			Node n = this;
			
			for (;;)
			{
				n = n.getParent();
				if (n == null)
					return null;
				if (n._typecontext != null)
				{
					_typecontext = n._typecontext;
					break;
				}
			}
		}
			
		return _typecontext;
	}
	
	public void setTypeContext(TypeContext typecontext)
	{
		_typecontext = typecontext;
	}
	
	public boolean isLoopingNode()
	{
		return false;
	}
	
	public abstract void visit(Visitor visitor) throws CompilationException;
}
