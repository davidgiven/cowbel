package com.cowlark.cowbel;

public class CompilerTimer implements CompilerListener
{
	private long _clock;
	
	private void reset()
	{
		_clock = System.currentTimeMillis();
	}
	
	private void report(String message)
	{
		long delta = System.currentTimeMillis() - _clock;
		
		System.err.println(message + ": " + delta + "ms");
	}
	
	@Override
	public void onParseBegin()
	{
		reset();
	}
	
	@Override
	public void onParseEnd()
	{
		report("Parsing");
	}
	
	@Override
	public void onSymbolTableAnalysisBegin()
	{
		reset();
	}
	
	@Override
	public void onSymbolTableAnalysisEnd()
	{
		report("Symbol tables");
	}
	
	@Override
	public void onTypeCheckBegin()
	{
		reset();
	}
	
	@Override
	public void onTypeCheckEnd()
	{
		report("Type checking");
	}
	
	@Override
	public void onBasicBlockAnalysisBegin()
	{
		reset();
	}
	
	@Override
	public void onBasicBlockAnalysisEnd()
	{
		report("Basic block analysis");
	}
	
	@Override
	public void onCodeGenerationBegin()
	{
		reset();
	}
	
	@Override
	public void onCodeGenerationEnd()
	{
		report("Code generation");
	}
	
	@Override
	public void onDataflowAnalysisBegin()
	{
		reset();
	}
	
	@Override
	public void onDataflowAnalysisEnd()
	{
		report("Dataflow analysis");
	}
}
