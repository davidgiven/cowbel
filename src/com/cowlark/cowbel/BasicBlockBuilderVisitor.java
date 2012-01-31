/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import com.cowlark.cowbel.ast.SimpleVisitor;
import com.cowlark.cowbel.ast.nodes.ArrayConstructorNode;
import com.cowlark.cowbel.ast.nodes.BooleanConstantNode;
import com.cowlark.cowbel.ast.nodes.BreakStatementNode;
import com.cowlark.cowbel.ast.nodes.ContinueStatementNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.DirectFunctionCallStatementNode;
import com.cowlark.cowbel.ast.nodes.DoWhileStatementNode;
import com.cowlark.cowbel.ast.nodes.DummyExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionListNode;
import com.cowlark.cowbel.ast.nodes.ExpressionNode;
import com.cowlark.cowbel.ast.nodes.ExpressionStatementNode;
import com.cowlark.cowbel.ast.nodes.FunctionDefinitionNode;
import com.cowlark.cowbel.ast.nodes.GotoStatementNode;
import com.cowlark.cowbel.ast.nodes.IdentifierListNode;
import com.cowlark.cowbel.ast.nodes.IfElseStatementNode;
import com.cowlark.cowbel.ast.nodes.IfStatementNode;
import com.cowlark.cowbel.ast.nodes.IntegerConstantNode;
import com.cowlark.cowbel.ast.nodes.LabelStatementNode;
import com.cowlark.cowbel.ast.nodes.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.nodes.MethodCallStatementNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.ast.nodes.ReturnStatementNode;
import com.cowlark.cowbel.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.ast.nodes.StringConstantNode;
import com.cowlark.cowbel.ast.nodes.VarAssignmentNode;
import com.cowlark.cowbel.ast.nodes.VarDeclarationNode;
import com.cowlark.cowbel.ast.nodes.VarReferenceNode;
import com.cowlark.cowbel.ast.nodes.WhileStatementNode;
import com.cowlark.cowbel.errors.BreakOrContinueNotInLoopException;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Symbol;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.FunctionType;
import com.cowlark.cowbel.types.Type;

public class BasicBlockBuilderVisitor extends SimpleVisitor
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
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		ScopeConstructorNode parent = node.getScope();
		if ((parent == null) || (node.getConstructor() != parent.getConstructor())) 
			_currentBB.insnConstruct(node, node.getConstructor());
		
		node.getChild().visit(this);
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
		
		for (int i = 0; i < size; i++)
		{
			eln.getExpression(i).visit(this);
			_currentBB.insnVarCopy(node, _result, (Variable) iln.getSymbol(i));
		}
	}
	
	@Override
	public void visit(ReturnStatementNode node) throws CompilationException
	{
		FunctionDefinitionNode fdn = (FunctionDefinitionNode) _function.getNode();
		node.getValue().visit(this);
		
		_currentBB.insnVarCopy(node, _result,
				(Variable) fdn.getFunctionHeader().getOutputParametersNode()
					.getParameter(0).getSymbol());
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
		_currentBB.insnIf(node, _result, _continueBB, _breakBB);
		node.getBodyStatement().visit(this);
		node.getConditionalExpression().visit(this);
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
	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		node.getExpression().visit(this);
	}
	
	@Override
	public void visit(DirectFunctionCallStatementNode node)
	        throws CompilationException
	{
		ExpressionListNode arguments = node.getArguments();
		IdentifierListNode variables = node.getVariables();
		int numinvars = arguments.getNumberOfChildren();
		int numoutvars = variables.getNumberOfChildren();
		Function function = (Function) node.getSymbol();
		FunctionType functiontype = (FunctionType) function.getSymbolType();
		List<Type> realouttypes = functiontype.getOutputArgumentTypes();
		int numrealouttypes = realouttypes.size();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		Vector<Variable> outvars = new Vector<Variable>(numoutvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}

		for (int i = 0; i < numrealouttypes; i++)
		{
			Variable v;
			if (i < numoutvars)
				v = (Variable) variables.getSymbol(i);
			else
				v = _currentBB.createTemporary(node, realouttypes.get(i));
			outvars.add(v);
		}
		
		/* Direct function call. */
		
		_currentBB.insnDirectFunctionCall(node, function, invars, outvars);
	}
	
	@Override
	public void visit(MethodCallStatementNode node)
	        throws CompilationException
	{
		Method method = node.getMethod();
		ExpressionListNode arguments = node.getArguments();
		IdentifierListNode variables = node.getVariables();
		int numinvars = arguments.getNumberOfChildren();
		int numoutvars = variables.getNumberOfChildren();
		List<Type> realouttypes = method.getOutputTypes();
		int numrealoutvars = realouttypes.size();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		Vector<Variable> outvars = new Vector<Variable>(numrealoutvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}

		for (int i = 0; i < numrealoutvars; i++)
		{
			Variable v;
			if (i < numoutvars)
				v = (Variable) variables.getSymbol(i);
			else
				v = _currentBB.createTemporary(node, realouttypes.get(i));
			outvars.add(v);
		}
		
		node.getMethodReceiver().visit(this);
		Variable receiver = _result;
		
		_currentBB.insnMethodCall(node,
				node.getMethod(), receiver,
				invars, outvars);
	}
	
	@Override
	public void visit(DirectFunctionCallExpressionNode node) throws CompilationException
	{
		ExpressionListNode arguments = node.getArguments();
		Symbol symbol = node.getSymbol();
		int numinvars = arguments.getNumberOfChildren();
		Vector<Variable> invars = new Vector<Variable>(numinvars);
		
		for (int i = 0; i < numinvars; i++)
		{
			arguments.getExpression(i).visit(this);
			invars.add(_result);
		}
		
		if (symbol instanceof Function)
		{
			/* Direct function call. */
			
			_result = _currentBB.createTemporary(node, node.getType());
			_currentBB.insnDirectFunctionCall(node, (Function) symbol,
					invars, Collections.singletonList(_result));
		}
		else
		{
			/* Indirect function call. */
			
			assert(false);
			/*
			_currentBB.insnIndirectFunctionCall(node, arguments.size());

			Variable var = (Variable) smybol;
			Constructor constructor = var.getConstructor();
			
			if (constructor.isStackVariable(var))
				_currentBB.insnGetUpvalue(node, constructor, var);
			else
				_currentBB.insnGetLocal(node, var);
				*/
		}
	}
	
	@Override
	public void visit(MethodCallExpressionNode node) throws CompilationException
	{
		ExpressionListNode arguments = node.getArguments();
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
		_currentBB.insnMethodCall(node,
				node.getMethod(), receiver,
				invars, Collections.singletonList(_result));
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
	public void visit(ArrayConstructorNode node) throws CompilationException
	{
		List<ExpressionNode> members = node.getListMembers();
		Vector<Variable> values = new Vector<Variable>();
		
		for (ExpressionNode e : members)
		{
			e.visit(this);
			values.add(_result);
		}
		
		_result = _currentBB.createTemporary(node, node.getType());
		_currentBB.insnListConstructor(node, values, _result);
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
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}
