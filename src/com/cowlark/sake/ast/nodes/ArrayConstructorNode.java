package com.cowlark.sake.ast.nodes;

import java.util.ArrayList;
import java.util.List;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class ArrayConstructorNode extends ExpressionLiteralNode
{
	public ArrayConstructorNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ArrayConstructorNode(Location start, Location end,
			List<ExpressionNode> params)
    {
		this(start, end);
		addChildren(params);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
	
	private ArrayList<ExpressionNode> _members;
	public List<ExpressionNode> getListMembers()
	{
		if (_members == null)
		{
			_members = new ArrayList<ExpressionNode>();
			for (Node node : getChildren())
				_members.add((ExpressionNode) node);
		}
		return _members;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}
