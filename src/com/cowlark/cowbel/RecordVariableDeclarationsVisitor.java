/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.nodes.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Variable;

public class RecordVariableDeclarationsVisitor extends RecursiveVisitor
{
	private Node _rootNode;
	
	public RecordVariableDeclarationsVisitor(Node rootNode)
    {
		_rootNode = rootNode;
    }
	
	private void add_parameters(Function function,
			FunctionDefinitionNode node, ParameterDeclarationListNode pdln,
			boolean isOutputParameter)
			throws CompilationException
	{
		FunctionScopeConstructorNode body = node.getFunctionBody();

		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;

			Variable v = new Variable(pdn);
			v.setParameter(true);
			v.setOutputParameter(isOutputParameter);
			v.setScope(body);
			body.addSymbol(v);
			pdn.setSymbol(v);
		}
	}
	
	@Override
	public void visit(FunctionDefinitionNode node)
	        throws CompilationException
	{
		/* Add this function to the current scope. */
		
		Function f = new Function(node);
		node.getScope().addSymbol(f);
		node.setSymbol(f);

		FunctionScopeConstructorNode body = node.getFunctionBody();
		body.setFunction(f);
		f.setScope(body);
		
		/* Add function parameters to its scope. */
		
		ParameterDeclarationListNode pdln = node.getFunctionHeader().getInputParametersNode();
		add_parameters(f, node, pdln, false);

		/* Add function return parameters to scope. */
		
		pdln = node.getFunctionHeader().getOutputParametersNode();
		add_parameters(f, node, pdln, true);
		
		super.visit(node);
	}
	
	@Override
	public void visit(VarDeclarationNode node)
	        throws CompilationException
	{
		/* Add these symbols to the current scope. */
		
		ScopeConstructorNode scope = node.getScope();
		ParameterDeclarationListNode pdln = node.getVariables();
		for (Node n : pdln)
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			Variable v = new Variable(pdn);
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
		
		ScopeConstructorNode scope = node.getScope();
		Label label = new Label(node);
		
		scope.addLabel(label);
		node.setLabel(label);
		
	    super.visit(node);
	}
}
