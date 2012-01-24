package com.cowlark.cowbel.ast.nodes;

import java.util.List;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class StatementListNode extends StatementNode
{
	public StatementListNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end);
        addChildren(statements);
    }
	
	public StatementListNode(Location start, Location end,
			StatementNode... statements)
	{
		super(start, end);
		addChildren(statements);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}