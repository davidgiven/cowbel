/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.InterfaceTypeNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.InterfaceType;

public class DefineInterfacesVisitor extends RecursiveASTVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}

	@Override
	public void visit(InterfaceTypeNode node) throws CompilationException
	{
		for (Node n : node)
		{
			if (n instanceof FunctionHeaderNode)
			{
				FunctionHeaderNode fhn = (FunctionHeaderNode) n;
				InterfaceType type = (InterfaceType) node.getType();
				MethodTemplate mt = new MethodTemplate(
						node.getTypeContext(), fhn, type);
				type.addMethodTemplate(mt);
			}
		}
		
		super.visit(node);
	}
}
