package com.cowlark.cowbel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.cowbel.ast.nodes.IdentifierNode;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.instructions.ArrayConstructorInstruction;
import com.cowlark.cowbel.instructions.BooleanConstantInstruction;
import com.cowlark.cowbel.instructions.ConstructInstruction;
import com.cowlark.cowbel.instructions.DirectFunctionCallInstruction;
import com.cowlark.cowbel.instructions.DiscardInstruction;
import com.cowlark.cowbel.instructions.FunctionExitInstruction;
import com.cowlark.cowbel.instructions.GetLocalInstruction;
import com.cowlark.cowbel.instructions.GetUpvalueInstruction;
import com.cowlark.cowbel.instructions.GotoInstruction;
import com.cowlark.cowbel.instructions.IfInstruction;
import com.cowlark.cowbel.instructions.Instruction;
import com.cowlark.cowbel.instructions.InstructionVisitor;
import com.cowlark.cowbel.instructions.IntegerConstantInstruction;
import com.cowlark.cowbel.instructions.MethodCallInstruction;
import com.cowlark.cowbel.instructions.SetLocalInstruction;
import com.cowlark.cowbel.instructions.SetReturnValueInstruction;
import com.cowlark.cowbel.instructions.SetUpvalueInstruction;
import com.cowlark.cowbel.instructions.StringConstantInstruction;
import com.cowlark.cowbel.symbols.Function;
import com.cowlark.cowbel.symbols.Variable;

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
		return _function.getSymbolName().getText() + "." + _id;
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
	
	public void insnConstruct(Node node, Constructor constructor)
	{
		addInstruction(new ConstructInstruction(node, constructor));
	}
		
	public void insnSetLocal(Node node, Variable var)
	{
		addInstruction(new SetLocalInstruction(node, var));
	}
	
	public void insnSetUpvalue(Node node, Constructor c, Variable var)
	{
		addInstruction(new SetUpvalueInstruction(node, c, var));
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
	
	public void insnDirectFunctionCall(Node node, Function function, int args)
	{
		addInstruction(new DirectFunctionCallInstruction(node, function, args));
	}

	public void insnMethodCall(Node node, IdentifierNode method, int args)
	{
		addInstruction(new MethodCallInstruction(node, method, args));
	}

	public void insnGetLocal(Node node, Variable var)
	{
		addInstruction(new GetLocalInstruction(node, var));
	}
	
	public void insnGetUpvalue(Node node, Constructor c, Variable var)
	{
		addInstruction(new GetUpvalueInstruction(node, c, var));
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
