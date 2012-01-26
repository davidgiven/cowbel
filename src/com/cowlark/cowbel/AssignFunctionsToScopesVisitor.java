/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Set;
import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Function;

public class AssignFunctionsToScopesVisitor extends RecursiveVisitor
{
	private Set<Function> _functions;
	
	public AssignFunctionsToScopesVisitor(Set<Function> functions)
	{
		_functions = functions;
	}
	
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		if (node.isFunctionScope())
			_functions.add(node.getFunction());
		else
		{
			Function f = node.getScope().getFunction();
			node.setFunction(f);
		}
		
		super.visit(node);
	}
}
