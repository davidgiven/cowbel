/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;

public class WrongNumberOfExpressionsInMultipleAssignments extends CompilationException
{
    private static final long serialVersionUID = 364494724060279571L;
    
	private VarAssignmentNode _node;
    
	public WrongNumberOfExpressionsInMultipleAssignments(VarAssignmentNode node)
    {
		_node = node;
    }
	
	@Override
	public String getMessage()
	{
		IdentifierListNode iln = _node.getVariables();
		ExpressionListNode eln = _node.getExpressions();
		
		return "You have attempted to assign " + eln.getNumberOfChildren() +
			" values to " + iln.getNumberOfChildren() + " variables at " +
			iln.locationAsString();
	}
}
