/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.methods.VirtualMethod;
import com.cowlark.cowbel.types.InterfaceType;

public class MethodTemplate extends AbstractCallableTemplate
{
	private static ASTCopyVisitor astCopyVisitor = new ASTCopyVisitor();

	private InterfaceType _type;
	
	public MethodTemplate(TypeContext parentContext,
			FunctionHeaderNode ast, InterfaceType type)
	{
		super(parentContext, ast);
		_type = type;
	}
	
	public InterfaceType getType()
    {
	    return _type;
    }
	
	/** Returns the method instance for the given template with the given
	 * type assignments. If no suitable method on this interface has already
	 * been instantiated, instantiate a new one.
	 */
	
	public Method instantiate(Node node, TypeListNode typeassignments)
				throws CompilationException
	{
		TypeContext tc = createTypeContext(node, typeassignments);
		String signature = tc.getSignature();
		signature += " ";
		signature += getNode().locationAsString();

		VirtualMethod method = _type.lookupVirtualMethod(signature);
		if (method != null)
			return method;

		/* Duplicate the function header (only). */
		
		getNode().visit(astCopyVisitor);
		FunctionHeaderNode ast = (FunctionHeaderNode) astCopyVisitor.getResult();
		
		ast.setParent(getNode().getParent());
		ast.setTypeContext(tc);
		
		method = new VirtualMethod(this, ast, tc, typeassignments);
		_type.addVirtualMethod(signature, method);
		
		return method;
	}
}
