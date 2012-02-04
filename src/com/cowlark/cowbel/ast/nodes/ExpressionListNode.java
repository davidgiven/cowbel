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
import com.cowlark.cowbel.types.Type;

public class ExpressionListNode extends Node
{
	public ExpressionListNode(Location start, Location end,
			List<AbstractExpressionNode> args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public ExpressionListNode(Location start, Location end, AbstractExpressionNode... args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public AbstractExpressionNode getExpression(int i)
	{
	    return (AbstractExpressionNode) getChild(i);
	}
	
	private List<Type> _types;
	public List<Type> calculateTypes() throws CompilationException
	{
		if (_types == null)
		{
			_types = new ArrayList<Type>();
			for (Node n : this)
			{
				AbstractExpressionNode e = (AbstractExpressionNode) n; 
				Type type = e.calculateType();
				_types.add(type);
			}
		}
		return _types;
	}
			
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
