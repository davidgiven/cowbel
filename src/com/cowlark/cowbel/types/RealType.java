/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

public class RealType extends PrimitiveType
{
	private static RealType _instance = new RealType();
	
	public static RealType create()
	{
		return _instance;
	}
	
	private RealType()
    {
    }
	
	@Override
	public String getCanonicalTypeName()
	{
	    return "real";
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
