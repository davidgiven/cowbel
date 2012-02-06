/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.types;

import java.util.LinkedList;
import java.util.List;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.ast.IsMethod;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.NoSuchMethodException;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.methods.Method;

public class FunctionType extends PrimitiveType
{
	public static FunctionType create(List<Type> inputarguments,
			List<Type> outputarguments)
	{
		return new FunctionType(inputarguments, outputarguments);
	}
	
	public static FunctionType createVoidVoid()
	{
		List<Type> emptylist = new LinkedList<Type>();
		return create(emptylist, emptylist);
	}
	
	private List<Type> _inputarguments;
	private List<Type> _outputarguments;
	
	private FunctionType(List<Type> inputarguments, List<Type> outputarguments)
    {
		_inputarguments = inputarguments;
		_outputarguments = outputarguments;
    }
	
	private void emit_type_list(StringBuilder sb, List<Type> list)
	{
		boolean first = true;
		for (Type t : list)
		{
			if (!first)
				sb.append(", ");
			sb.append(t.getCanonicalTypeName());
			first = false;
		}
	}
	
	@Override
	public String getCanonicalTypeName()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append('(');
		emit_type_list(sb, _inputarguments);
		sb.append(" -> ");
		emit_type_list(sb, _outputarguments);
		sb.append(')');

		return sb.toString();
	}
	
	public List<Type> getInputArgumentTypes()
	{
		return _inputarguments;
	}
	
	public List<Type> getOutputArgumentTypes()
	{
		return _outputarguments;
	}
	
	@Override
	protected void unifyWithImpl(Node node, Type other)
	        throws CompilationException
	{
	    super.unifyWithImpl(node, other);
	    
	    if (!(getCanonicalTypeName().equals(other.getCanonicalTypeName())))
	    	throw new TypesNotCompatibleException(node, this, other);
	}
	
	@Override
	public <T extends Node & IsMethod & HasInputs & HasTypeArguments>
		Method lookupMethod(
	        T node, IdentifierNode id) throws CompilationException
	{
		throw new NoSuchMethodException(node, this, id);
	}
	
	@Override
	public void visit(TypeVisitor visitor)
	{
	    visitor.visit(this);	    
	}
}
