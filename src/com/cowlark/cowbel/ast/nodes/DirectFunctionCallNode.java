package com.cowlark.cowbel.ast.nodes;

import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.HasSymbol;
import com.cowlark.cowbel.ast.IsCallable;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public class DirectFunctionCallNode extends ExpressionNode
		implements HasSymbol, IsCallable
{
	private Symbol _symbol;
	
	public DirectFunctionCallNode(Location start, Location end, IdentifierNode object,
			ExpressionNode... arguments)
    {
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
    }
	
	public DirectFunctionCallNode(Location start, Location end, IdentifierNode object,
			List<ExpressionNode> arguments)
	{
		super(start, end);
		addChild(object);
		for (ExpressionNode n : arguments)
			addChild(n);
	}
	
	public IdentifierNode getIdentifier()
	{
		return (IdentifierNode) getChild(0);
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
	public int getArgumentCount()
	{
	    return getNumberOfChildren() - 1;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	@Override
	public Symbol getSymbol()
	{
	    return _symbol;
	}
	
	@Override
	public void setSymbol(Symbol symbol)
	{
	    _symbol = symbol;	    
	}
}
