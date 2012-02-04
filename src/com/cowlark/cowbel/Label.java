/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.HasNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;

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
