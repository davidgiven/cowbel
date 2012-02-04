/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.AbstractTypeNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.TypeAssignmentNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.Type;

public class RecordTypeDefinitionsVisitor extends RecursiveVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(TypeAssignmentNode node) throws CompilationException
	{
		TypeContext typeContext = node.getScope().getTypeContext();
		IdentifierNode id = node.getIdentifier();
		AbstractTypeNode typenode = node.getType();
		Type type = typenode.calculateType();
		
		typeContext.addType(id, type);
	}
}