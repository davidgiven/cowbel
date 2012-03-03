/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.ReturnStatementNode;
import com.cowlark.cowbel.core.Function;

public class InvalidExpressionReturn extends CompilationException
{
    private static final long serialVersionUID = 1285911348602107188L;
    
	private ReturnStatementNode _node;
	private Function _function;
    
	public InvalidExpressionReturn(ReturnStatementNode node, Function function)
    {
		_node = node;
		_function = function;
    }
	
	@Override
	public String getMessage()
	{
		return "Using an inline expression with 'return' is only valid for " +
			"functions which return exactly one expression, but the " +
			"function at "+_function.getNode().locationAsString()+" returns " +
			_function.getNode().getOutputParametersNode().getNumberOfChildren();
	}
}
