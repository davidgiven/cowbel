/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.types.Type;

public class Utils
{
	/* permissive: allow there to be fewer values in the call list than in the
	 * function list. */
	
	static boolean unifyTypeLists(Node node,
			List<Type> funclist, List<Type> calllist, boolean permissive) throws CompilationException
	{
		if (!permissive && (funclist.size() != calllist.size()))
			return false;
		else if (funclist.size() < calllist.size())
			return false;
		
		try
		{
			for (int i = 0; i < calllist.size(); i++)
			{
				Type t1 = funclist.get(i);
				Type t2 = calllist.get(i);
				t1.unifyWith(node, t2);
				t2.ensureConcrete(node);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;	
	}
}
