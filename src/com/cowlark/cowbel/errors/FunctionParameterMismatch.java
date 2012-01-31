/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.types.Type;

public class FunctionParameterMismatch extends CompilationException
{
    private Node _node;
    private Function _function; 
	private List<Type> _functionoutput;
	private List<Type> _calledoutput;
	private List<Type> _functioninput;
	private List<Type> _calledinput;
    
	public FunctionParameterMismatch(Node node, Function function,
			List<Type> functionoutput, List<Type> calledoutput,
			List<Type> functioninput, List<Type> calledinput)
    {
		_node = node;
		_function = function;
		_functionoutput = functionoutput;
		_calledoutput = calledoutput;
		_functioninput = functioninput;
		_calledinput = calledinput;
    }
	
	@Override
	public String getMessage()
	{
		return "Function parameter mismatch in call to "+_function.toString() +
			" at " + _node.locationAsString();
	}
}
