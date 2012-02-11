/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.BlockExpressionNode;
import com.cowlark.cowbel.ast.BlockScopeConstructorNode;
import com.cowlark.cowbel.ast.BooleanConstantNode;
import com.cowlark.cowbel.ast.BreakStatementNode;
import com.cowlark.cowbel.ast.ContinueStatementNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.DoWhileStatementNode;
import com.cowlark.cowbel.ast.DummyExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.ExpressionStatementNode;
import com.cowlark.cowbel.ast.ExternStatementNode;
import com.cowlark.cowbel.ast.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.FunctionScopeConstructorNode;
import com.cowlark.cowbel.ast.GotoStatementNode;
import com.cowlark.cowbel.ast.IdentifierListNode;
import com.cowlark.cowbel.ast.IfElseStatementNode;
import com.cowlark.cowbel.ast.IfStatementNode;
import com.cowlark.cowbel.ast.ImplementsStatementNode;
import com.cowlark.cowbel.ast.IntegerConstantNode;
import com.cowlark.cowbel.ast.LabelStatementNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.MethodCallStatementNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.ast.RealConstantNode;
import com.cowlark.cowbel.ast.ReturnStatementNode;
import com.cowlark.cowbel.ast.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.SimpleASTVisitor;
import com.cowlark.cowbel.ast.StringConstantNode;
import com.cowlark.cowbel.ast.TypeAssignmentNode;
import com.cowlark.cowbel.ast.VarAssignmentNode;
import com.cowlark.cowbel.ast.VarDeclarationNode;
import com.cowlark.cowbel.ast.VarReferenceNode;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.errors.BreakOrContinueNotInLoopException;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.ClassType;
import com.cowlark.cowbel.types.ExternType;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.IntegerType;
import com.cowlark.cowbel.types.InterfaceType;
import com.cowlark.cowbel.types.Type;

public class BasicBlockBuilderVisitor extends SimpleASTVisitor
{
	private Function _function;
	private BasicBlock _currentBB;
	private BasicBlock _continueBB;
	private BasicBlock _breakBB;
	private Variable _result;
	
	public BasicBlockBuilderVisitor(Function function)
    {
		_function = function;
		_currentBB = function.getEntryBB();
		_continueBB = null;
		_breakBB = null;
    }
	
	public BasicBlock getCurrentBasicBlock()
	{
		return _currentBB;
	}
	
	private Variable cast(Node node, Variable value, Type targettype)
	{
		Type srctype = value.getType().getConcreteType();
		targettype = targettype.getConcreteType();
		
		if (srctype.equals(targettype))
			return value;
		
		/* Special hole in the type rules: integers can be cast to __extern;
		 * but the extern is left uninitialised. */
		
		if ((targettype instanceof ExternType) && (srctype instanceof IntegerType))
			return _currentBB.createTemporary(node, targettype);

		assert(targettype instanceof InterfaceType);
		assert(srctype instanceof ClassType);
		
		_result = _currentBB.createTemporary(node, targettype);
		_currentBB.insnVarCopy(node, value, _result);
		return _result;
	}
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	private void visit(AbstractScopeConstructorNode node) throws CompilationException
	{
		AbstractScopeConstructorNode parent = node.getScope();
		if ((parent == null) || (node.getConstructor() != parent.getConstructor())) 
			_currentBB.insnConstruct(node, node.getConstructor());
		
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(FunctionScopeConstructorNode node)
	        throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	}
	
	@Override
	public void visit(BlockScopeConstructorNode node)
	        throws CompilationException
	{
		visit((AbstractScopeConstructorNode) node);
	}
	
	@Override
	public void visit(BlockExpressionNode node) throws CompilationException
	{
		BlockScopeConstructorNode block = node.getBlock();
		block.visit(this);
		
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnCreateObjectReference(node, block.getConstructor(),
				_result);
	}
	
	@Override
	public void visit(TypeAssignmentNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ImplementsStatementNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(VarDeclarationNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		IdentifierListNode iln = node.getVariables();
		ExpressionListNode eln = node.getExpressions();
		int size = eln.getNumberOfChildren();
		
		List<Variable> inputs = new Vector<Variable>();
		for (int i = 0; i < size; i++)
		{
			eln.getExpression(i).visit(this);
			Variable v = _currentBB.createTemporary(node, _result.getType());
			_currentBB.insnVarCopy(node, _result, v);
			inputs.add(v);
		}
		
		for (int i = 0; i < size; i++)
		{
			Variable var = (Variable) iln.getSymbol(i);
			_result = cast(node, inputs.get(i), var.getType());
			_currentBB.insnVarCopy(node, _result, var);
		}
	}
	
	@Override
	public void visit(ReturnStatementNode node) throws CompilationException
	{
		FunctionDefinitionNode fdn = (FunctionDefinitionNode) _function.getNode();
		node.getValue().visit(this);
		
		Variable var = (Variable) fdn.getFunctionHeader().getOutputParametersNode()
			.getParameter(0).getSymbol(); 
		_result = cast(node, _result, var.getType()); 
		_currentBB.insnVarCopy(node, _result, var);
		_currentBB.insnGoto(node, _function.getExitBB());
		_currentBB.terminate();
	}
	
	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		_currentBB.insnGoto(node, _function.getExitBB());
		_currentBB.terminate();
	}
	
