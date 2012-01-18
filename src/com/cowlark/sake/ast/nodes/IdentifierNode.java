package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;

public class IdentifierNode extends ExpressionNode
{
	private static int _globalid = 0;
	
	public static IdentifierNode createInternalIdentifier(String description)
	{
		int id = _globalid++;
		String name = " " + description + ": " + id;
		
		Location loc = new Location(name, "<internal>");
		MutableLocation end = new MutableLocation(loc);
		end.advance(name.codePointCount(0, name.length()));

		return new IdentifierNode(loc, end);
	}
	
	public IdentifierNode(Location start, Location end)
    {
		super(start, end);
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
