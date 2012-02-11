/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.errors.CompilationException;

public class RecursiveASTVisitor extends ASTVisitor
{
	@Override
	public void visit(Node node) throws CompilationException
	{
		for (Node n : node)
			n.visit(this);
	}
}