	@Override
	public void visit(IfStatementNode node) throws CompilationException
	{
		BasicBlock conditionalbb = _currentBB;
		BasicBlock positivebb = new BasicBlock(_function);
		BasicBlock exitbb = new BasicBlock(_function);
		
		_currentBB = conditionalbb;
		node.getConditionalExpression().visit(this);
		_currentBB.insnIf(node, _result, positivebb, exitbb);
		_currentBB.terminate();
		
		_currentBB = positivebb;
		node.getPositiveStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		_currentBB.terminate();
		
		_currentBB = exitbb;
	}
	
	@Override
	public void visit(IfElseStatementNode node) throws CompilationException
	{
		BasicBlock conditionalbb = _currentBB;
		BasicBlock positivebb = new BasicBlock(_function);
		BasicBlock negativebb = new BasicBlock(_function);
		BasicBlock exitbb = new BasicBlock(_function);
		
		_currentBB = conditionalbb;
		node.getConditionalExpression().visit(this);
		_currentBB.insnIf(node, _result, positivebb, negativebb);
		_currentBB.terminate();
		
		_currentBB = positivebb;
		node.getPositiveStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		_currentBB.terminate();
		
		_currentBB = negativebb;
		node.getNegativeStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		_currentBB.terminate();
		
		_currentBB = exitbb;
	}
	
	@Override
	public void visit(WhileStatementNode node) throws CompilationException
	{
		BasicBlock oldcontinuebb = _continueBB;
		BasicBlock oldbreakbb = _breakBB;
		
		_continueBB = new BasicBlock(_function);
		_breakBB = new BasicBlock(_function);
		BasicBlock body = new BasicBlock(_function);
		
		_currentBB.insnGoto(node, _continueBB);
		_currentBB.terminate();
		
		_currentBB = _continueBB;
		node.getConditionalExpression().visit(this);
		_currentBB.insnIf(node, _result, body, _breakBB);
		_currentBB.terminate();
		
		_currentBB = body;
		node.getBodyStatement().visit(this);
		_currentBB.insnGoto(node, _continueBB);
		_currentBB.terminate();
		
		_currentBB = _breakBB;
		
		_continueBB = oldcontinuebb;
		_breakBB = oldbreakbb;
	}
	
	@Override
	public void visit(DoWhileStatementNode node) throws CompilationException
	{
		BasicBlock oldcontinuebb = _continueBB;
		BasicBlock oldbreakbb = _breakBB;
		
		_continueBB = new BasicBlock(_function);
		_breakBB = new BasicBlock(_function);
		
		_currentBB.insnGoto(node, _continueBB);
		_currentBB.terminate();
		
		_currentBB = _continueBB;
		node.getBodyStatement().visit(this);
		node.getConditionalExpression().visit(this);
		_currentBB.insnIf(node, _result, _continueBB, _breakBB);
		_currentBB.terminate();
		
		_currentBB = _breakBB;
		
		_continueBB = oldcontinuebb;
		_breakBB = oldbreakbb;
	}
	
	@Override
	public void visit(BreakStatementNode node) throws CompilationException
	{
		if (_breakBB == null)
			throw new BreakOrContinueNotInLoopException(node);
		
		_currentBB.insnGoto(node, _breakBB);
		_currentBB.terminate();
	}
	
	@Override
	public void visit(ContinueStatementNode node) throws CompilationException
	{
		if (_continueBB == null)
			throw new BreakOrContinueNotInLoopException(node);
		
		_currentBB.insnGoto(node, _continueBB);
		_currentBB.terminate();
	}
	
	@Override
	public void visit(LabelStatementNode node) throws CompilationException
	{
		Label label = node.getLabel();
		BasicBlock next = label.getBasicBlock(_function);
		
		_currentBB.insnGoto(node, next);
		_currentBB = next;
	}
	
	@Override
	public void visit(GotoStatementNode node) throws CompilationException
	{
		Label label = node.getLabel();
		BasicBlock next = label.getBasicBlock(_function);
		
		_currentBB.insnGoto(node, next);
		_currentBB.terminate();
	}
	
