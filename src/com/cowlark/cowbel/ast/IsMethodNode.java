/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;

public interface IsMethodNode extends IsCallable
{
	public IdentifierNode getMethodIdentifier();
	public ExpressionNode getMethodReceiver();
}
