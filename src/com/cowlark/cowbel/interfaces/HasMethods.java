/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.interfaces;

import com.cowlark.cowbel.core.Callable;
import com.cowlark.cowbel.core.FunctionTemplateSignature;
import com.cowlark.cowbel.errors.CompilationException;

public interface HasMethods
{
	public Callable lookupMethod(FunctionTemplateSignature signature,
			IsCallNode node)
			throws CompilationException;
}
