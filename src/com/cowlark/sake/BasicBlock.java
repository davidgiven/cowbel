package com.cowlark.sake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.instructions.DiscardInstruction;
import com.cowlark.sake.instructions.FunctionCallInstruction;
import com.cowlark.sake.instructions.GetGlobalVariableInstruction;
import com.cowlark.sake.instructions.GetLocalVariableInstruction;
import com.cowlark.sake.instructions.GotoInstruction;
import com.cowlark.sake.instructions.IfInstruction;
import com.cowlark.sake.instructions.Instruction;
import com.cowlark.sake.instructions.ListConstructorInstruction;
import com.cowlark.sake.instructions.MethodCallInstruction;
import com.cowlark.sake.instructions.SetGlobalVariableInstruction;
import com.cowlark.sake.instructions.SetLocalVariableInInstruction;
import com.cowlark.sake.instructions.SetReturnValueInstruction;
import com.cowlark.sake.instructions.StringConstantInstruction;

public class BasicBlock
{
	private static int _globalId = 0;
	
	private int _id;
	private ArrayList<Instruction> _instructions = new ArrayList<Instruction>();
	private HashSet<BasicBlock> _blocks = new HashSet<BasicBlock>();
	
	public BasicBlock()
    {
		_id = _globalId++;
    }
	
	@Override
	public String toString()
	{
		return "BasicBlock_" + _id;
	}
	
	public List<Instruction> getInstructions()
	{
		return _instructions;
	}
	
	public Set<BasicBlock> getReferencedBlocks()
	{
		return _blocks;
	}
	
	private void addInstruction(Instruction insn)
	{
		_instructions.add(insn);
	}
	
	public void insnSetReturnValue(Node node)
	{
		addInstruction(new SetReturnValueInstruction(node));
	}
	
	public void insnSetLocalVariableIn(Node node, LocalVariable var, BasicBlock next)
	{
		addInstruction(new SetLocalVariableInInstruction(node, var, next));
		_blocks.add(next);
	}
	
	public void insnSetGlobalVariable(Node node, GlobalVariable var)
	{
		addInstruction(new SetGlobalVariableInstruction(node, var));
	}
	
	public void insnGoto(Node node, BasicBlock target)
	{
		addInstruction(new GotoInstruction(node, target));
		_blocks.add(target);
	}
	
	public void insnIf(Node node, BasicBlock positive, BasicBlock negative)
	{
		addInstruction(new IfInstruction(node, positive, negative));
		_blocks.add(positive);
		_blocks.add(negative);
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
		addInstruction(new ListConstructorInstruction(node, length));
	}
	
	public void insnStringConstant(Node node, String value)
	{
		addInstruction(new StringConstantInstruction(node, value));
	}
}
