/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ASTCopyVisitor;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.HasTypeVariables;
import com.cowlark.cowbel.utils.DeterministicObject;

public class InterfaceTemplate extends DeterministicObject<InterfaceTemplate>
		implements HasNode
{
	private InterfaceContext _context;
	private TypeAssignmentNode _ast;
	
	public InterfaceTemplate(InterfaceContext parentContext,
			TypeAssignmentNode ast)
	{
		_context = parentContext;
		_ast = ast;
	}
	
	@Override
    public TypeAssignmentNode getNode()
	{
		return _ast;
	}
	
	public InterfaceContext getInterfaceContext()
	{
		return _context;
	}
	
	public String getName()
	{
		return _ast.getIdentifier().getText();
	}
	
	public InterfaceContext createTypeContext(Node node,
			HasTypeVariables hastypevars, HasTypeArguments hastypeargs)
				throws CompilationException
	{
		if (!(node instanceof HasTypeArguments))
			return _context;

		
		return new InterfaceContext(node, _context, hastypevars, hastypeargs);
	}
	
	/** Returns a new type instance for the given template with the given
	 * type assignments.
	 */
	
	public Interface instantiate(Node node, TypeAssignment ta)
				throws CompilationException
	{
		InterfaceContext tc = _context;
		if (!ta.isEmpty())
			tc = new InterfaceContext(node, _context, ta);
		
		/* Deep-copy the AST tree so the version this type gets can be
		 * annotated without affecting any other instantiations. */
		
		ASTCopyVisitor.Instance.reset();
		getNode().visit(ASTCopyVisitor.Instance);
		TypeAssignmentNode ast = (TypeAssignmentNode) ASTCopyVisitor.Instance.getResult();
		
		ast.setParent(getNode().getParent());
		ast.setTypeContext(tc);

//		ast.visit(DefineInterfacesVisitor.Instance);
		
		Interface interf = ast.getTypeNode().getInterface();
		interf.setNameHint(ast.getIdentifier().getText());
		return interf;
	}
}
