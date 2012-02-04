/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import com.cowlark.cowbel.ast.HasNode;

public class MultipleDefinitionException extends CompilationException
{
    private static final long serialVersionUID = 5324462605948584281L;

	public MultipleDefinitionException(HasNode old, HasNode current)
    {
    }
}
