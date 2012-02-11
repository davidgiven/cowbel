/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.errors.CompilationException;

public class ExternType extends PrimitiveType
{
	private static ExternType _instance = new ExternType();
	
	public static ExternType create()
	{
		return _instance;
	}
	
	private ExternType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "__extern";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
	
	/* Special hole in the type rules: integers can be assigned to __extern. */
	
	@Override
	protected void unifyWithImpl(Node node, Type src)
	        throws CompilationException
	{
		if (src instanceof IntegerType)
			return;
		
		super.unifyWithImpl(node, src);
	}	
}
