/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import java.util.Collection;
import java.util.Iterator;
import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationListNode;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.InvalidFunctionCallInExpressionContext;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasOutputs;
import com.cowlark.cowbel.interfaces.IsExpressionNode;
import com.cowlark.cowbel.interfaces.IsNode;

public class Utils
{
	/** Attach type constraints for a function or method input argument list.
	 */
	
	static boolean attachInputTypeConstraints(IsNode node,
			ParameterDeclarationListNode header, HasInputs call)
	{
		ExpressionListNode callnodes = call.getInputs();
		
		if (header.getNumberOfChildren() != callnodes.getNumberOfChildren())
			return false;
		
		/* call gets downcast to header. */
		for (int i = 0; i < header.getNumberOfChildren(); i++)
		{
			AbstractExpressionNode n = (AbstractExpressionNode) callnodes.getChild(i);
			TypeRef tr = header.getParameter(i).getTypeRef();
			tr.addParent(n.getTypeRef());
		}
		
		return true;
	}
	
	/** Attach type constraints for a function header argument list.
	 */
	
	static boolean attachInputTypeConstraints(IsNode node,
			ParameterDeclarationListNode child, ParameterDeclarationListNode parent)
	{
		if (child.getNumberOfChildren() != parent.getNumberOfChildren())
			return false;
		
		for (int i = 0; i < child.getNumberOfChildren(); i++)
		{
			ParameterDeclarationNode pdn = parent.getParameter(i);
			TypeRef tr = child.getParameter(i).getTypeRef();
			tr.addParent(pdn.getTypeRef());
		}
		
		return true;
	}
	
	/** Attach type constraints for a function or method input argument list.
	 */
	
	static boolean attachOutputTypeConstraints(IsNode node,
			ParameterDeclarationListNode header, HasOutputs call)
	{
		IdentifierListNode outputvars = call.getOutputs();

		if (header.getNumberOfChildren() < outputvars.getNumberOfChildren())
			return false;
		
		/* Header gets downcast to call. */
		for (int i = 0; i < outputvars.getNumberOfChildren(); i++)
		{
			outputvars.getSymbol(i).getTypeRef()
				.addParent(header.getParameter(i).getTypeRef());
		}
		
		return true;
	}
	
	/** Attach type constraints for a function header argument list.
	 */
	
	static boolean attachOutputTypeConstraints(IsNode node,
			ParameterDeclarationListNode callee, ParameterDeclarationListNode caller)
	{
		if (callee.getNumberOfChildren() < caller.getNumberOfChildren())
			return false;
		
		for (int i = 0; i < caller.getNumberOfChildren(); i++)
		{
			caller.getParameter(i).getTypeRef()
				.addParent(callee.getParameter(i).getTypeRef());
		}
		
		return true;
	}
	
	/** Attach type constraints for a function or method which returns a
	 * single value.
	 */
	
	static boolean attachSingleOutputTypeConstraint(IsExpressionNode node,
			ParameterDeclarationListNode header)
				throws CompilationException
	{
		if (header.getNumberOfChildren() != 1)
			throw new InvalidFunctionCallInExpressionContext(node);

		/* header gets downcast to call. */
		node.getTypeRef()
				.addParent(header.getParameter(0).getTypeRef());
		
		return true;
	}
	
	/** Perform a three-way comparison of two integers.
	 */
	
	public static int compare(int i1, int i2)
	{
		if (i1 < i2)
			return -1;
		if (i2 > i2)
			return 1;
		return 0;
	}
	
	/** Determine if all the objects in the collection are equal to each
	 * other. If the collection has one item, returns true.
	 */
	
	public static <T> boolean allElementsEqual(Collection<T> collection)
	{
		Iterator<T> iterator = collection.iterator();
		T item = iterator.next();
		
		while (iterator.hasNext())
		{
			T nextitem = iterator.next();
			if (!item.equals(nextitem))
				return false;
		}
		
		return true;
	}
}
