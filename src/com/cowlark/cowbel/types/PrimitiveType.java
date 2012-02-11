/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.NoSuchMethodException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.interfaces.HasInputs;
import com.cowlark.cowbel.interfaces.HasTypeArguments;
import com.cowlark.cowbel.interfaces.IsMethod;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.methods.PrimitiveMethod;

public abstract class PrimitiveType extends Type
{
	@Override
	protected void unifyWithImpl(Node node, Type src)
	        throws CompilationException
	{
		if (!(getClass().equals(src.getClass())))
			throw new TypesNotCompatibleException(node, this, src);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		String signature = getCanonicalTypeName() + "." + id.getText() +
			"." + node.getInputs().getNumberOfChildren();
		
		Method method = PrimitiveMethod.lookupPrimitiveMethod(signature);
		if (method == null)
			throw new NoSuchMethodException(node, this, id);
		return method;
	}
}
