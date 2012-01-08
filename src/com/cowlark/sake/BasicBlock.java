package com.cowlark.sake;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.instructions.BooleanConstantInstruction;
import com.cowlark.sake.instructions.DiscardInstruction;
import com.cowlark.sake.instructions.FunctionCallInstruction;
import com.cowlark.sake.instructions.FunctionExitInstruction;
import com.cowlark.sake.instructions.GetGlobalVariableInstruction;
import com.cowlark.sake.instructions.GetLocalVariableInstruction;
import com.cowlark.sake.instructions.GotoInstruction;
import com.cowlark.sake.instructions.IfInstruction;
import com.cowlark.sake.instructions.Instruction;
import com.cowlark.sake.instructions.InstructionVisitor;
import com.cowlark.sake.instructions.IntegerConstantInstruction;
import com.cowlark.sake.instructions.ArrayConstructorInstruction;
import com.cowlark.sake.instructions.MethodCallInstruction;
import com.cowlark.sake.instructions.SetGlobalVariableInstruction;
import com.cowlark.sake.instructions.SetLocalVariableInInstruction;
import com.cowlark.sake.instructions.SetReturnValueInstruction;
import com.cowlark.sake.instructions.StringConstantInstruction;
import com.cowlark.sake.symbols.Function;
import com.cowlark.sake.symbols.GlobalVariable;
import com.cowlark.sake.symbols.LocalVariable;

public class BasicBlock implements Comparable<BasicBlock>
{
	private static int _globalId = 0;

	private Function _function;
	private int _id = _globalId++;
	private ArrayList<Instruction> _instructions = new ArrayList<Instruction>();
	private TreeSet<BasicBlock> _sourceBlocks = new TreeSet<BasicBlock>();
	private TreeSet<BasicBlock> _destinationBlocks = new TreeSet<BasicBlock>();
	private TreeSet<LocalVariable> _definedVariables = new TreeSet<LocalVariable>();
	private TreeSet<LocalVariable> _inputVariables = new TreeSet<LocalVariable>();
	private TreeSet<LocalVariable> _outputVariables = new TreeSet<LocalVariable>();
	private boolean _terminated = false;
	
	public BasicBlock(Function function)
    {
		_function = function;
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
		return _function.getSymbolName().getText() + "." + _id;
	}
	
	private static void append_set(StringBuilder sb, Set<LocalVariable> set)
	{
		sb.append("{");
		
		boolean first = true;
		for (LocalVariable v : set)
		{
			if (!first)
				sb.append(", ");
			first = false;
			
			sb.append(v);
		}
		
		sb.append("}");
	}
	
	public String description()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("d=");
		append_set(sb, _definedVariables);
		sb.append(" i=");
		append_set(sb, _inputVariables);
		sb.append(" o=");
		append_set(sb, _outputVariables);
		
		return sb.toString();
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
	
	public Set<LocalVariable> getDefinedVariables()
	{
		return _definedVariables;
	}
	
	public Set<LocalVariable> getInputVariables()
	{
		return _inputVariables;
	}
	
	public Set<LocalVariable> getOutputVariables()
	{
		return _outputVariables;
	}
	
	private void jumpsTo(BasicBlock next)
	{
		_destinationBlocks.add(next);
		next._sourceBlocks.add(this);
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
	
	public void insnSetReturnValue(Node node)
	{
		addInstruction(new SetReturnValueInstruction(node));
	}
	
	public void insnSetLocalVariableIn(Node node, LocalVariable var, BasicBlock next)
	{
		addInstruction(new SetLocalVariableInInstruction(node, var, next));
		_definedVariables.add(var);
		jumpsTo(next);
	}
	
	public void insnSetGlobalVariable(Node node, GlobalVariable var)
	{
		addInstruction(new SetGlobalVariableInstruction(node, var));
	}
	
	public void insnGoto(Node node, BasicBlock target)
	{
		addInstruction(new GotoInstruction(node, target));
		jumpsTo(target);
	}
	
	public void insnIf(Node node, BasicBlock positive, BasicBlock negative)
	{
		addInstruction(new IfInstruction(node, positive, negative));
		jumpsTo(positive);
		jumpsTo(negative);
	}
	
	public void insnDiscard(Node node)
	{
		addInstruction(new DiscardInstruction(node));
	}
	
	public void insnFunctionCall(Node node, int args)
	{
		addInstruction(new FunctionCallInstruction(node, args));
	}

	public void insnMethodCall(Node node, IdentifierNode method, int args)
	{
		addInstruction(new MethodCallInstruction(node, method, args));
	}

	public void insnGetGlobalVariable(Node node, GlobalVariable var)
	{
		addInstruction(new GetGlobalVariableInstruction(node, var));
	}
	
	public void insnGetLocalVariable(Node node, LocalVariable var)
	{
		addInstruction(new GetLocalVariableInstruction(node, var));
	}
	
	public void insnListConstructor(Node node, int length)
	{
		addInstruction(new ArrayConstructorInstruction(node, length));
	}
	
	public void insnBooleanConstant(Node node, boolean value)
	{
		addInstruction(new BooleanConstantInstruction(node, value));
	}

	public void insnStringConstant(Node node, String value)
	{
		addInstruction(new StringConstantInstruction(node, value));
	}

	public void insnIntegerConstant(Node node, long value)
	{
		addInstruction(new IntegerConstantInstruction(node, value));
	}
}
