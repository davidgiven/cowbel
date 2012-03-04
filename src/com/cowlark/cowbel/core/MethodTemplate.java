/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsCallNode;

public class MethodTemplate extends AbstractCallableTemplate
{
	private Interface _type;
	
	public MethodTemplate(InterfaceContext context,
			FunctionHeaderNode node, Interface type)
	{
		super(context, node);
		_type = type;
	}
	
	/** Returns the method instance for the given template with the given
	 * type assignments. If no suitable method on this interface has already
	 * been instantiated, instantiate a new one.
	 */
	
	public Method instantiate(FunctionSignature signature, IsCallNode node,
			TypeAssignment ta)
				throws CompilationException
	{
		InterfaceContext tc = getContext();
		if ((ta != null) && !ta.isEmpty())
			tc = new InterfaceContext(node, tc, ta);
		
		/* Deep-copy the AST tree so the version this function can be
		 * annotated without affecting any other instantiations. */
		
		ASTCopyVisitor.Instance.reset();
		getNode().visit(ASTCopyVisitor.Instance);
		FunctionHeaderNode ast = (FunctionHeaderNode) ASTCopyVisitor.Instance.getResult();
		
		ast.setParent(getNode().getParent());
		ast.setTypeContext(tc);

		Method method = new Method(signature, ast, node);
		
		return method;
	}
}
