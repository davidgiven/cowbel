/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionHeaderNode;
import com.cowlark.cowbel.ast.nodes.InterfaceTypeNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.InterfaceType;

public class DefineInterfacesVisitor extends RecursiveVisitor
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
