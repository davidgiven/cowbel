/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class ArrayConstructorNode extends AbstractExpressionLiteralNode
{
	public ArrayConstructorNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ArrayConstructorNode(Location start, Location end,
			List<AbstractExpressionNode> params)
    {
		this(start, end);
		addChildren(params);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	private ArrayList<AbstractExpressionNode> _members;
	public List<AbstractExpressionNode> getListMembers()
	{
		if (_members == null)
		{
			_members = new ArrayList<AbstractExpressionNode>();
			for (Node node : this)
				_members.add((AbstractExpressionNode) node);
		}
		return _members;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
