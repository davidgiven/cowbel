/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.types.Type;

public class InvalidFunctionCallInExpressionContext extends CompilationException
{
    private static final long serialVersionUID = 2219952398091478590L;
    
	private ExpressionNode _node;
	private List<Type> _inputTypes;
	private List<Type> _outputTypes;
    
	public InvalidFunctionCallInExpressionContext(ExpressionNode node,
			List<Type> inputTypes, List<Type> outputTypes)
    {
		_node = node;
		_inputTypes = inputTypes;
		_outputTypes = outputTypes;
    }
	
	@Override
	public String getMessage()
	{
		return "Function call at "+_node.locationAsString()+" must return " +
				"exactly one value to be usable in an expression context " +
				"(but actually returns " + _outputTypes.size() + ")";
	}
}
