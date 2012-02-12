/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

public class CompilerTimer implements CompilerListener
{
	private long _clock;
	private boolean _quiet;
	
	public CompilerTimer(boolean quiet)
    {
		_quiet = quiet;
    }
	
	private void reset()
	{
		_clock = System.currentTimeMillis();
	}
	
	private void report(String message)
	{
		long delta = System.currentTimeMillis() - _clock;
	
		if (!_quiet)
			System.err.println(message + ": " + delta + "ms");
	}
	
	public void onPreprocessBegin()
	{
		reset();
	}
	
	public void onPreprocessEnd()
	{
		report("Preprocessing");
	}
	
	public void onCCompilationBegin()
	{
		reset();
	}
	
	public void onCCompilationEnd()
	{
		report("C compilation");
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
}
