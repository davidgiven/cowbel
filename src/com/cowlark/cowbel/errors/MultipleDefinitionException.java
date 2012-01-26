/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.HasNode;

public class MultipleDefinitionException extends CompilationException
{
	public MultipleDefinitionException(HasNode old, HasNode current)
    {
    }
}
