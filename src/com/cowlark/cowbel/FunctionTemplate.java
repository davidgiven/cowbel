/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Map;
import java.util.TreeMap;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.types.FunctionType;

public class FunctionTemplate extends AbstractCallableTemplate
{
	private static ASTCopyVisitor astCopyVisitor = new ASTCopyVisitor();
	private static TreeMap<String, Function> _functions = new TreeMap<String, Function>();
	private static TreeMap<String, Function> _newFunctions = new TreeMap<String, Function>();
	
	/** Return the map of fully instantiated functions. */
	
	public static Map<String, Function> getInstantiatedFunctions()
	{
		return _functions;
	}
	
	/** Fetch the next partially instantiated function and add it to the
	 * fully instantiated function list. */
	
	public static Function getNextPendingFunction()
	{
		Map.Entry<String, Function> e = _newFunctions.pollFirstEntry();
		if (e == null)
			return null;
		
		_functions.put(e.getKey(), e.getValue());
		return e.getValue();
	}
	
	private FunctionDefinitionNode _definition;
	
	public FunctionTemplate(TypeContext parentContext,
			FunctionDefinitionNode ast)
	{
		super(parentContext, ast.getFunctionHeader());
		_definition = ast;
	}
	
	public FunctionDefinitionNode getFunctionDefinition()
    {
	    return _definition;
    }
	
	/** Returns the function instance for the given template with the given
	 * type assignments. If no suitable function has already been instantiated,
	 * instantiate a new one and add it to the compiler's pending list.
	 */
	
	public Function instantiate(Node node, TypeListNode typeassignments)
				throws CompilationException
	{
		FunctionDefinitionNode funcdef = getFunctionDefinition();
		
		TypeContext tc = createTypeContext(node, typeassignments);
		String signature = tc.getSignature();
		signature += " ";
		
		AbstractScopeConstructorNode definingscope = funcdef.getScope();
		if (definingscope != null)
		{
			signature += definingscope.getId();
			signature += " ";
		}
		
		signature += funcdef.locationAsString();

		Function function = _functions.get(signature);
		if (function != null)
			return function;
		function = _newFunctions.get(signature);
		if (function != null)
			return function;
		
		/* Deep-copy the AST tree so the version the function gets can be
		 * annotated without affecting any other instantiations. */
		
		funcdef.visit(astCopyVisitor);
		FunctionDefinitionNode ast = (FunctionDefinitionNode) astCopyVisitor.getResult();
		
		ast.setParent(funcdef.getParent());
		ast.setTypeContext(tc);
		
		function = new Function(signature, ast);
		FunctionType type = (FunctionType) ast.getFunctionHeader().calculateFunctionType();
		function.setType(type);
		
		if (definingscope != null)
			function.setScope(definingscope);

		_newFunctions.put(signature, function);
		
		return function;
	}
	
}
