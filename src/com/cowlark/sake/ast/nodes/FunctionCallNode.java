package com.cowlark.sake.ast.nodes;

import java.util.List;
import java.util.Vector;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class FunctionCallNode extends ExpressionNode
{
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
    }
	
	public FunctionCallNode(Location start, Location end, ExpressionNode object,
			List<ExpressionNode> arguments)
	{
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
	}
	
	public ExpressionNode getFunction()
	{
		return (ExpressionNode) getChild(0);
	}
	
	private Vector<ExpressionNode> _arguments;
	public List<ExpressionNode> getArguments()
	{
		if (_arguments == null)
		{
			_arguments = new Vector<ExpressionNode>();
			
			for (int i = 1; i < getNumberOfChildren(); i++)
				_arguments.add((ExpressionNode) getChild(i));
		}
		
		return _arguments;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
