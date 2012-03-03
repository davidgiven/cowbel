/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.errors.CompilationException;

public class RecordTypeDefinitionsVisitor extends RecursiveASTVisitor
{
	public static RecordTypeDefinitionsVisitor Instance =
		new RecordTypeDefinitionsVisitor();
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(TypeAssignmentNode node) throws CompilationException
	{
		InterfaceContext interfaceContext = node.getScope().getTypeContext();
		interfaceContext.addTypeTemplate(node);
	}
}
