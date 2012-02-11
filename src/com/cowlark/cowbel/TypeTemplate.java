/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.List;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.TypeParameterMismatch;
import com.cowlark.cowbel.interfaces.HasNode;
import com.cowlark.cowbel.types.Type;

public class TypeTemplate implements Comparable<TypeTemplate>, HasNode
{
	private static ASTCopyVisitor _ast_copy_visitor = new ASTCopyVisitor();
	private static DefineInterfacesVisitor _define_interfaces_visitor =
		new DefineInterfacesVisitor();
	
	private static TreeSet<Node> _typeNodesToScan = new TreeSet<Node>();
	
	private static int _globalid = 0;
	
	/** Fetch the next partially instantiated type. */
	
	public static Node getNextPendingTypeNode()
	{
		return _typeNodesToScan.pollFirst();
	}

	private int _id = _globalid++;
	private TypeContext _parentContext;
	private TypeAssignmentNode _ast;
	private String _signature;
	
	public TypeTemplate(TypeContext parentContext,
			TypeAssignmentNode ast)
	{
		_parentContext = parentContext;
		_ast = ast;
		
		StringBuilder sb = new StringBuilder();
		sb.append(_ast.getIdentifier().getText());
		sb.append("<");
		sb.append(_ast.getTypeVariables().getNumberOfChildren());
		sb.append(">");
		_signature = sb.toString();
	}
	
	@Override
    public TypeAssignmentNode getNode()
	{
		return _ast;
	}
	
	public String getSignature()
	{
		return _signature;
	}
	
	public String getName()
	{
		return _ast.getIdentifier().getText();
	}
	
	@Override
	public int compareTo(TypeTemplate other)
	{
		if (_id < other._id)
			return -1;
		if (_id > other._id)
			return 1;
		return 0;
	}

	public TypeContext createTypeContext(Node node, List<Type> types)
		throws CompilationException
	{
		if ((types == null) || (types.size() == 0))
			return _parentContext;
		
		TypeContext newcontext = new TypeContext(node, _parentContext);
		
		IdentifierListNode ids = _ast.getTypeVariables();
		if (ids.getNumberOfChildren() != types.size())
			throw new TypeParameterMismatch(node, _ast, ids, types);
		
		for (int i=0; i<ids.getNumberOfChildren(); i++)
		{
			IdentifierNode id = ids.getIdentifier(i);
			newcontext.addType(id, types.get(i));
		}
		
		return newcontext;
	}
	
	/** Returns the type instance for the given template with the given
	 * type assignments. Unlike symbol instantiation this *always* creates
	 * a new one.
	 */
	
	public Type instantiate(Node node, List<Type> typeassignments)
				throws CompilationException
	{
		TypeContext tc = createTypeContext(node, typeassignments);
		
		/* Deep-copy the AST tree so the version this type gets can be
		 * annotated without affecting any other instantiations. */
		
		getNode().visit(_ast_copy_visitor);
		TypeAssignmentNode ast = (TypeAssignmentNode) _ast_copy_visitor.getResult();
		
		ast.setParent(getNode().getParent());
		ast.setTypeContext(tc);

		ast.visit(_define_interfaces_visitor);
		
		return ast.getTypeNode().getType();
	}
	
}
