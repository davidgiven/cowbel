/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.AbstractTypeExpressionNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.RecursiveASTVisitor;
import com.cowlark.cowbel.ast.TypeExternNode;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;
import com.cowlark.cowbel.symbols.Variable;

public class RecordVariableDeclarationsVisitor extends RecursiveASTVisitor
{
	public static RecordVariableDeclarationsVisitor Instance =
		new RecordVariableDeclarationsVisitor();
	
	@Override
	public void visit(FunctionDefinitionNode node)
	        throws CompilationException
	{
		IsNode parent = node.getParent();
		FunctionTemplate ft = new FunctionTemplate(
				parent.getTypeContext(), node);
		node.getScope().getImplementation().addMethodTemplate(ft);
		
		/* Don't recurse into child functions. */
	}
	
	@Override
	public void visit(TypeExternNode node) throws CompilationException
	{
		node.getScope().getImplementation().addExternType(node.getExternType().getValue());
	}
	
	@Override
	public void visit(VarDeclarationNode node)
	        throws CompilationException
	{
		/* Add these symbols to the current scope. */
		
		AbstractScopeConstructorNode scope = node.getScope();
		ParameterDeclarationListNode pdln = node.getVariables();
		for (IsNode n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			IdentifierNode name = pdn.getIdentifier();
			TypeRef typeref = pdn.getTypeRef();
			typeref.addCastConstraint(pdn.getVariableTypeNode().getInterface());
			
			Variable v = new Variable(pdn, name, typeref);
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
		Implementation implementation = node.getScope().getImplementation();
		AbstractTypeExpressionNode typenode = node.getTypeNode();
		Interface interf = typenode.getInterface();
		
		implementation.addInterface(interf);
		interf.addImplementation(implementation);
		
	}
}
