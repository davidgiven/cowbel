package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.Symbol;
import com.cowlark.sake.ast.HasSymbol;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.Type;

public class VarDeclarationNode extends StatementNode implements HasSymbol
{
	private Symbol _symbol;
	private ExpressionNode _initialiser;
	
	public VarDeclarationNode(Location start, Location end,
			IdentifierNode identifier, TypeNode type,
			ExpressionNode initialiser)
    {
		super(start, end);
		addChild(identifier);
		addChild(type);
		_initialiser = initialiser;
    }
	
	public IdentifierNode getVariableName()
	{
		return (IdentifierNode) getChild(0);
	}
	
	public TypeNode getVariableTypeNode()
	{
		return (TypeNode) getChild(1);
	}
	
	public Type getVariableType()
	{
		return getVariableTypeNode().getType();
	}
	
	public ExpressionNode getVariableInitialiser()
	{
		return _initialiser;
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
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
