/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast;

import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public abstract class AbstractExpressionLiteralNode extends AbstractExpressionNode
{
	private String _implName;
	
	public AbstractExpressionLiteralNode(Location start, Location end, String impl)
    {
        super(start, end);
        _implName = impl;
    }
	
	@Override
	public void visit(ASTVisitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
		
	@Override
	public TypeRef getTypeRef()
	{
//		if (!hasTypeRef())
//		{
//			TypeRef tr = super.getTypeRef();
//			tr.addParent(Compiler.Instance.getCanonicalPrimitiveTypeRef(_implName));
//		}
		return super.getTypeRef();
	}

}