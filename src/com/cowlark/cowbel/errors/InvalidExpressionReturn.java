/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.types.FunctionType;

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
		FunctionType ft = (FunctionType) _function.getSymbolType();
		
		return "Using an inline expression with 'return' is only valid for " +
			"functions which return exactly one expression, but the " +
			"function at "+_function.getNode().locationAsString()+" returns " +
			ft.getOutputArgumentTypes().size();
	}
}
