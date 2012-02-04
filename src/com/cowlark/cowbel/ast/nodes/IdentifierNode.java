/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.Comparator;
import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;

public class IdentifierNode extends AbstractExpressionNode
{
	private static int _globalid = 0;
	
	public static IdentifierNode createInternalIdentifier(String description)
	{
		int id = _globalid++;
		return createIdentifier("#" + description + "_" + id);
	}
	
	public static IdentifierNode createIdentifier(String name)
	{
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
	
	public static Comparator<IdentifierNode> valueComparator =
		new Comparator<IdentifierNode>()
		{
			@Override
            public int compare(IdentifierNode o1, IdentifierNode o2)
			{
				return o1.getText().compareTo(o2.getText());
			}
		};
}
