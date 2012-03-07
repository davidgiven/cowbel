/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.Collection;
import java.util.Collections;
import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.core.Interface;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;
import com.cowlark.cowbel.interfaces.IsNode;

public class InferenceFailedConcreteType
		extends AbstractConcreteType
{
	private IsNode _node;
	
	public InferenceFailedConcreteType(IsNode node)
    {
		_node = node;
    }

	@Override
	public Collection<Interface> getSupportedInterfaces()
	{
		return Collections.emptySet();
	}
	
	@Override
	public Callable getCallable(FunctionTemplateSignature fts, IsCallNode node)
	        throws CompilationException
	{
	    return null;
	}
	
	@Override
	public void supportsInterface(InterfaceConcreteType ctype)
	{
	}
}