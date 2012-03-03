/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.Label;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasLabel;
import com.cowlark.cowbel.parser.core.Location;

public class GotoStatementNode extends AbstractStatementNode implements HasLabel
{
	private Label _label;
	
	public GotoStatementNode(Location start, Location end)
    {
		super(start, end);
    }
	
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
	public void visit(ASTVisitor visitor) throws CompilationException
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
