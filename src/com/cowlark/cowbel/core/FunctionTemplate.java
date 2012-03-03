/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ASTCopyVisitor;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.interfaces.IsNode;

public class FunctionTemplate extends AbstractCallableTemplate
{
	private static ASTCopyVisitor astCopyVisitor = new ASTCopyVisitor();
	private static TreeSet<Function> _functions = new TreeSet<Function>();
	private static TreeSet<Function> _newFunctions = new TreeSet<Function>();
	
	/** Return the set of fully instantiated functions. */
	
	public static Set<Function> getInstantiatedFunctions()
	{
		return _functions;
	}
	
	/** Fetch the next partially instantiated function and add it to the
	 * fully instantiated function list. */
	
	public static Function getNextPendingFunction()
	{
		Function f = _newFunctions.pollFirst();
		if (f == null)
			return null;
		
		_functions.add(f);
		return f;
	}
	
	/** Returns true if there are partially instantiated functions waiting
	 * to be processed. */
	
	public static boolean pendingFunctions()
	{
		return !_newFunctions.isEmpty();
	}
	
	private FunctionDefinitionNode _definition;
	
	public FunctionTemplate(InterfaceContext context,
			FunctionDefinitionNode ast)
	{
		super(context, ast.getFunctionHeader());
		_definition = ast;
	}
	
    public FunctionDefinitionNode getDefinition()
    {
	    return _definition;
    }
	
	/** Returns a new function instance for the given template with the given
	 * type assignments, and add it the compiler's pending list.
	 */
	
	public Function instantiate(IsNode node, FunctionSignature signature,
			TypeAssignment ta)
				throws CompilationException
	{
		InterfaceContext tc = getContext();
		if ((ta != null) && !ta.isEmpty())
			tc = new InterfaceContext(node, tc, ta);
		
		/* Deep-copy the AST tree so the version this function can be
		 * annotated without affecting any other instantiations. */
		
		ASTCopyVisitor.Instance.reset();
		_definition.visit(ASTCopyVisitor.Instance);
		FunctionDefinitionNode ast = (FunctionDefinitionNode) ASTCopyVisitor.Instance.getResult();
		
		ast.setParent(getNode().getParent());
		ast.setTypeContext(tc);

		Function function = new Function(signature, ast);
		
		AbstractScopeConstructorNode definingscope = _definition.getScope();
		if (definingscope != null)
			function.setScope(definingscope);

		_newFunctions.add(function);
		
		return function;
	}
	
}
