/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.MethodTemplate;
import com.cowlark.cowbel.TypeContext;
import com.cowlark.cowbel.ast.AbstractTypeNode;
import com.cowlark.cowbel.ast.FunctionHeaderNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.ParameterDeclarationNode;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.types.Type;

public class VirtualMethod extends Method
{
	private MethodTemplate _template;
	private TypeListNode _typeassignments;
	
	public VirtualMethod(MethodTemplate template,
			FunctionHeaderNode header, TypeContext typecontext,
			TypeListNode typeassignments)
				throws CompilationException
    {
		_template = template;
		_typeassignments = typeassignments;

		List<Type> inputtypes = new Vector<Type>();
		for (Node n : header.getInputParametersNode())
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			AbstractTypeNode tn = pdn.getVariableTypeNode();
			inputtypes.add(tn.getType());
		}
		
		List<Type> outputtypes = new Vector<Type>();
		for (Node n : header.getOutputParametersNode())
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;
			AbstractTypeNode tn = pdn.getVariableTypeNode();
			outputtypes.add(tn.getType());
		}
		
		setInputTypes(inputtypes);
		setOutputTypes(outputtypes);
    }

	public MethodTemplate getTemplate()
    {
	    return _template;
    }
	
	public TypeListNode getTypeAssignments()
    {
	    return _typeassignments;
    }
	
	@Override
	public String getName()
	{
	    return _template.getSignature();
	}
	
	@Override
	public void visit(MethodCallInstruction insn, MethodVisitor visitor)
	{
		visitor.visit(insn, this);
	}
}
