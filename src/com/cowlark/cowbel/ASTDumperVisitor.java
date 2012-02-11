/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.errors.CompilationException;

public class ASTDumperVisitor extends RecursiveASTVisitor
{
	private int _indent = 0;
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		node.dump(_indent);
		_indent++;
	    super.visit(node);
	    _indent--;
	}
}