	@Override
	public void visit(ExternStatementNode node) throws CompilationException
	{
		List<Variable> variables = new Vector<Variable>();
		for (Node n : node)
		{
			VarReferenceNode varnode = (VarReferenceNode) n;
			variables.add((Variable) varnode.getSymbol());
		}
		
		_currentBB.insnExtern(node, node.getTemplate(), variables);
	}
	
	@Override
	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		node.getExpression().visit(this);
	}
	
	@Override
	public void visit(DirectFunctionCallStatementNode node)
	        throws CompilationException
	{
		ExpressionListNode arguments = node.getInputs();
		IdentifierListNode variables = node.getOutputs();
		int numinvars = arguments.getNumberOfChildren();
		int numoutvars = variables.getNumberOfChildren();
		Function function = node.getFunction();
		FunctionType functiontype = function.getType();
		List<Type> realintypes = functiontype.getInputArgumentTypes();
		List<Type> realouttypes = functiontype.getOutputArgumentTypes();
		int numrealouttypes = realouttypes.size();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		Vector<Variable> outvars = new Vector<Variable>(numoutvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			_result = cast(node, _result, realintypes.get(i));
			invars.add(_result);
		}

		for (int i = 0; i < numrealouttypes; i++)
		{
			Variable v = _currentBB.createTemporary(node, realouttypes.get(i));
			outvars.add(v);
		}
		
		/* Direct function call. */
		
		_currentBB.insnDirectFunctionCall(node, function, invars, outvars);
		
		/* Copy (and type convert) the output results into their target
		 * variables. */
		
		for (int i = 0; i < numrealouttypes; i++)
		{
			if (i < numoutvars)
			{
				Variable src = outvars.get(i);
				Variable dest = (Variable) variables.getSymbol(i);
				_currentBB.insnVarCopy(node, src, dest);
			}
		}
	}
	
	@Override
	public void visit(MethodCallStatementNode node)
	        throws CompilationException
	{
		Method method = node.getMethod();
		ExpressionListNode arguments = node.getInputs();
		IdentifierListNode variables = node.getOutputs();
		int numinvars = arguments.getNumberOfChildren();
		int numoutvars = variables.getNumberOfChildren();
		List<Type> realintypes = method.getInputTypes();
		List<Type> realouttypes = method.getOutputTypes();
		int numrealouttypes = realouttypes.size();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		Vector<Variable> outvars = new Vector<Variable>(numrealouttypes);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}

		for (int i = 0; i < numrealouttypes; i++)
		{
			Variable v = _currentBB.createTemporary(node, realouttypes.get(i));
			outvars.add(v);
		}
		
		node.getMethodReceiver().visit(this);
		Variable receiver = _result;
		
		_currentBB.insnMethodCall(node,
				node.getMethod(), receiver,
				invars, outvars);
		
		/* Copy (and type convert) the output results into their target
		 * variables. */
		
		for (int i = 0; i < numrealouttypes; i++)
		{
			if (i < numoutvars)
			{
				Variable src = outvars.get(i);
				Variable dest = (Variable) variables.getSymbol(i);
				_currentBB.insnVarCopy(node, src, dest);
			}
		}
	}
	
	@Override
	public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		ExpressionListNode arguments = node.getInputs();
		Function function = node.getFunction();
		int numinvars = arguments.getNumberOfChildren();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}
		
		/* Direct function call. */
		
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnDirectFunctionCall(node, function,
				invars, Collections.singletonList(_result));
	}
	
	@Override
	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		Method method = node.getMethod();
		ExpressionListNode arguments = node.getInputs();
		int numinvars = arguments.getNumberOfChildren();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}
		
		node.getMethodReceiver().visit(this);
		Variable receiver = _result;
		
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnMethodCall(node, method, receiver,
				invars, Collections.singletonList(_result));
		
		_result = cast(node, _result, method.getOutputTypes().get(0));
	}
	
	@Override
	public void visit(DummyExpressionNode node) throws CompilationException
	{
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		_result = (Variable) node.getSymbol();
	}
	
	@Override
	public void visit(BooleanConstantNode node) throws CompilationException
	{
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnBooleanConstant(node, node.getValue(), _result);
	}
	
	@Override
	public void visit(StringConstantNode node) throws CompilationException
	{
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnStringConstant(node, node.getValue(), _result);
	}
	
	@Override
	public void visit(IntegerConstantNode node) throws CompilationException
	{
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnIntegerConstant(node, node.getValue(), _result);
	}
	
	@Override
	public void visit(RealConstantNode node) throws CompilationException
	{
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnRealConstant(node, node.getValue(), _result);
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
