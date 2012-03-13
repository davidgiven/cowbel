/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.interfaces.IsMethod;

public class CouldNotFindMethod extends CompilationException
{
    private static final long serialVersionUID = 6971449499513232012L;

    private IsMethod _node;
    
	public CouldNotFindMethod(IsMethod node)
    {
		_node = node;
    }
	
	@Override
    public String getMessage()
	{
		FunctionTemplateSignature signature = new FunctionTemplateSignature(_node);
		return "Could not find a method " + signature + " on the value at " +
			_node.getReceiver().locationAsString();
	}
}
