/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.interfaces;

import java.util.Iterator;
import com.cowlark.cowbel.ast.ASTVisitor;
import com.cowlark.cowbel.core.InterfaceContext;
import com.cowlark.cowbel.errors.CompilationException;

public interface IsNode extends Iterable<IsNode>, HasNode, HasScope
{
	public String locationAsString();

	public int getNumberOfChildren();
	public void setParent(IsNode parent);
	public IsNode getParent();
	public String getNodeName();
	public String getShortDescription();
	public InterfaceContext getTypeContext();
	public void setTypeContext(InterfaceContext typecontext);
	public boolean isLoopingNode();
	
	public void visit(ASTVisitor visitor) throws CompilationException;
	
    @Override
    public Iterator<IsNode> iterator();
}
