/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasScope;
import com.cowlark.cowbel.interfaces.IsCallNode;

public class Method extends Callable
		implements HasNode, HasScope
{
	private IsCallNode _methodcall;
	
	public Method(FunctionSignature signature, FunctionHeaderNode node,
			IsCallNode methodcall)
		throws CompilationException
    {
		super(signature, node);
		_methodcall = methodcall;
    }
	
	public IsCallNode getRepresentativeMethodCall()
    {
	    return _methodcall;
    }
}
