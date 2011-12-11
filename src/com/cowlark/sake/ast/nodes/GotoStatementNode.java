package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.Label;
import com.cowlark.sake.ast.HasLabel;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class GotoStatementNode extends StatementNode implements HasLabel
{
	private Label _label;
	
	public GotoStatementNode(Location start, Location end, IdentifierNode name)
    {
		super(start, end);
		addChild(name);
    }
	
	@Override
	public String getShortDescription()
	{
		if (_label == null)
			return getText();
		else
			return _label.toString();
	}
	
	public IdentifierNode getLabelName()
	{
		return (IdentifierNode) getChild(0);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	@Override
	public Label getLabel()
	{
	    return _label;
	}
	
	@Override
	public void setLabel(Label label)
	{
	    _label = label;	    
	}
}
