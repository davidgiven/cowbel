/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import com.cowlark.cowbel.core.InterfaceContext;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.Token;

public abstract class Node extends Token implements IsNode
{
	private Vector<IsNode> _children = new Vector<IsNode>();
	private IsNode _parent;
	private AbstractScopeConstructorNode _scope;
	private InterfaceContext _typecontext;
	
	public Node(Location start, Location end)
    {
	    super(start, end);
    }
	
	@Override
	public Node getNode()
	{
	    return this;
	}
	
	public void addChild(IsNode child)
	{
		_children.add(child);
		child.setParent(this);
	}
	
	public IsNode getChild(int i)
	{
		return _children.get(i);
	}

	public void addChildren(Collection<? extends IsNode> children)
	{
		for (IsNode n : children)
			addChild(n);
	}
	
	public void addChildren(IsNode... children)
	{
		for (IsNode n : children)
			addChild(n);
	}
	
	@Override
    public int getNumberOfChildren()
	{
		return _children.size();
	}
	
	@Override
    public Iterator<IsNode> iterator()
	{
		return _children.iterator();
	}
	
	@Override
    public void setParent(IsNode parent)
	{
		_parent = parent;
	}
	
	@Override
    public IsNode getParent()
	{
		return _parent;
	}
	
	@Override
    public String getNodeName()
	{
		return getClass().getSimpleName();
	}
	
	@Override
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
			IsNode n = this;
			
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
	
	@Override
    public InterfaceContext getTypeContext()
	{
		if (_typecontext == null)
			_typecontext = getParent().getTypeContext();
			
		return _typecontext;
	}
	
	@Override
    public void setTypeContext(InterfaceContext typecontext)
	{
		_typecontext = typecontext;
	}
	
	@Override
    public boolean isLoopingNode()
	{
		return false;
	}
	
	@Override
    public abstract void visit(ASTVisitor visitor) throws CompilationException;
}
