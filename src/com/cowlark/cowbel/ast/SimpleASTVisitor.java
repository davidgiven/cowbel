/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;

public class SimpleASTVisitor extends ASTVisitor
{
	@Override
	public void visit(StatementListNode node) throws CompilationException
	{
		for (IsNode n : node)
			n.visit(this);
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
	}
}
