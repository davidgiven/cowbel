/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.BasicBlock;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.interfaces.HasNode;

public class Label implements HasNode
{
	private LabelStatementNode _node;
	private BasicBlock _bb;
	
	public Label(LabelStatementNode node)
    {
		_node = node;
    }
	
	@Override
	public Node getNode()
	{
	    return _node;
	}
	
	public IdentifierNode getLabelName()
	{
		return _node.getLabelName();
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + getLabelName().getText();
	}
	
	public BasicBlock getBasicBlock(Function function)
    {
		if (_bb == null)
			_bb = new BasicBlock(function);
	    return _bb;
    }
}
