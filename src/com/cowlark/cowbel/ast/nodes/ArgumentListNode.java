package com.cowlark.cowbel.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.types.Type;

public class ArgumentListNode extends Node
{
	public ArgumentListNode(Location start, Location end,
			List<ExpressionNode> args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public ArgumentListNode(Location start, Location end, ExpressionNode... args)
    {
		super(start, end);
		addChildren(args);
    }
	
	public ExpressionNode getExpression(int i)
	{
	    return (ExpressionNode) getChild(i);
	}
	
	private List<Type> _types;
	public List<Type> calculateTypes() throws CompilationException
	{
		if (_types == null)
		{
			_types = new ArrayList<Type>();
			for (Node n : this)
			{
				ExpressionNode e = (ExpressionNode) n; 
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
