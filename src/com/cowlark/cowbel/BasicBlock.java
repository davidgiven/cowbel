/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.core.Constructor;
import com.cowlark.cowbel.core.Function;
import com.cowlark.cowbel.core.Method;
import com.cowlark.cowbel.core.TypeRef;
import com.cowlark.cowbel.instructions.BooleanConstantInstruction;
import com.cowlark.cowbel.instructions.ConstructInstruction;
import com.cowlark.cowbel.instructions.CreateObjectReferenceInstruction;
import com.cowlark.cowbel.instructions.DirectFunctionCallInstruction;
import com.cowlark.cowbel.instructions.ExternInstruction;
import com.cowlark.cowbel.instructions.FunctionExitInstruction;
import com.cowlark.cowbel.instructions.GotoInstruction;
import com.cowlark.cowbel.instructions.IfInstruction;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.InstructionVisitor;
import com.cowlark.cowbel.instructions.IntegerConstantInstruction;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.instructions.RealConstantInstruction;
import com.cowlark.cowbel.instructions.StringConstantInstruction;
import com.cowlark.cowbel.instructions.VarCopyInstruction;
import com.cowlark.cowbel.symbols.Variable;
import com.cowlark.cowbel.types.AbstractConcreteType;

public class BasicBlock implements Comparable<BasicBlock>
{
	private static int _globalId = 0;

	private Function _function;
	private int _id = _globalId++;
	private ArrayList<Instruction> _instructions = new ArrayList<Instruction>();
	private TreeSet<BasicBlock> _sourceBlocks = new TreeSet<BasicBlock>();
	private TreeSet<BasicBlock> _destinationBlocks = new TreeSet<BasicBlock>();
	private boolean _terminated = false;
	
	public BasicBlock(Function function)
    {
		_function = function;
    }
	
	public Function getFunction()
	{
		return _function;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Override
	public int compareTo(BasicBlock other)
	{
		if (_id < other._id)
			return -1;
		if (_id > other._id)
			return 1;
		return 0;
	}
	
	public String getName()
	{
		return _function.getNode().getFunctionName().getText()
			+ "." + _id;
	}
	
	public String description()
	{
		return "";
	}
	
	public List<Instruction> getInstructions()
	{
		return _instructions;
	}
	
	public Set<BasicBlock> getSourceBlocks()
	{
		return _sourceBlocks;
	}
	
	public Set<BasicBlock> getDestinationBlocks()
	{
		return _destinationBlocks;
	}
	
	private void jumpsTo(BasicBlock next)
	{
		_destinationBlocks.add(next);
		next._sourceBlocks.add(this);
	}
	
	protected Variable createTemporary(Node node, AbstractConcreteType ctype)
	{
		Constructor constructor = node.getScope().getConstructor();
		TypeRef tr = new TypeRef(node);
		tr.setConcreteType(ctype);
		Variable var = new Variable(node,
				IdentifierNode.createInternalIdentifier("temp"),
				tr);
		var.setScope(node.getScope());
		constructor.addVariable(var);
		return var;
	}
	
	public void terminate()
	{
		_terminated = true;
	}
	
	private void addInstruction(Instruction insn)
	{
		if (!_terminated)
			_instructions.add(insn);
	}
	
	public void visit(InstructionVisitor visitor)
	{
		visitor.visit(this);
		for (Instruction insn : _instructions)
			insn.visit(visitor);
	}
	
	public void insnFunctionExit(Node node)
	{
		addInstruction(new FunctionExitInstruction(node));
	}
	
	public void insnConstruct(Node node, Constructor constructor)
	{
		addInstruction(new ConstructInstruction(node, constructor));
	}
		
	public void insnGoto(Node node, BasicBlock target)
	{
		addInstruction(new GotoInstruction(node, target));
		jumpsTo(target);
	}
	
	public void insnIf(Node node, Variable condition,
			BasicBlock positive, BasicBlock negative)
	{
		addInstruction(new IfInstruction(node, condition, positive, negative));
		jumpsTo(positive);
		jumpsTo(negative);
	}
	
	public void insnDirectFunctionCall(Node node, Function function,
			List<Variable> inargs, List<Variable> outargs)
	{
		addInstruction(new DirectFunctionCallInstruction(node, function,
				inargs, outargs));
	}

	public void insnMethodCall(Node node, Method method,
			Variable receiver, List<Variable> inargs, List<Variable> outargs)
	{
		addInstruction(new MethodCallInstruction(node, method, receiver,
				inargs, outargs));
	}

	public void insnBooleanConstant(Node node, boolean value, Variable var)
	{
		addInstruction(new BooleanConstantInstruction(node, value, var));
	}

	public void insnStringConstant(Node node, String value, Variable var)
	{
		addInstruction(new StringConstantInstruction(node, value, var));
	}

	public void insnIntegerConstant(Node node, long value, Variable var)
	{
		addInstruction(new IntegerConstantInstruction(node, value, var));
	}
	
	public void insnRealConstant(Node node, double value, Variable var)
	{
		addInstruction(new RealConstantInstruction(node, value, var));
	}
	
	public void insnVarCopy(Node node, Variable invar, Variable outvar)
	{
		addInstruction(new VarCopyInstruction(node, invar, outvar));
	}
	
	public void insnCreateObjectReference(Node node, Constructor constructor,
			Variable outvar)
	{
		addInstruction(new CreateObjectReferenceInstruction(node,
				constructor, outvar));
	}
	
	public void insnExtern(Node node, String template, List<Variable> vars)
	{
		addInstruction(new ExternInstruction(node, template, vars));
	}
}
