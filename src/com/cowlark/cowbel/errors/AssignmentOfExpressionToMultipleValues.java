/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;

public class AssignmentOfExpressionToMultipleValues extends CompilationException
{
    private static final long serialVersionUID = 364494724060279571L;
    
	private VarAssignmentNode _node;
    
	public AssignmentOfExpressionToMultipleValues(VarAssignmentNode node)
    {
		_node = node;
    }
	
	@Override
	public String getMessage()
	{
		IdentifierListNode iln = _node.getVariables();
		
		return "When assigning from an expression you must use exactly one " +
			"variable name on the left side of the assignment, but the " +
			"assignment at "+iln.locationAsString()+" has "+
			iln.getNumberOfChildren();
	}
}
