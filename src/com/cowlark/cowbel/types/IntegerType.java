/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public class IntegerType extends PrimitiveType
{
	private static IntegerType _instance =
		Type.canonicalise(new IntegerType());
	
	public static IntegerType create()
	{
		return _instance;
	}
	
	private IntegerType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "integer";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
