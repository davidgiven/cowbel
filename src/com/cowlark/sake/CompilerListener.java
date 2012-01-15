package com.cowlark.sake;

public interface CompilerListener
{
	public void onParseBegin();
	public void onParseEnd();
	
	public void onSymbolTableAnalysisBegin();
	public void onSymbolTableAnalysisEnd();
	
	public void onTypeCheckBegin();
	public void onTypeCheckEnd();
	
	public void onBasicBlockAnalysisBegin();
	public void onBasicBlockAnalysisEnd();
	
	public void onDataflowAnalysisBegin();
	public void onDataflowAnalysisEnd();
	
	public void onCodeGenerationBegin();
	public void onCodeGenerationEnd();
}
