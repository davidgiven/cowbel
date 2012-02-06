/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.Arrays;
import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MethodParameterMismatch;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.types.Type;

public abstract class Method implements Comparable<Method>
{
	private static int _globalid = 0;
	
	private int _id = _globalid++;
	private List<Type> _inputTypes;
	private List<Type> _outputTypes;
	private int _argumentTypeCount = 0;
	
	public Method()
    {
    }

	public int getId()
    {
	    return _id;
    }
	
	@Override
	public int compareTo(Method o)
	{
	    if (_id < o._id)
	    	return -1;
	    if (_id > o._id)
	    	return 1;
	    return 0;
	}
	
	public abstract String getName();
	
	protected void setInputTypes(Type... types)
	{
		_inputTypes = Arrays.asList(types);
	}

	protected void setInputTypes(List<Type> types)
	{
		_inputTypes = types;
	}
	
	public List<Type> getInputTypes()
	{
		return _inputTypes;
	}
	
	protected void setOutputTypes(Type... types)
	{
		_outputTypes = Arrays.asList(types);
	}

	protected void setOutputTypes(List<Type> types)
	{
		_outputTypes = types;
	}
	
	public List<Type> getOutputTypes()
	{
		return _outputTypes;
	}
	
	protected void setArgumentTypeCount(int argumentTypeCount)
    {
	    _argumentTypeCount = argumentTypeCount;
    }
	
	public int getArgumentTypeCount()
    {
	    return _argumentTypeCount;
    }
	
	private boolean typeCheckImpl(Node node,
				List<Type> methodtypes, List<Type> callertypes)
			throws CompilationException
	{
		if (callertypes.size() != methodtypes.size())
			return false;
		
		try
		{
			for (int i = 0; i < callertypes.size(); i++)
			{
				Type methodtype = methodtypes.get(i);
				Type callertype = callertypes.get(i);
				
				callertype.unifyWith(node, methodtype);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;
	}

	public void typeCheck(Node node,
			List<Type> outputtypes, List<Type> inputtypes)
		throws CompilationException
	{
		if (((outputtypes != null) && !typeCheckImpl(node, _outputTypes, outputtypes)) ||
			((inputtypes != null) && !typeCheckImpl(node, _inputTypes, inputtypes)))
		{
			throw new MethodParameterMismatch(node, this,
					_outputTypes, outputtypes, _inputTypes, inputtypes);
		}
	}
	
	public abstract void visit(MethodCallInstruction insn, MethodVisitor visitor);
}
