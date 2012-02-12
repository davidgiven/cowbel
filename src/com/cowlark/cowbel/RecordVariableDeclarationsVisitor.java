/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.errors.CanOnlyImplementInterfaces;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.InterfaceType;
import com.cowlark.cowbel.types.Type;

public class RecordVariableDeclarationsVisitor extends RecursiveASTVisitor
{
	@Override
	public void visit(FunctionDefinitionNode node)
	        throws CompilationException
	{
		Node parent = node.getParent();
		FunctionTemplate ft = new FunctionTemplate(
				parent.getTypeContext(), node);
		parent.getScope().addFunctionTemplate(ft);
		
		/* Don't recurse into child functions. */
	}
	
	@Override
	public void visit(VarDeclarationNode node)
	        throws CompilationException
	{
		/* Add these symbols to the current scope. */
		
		AbstractScopeConstructorNode scope = node.getScope();
		ParameterDeclarationListNode pdln = node.getVariables();
		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			IdentifierNode name = pdn.getIdentifier();
			Type type = pdn.getVariableTypeNode().getType();
			
			Variable v = new Variable(pdn, name, type);
			v.setScope(scope);
			scope.addSymbol(v);
			pdn.setSymbol(v);
		}
		
	    super.visit(node);
	}
	
	@Override
	public void visit(LabelStatementNode node) throws CompilationException
	{
		/* Add this label to the current scope. */
		
		AbstractScopeConstructorNode scope = node.getScope();
		Label label = new Label(node);
		
		scope.addLabel(label);
		node.setLabel(label);
		
	    super.visit(node);
	}
	
	@Override
	public void visit(ImplementsStatementNode node) throws CompilationException
	{
		AbstractScopeConstructorNode scope = node.getScope();
		Type type = node.getTypeNode().getType();
		
		if (!(type instanceof InterfaceType))
			throw new CanOnlyImplementInterfaces(node, type);
		
		scope.addInterface((InterfaceType) type);
	}
}
