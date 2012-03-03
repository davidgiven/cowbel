/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.backend.c.CBackend;
import com.cowlark.cowbel.core.Compiler;
import com.cowlark.cowbel.core.CompilerListener;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedParseException;
import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class Main implements CompilerListener
{
	public String OutputFile;
	public String Preprocessor = "cpp -undef -nostdinc ${includes} ${inputfile}";
	public String CCompiler = "gcc -o ${outputfile} ${inputfile} -lgc ${options}";
	public String Includes = "";
	public String CompilerOptions = "";
	public boolean Quiet;
	public boolean EmitC;
	public boolean DumpAST;
	public String DumpTypeRef;
	public boolean DumpIR;
	public boolean DumpConstructors;
	public boolean DumpConcreteTypes;
	private long _clock;
	private Compiler _compiler;
	
	private void abort(String message)
	{
		System.err.println(message);
		System.exit(1);
	}
	
	private void help(Options options)
	{
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("cowbel [<options>] <input files...>", options);
		System.err.println("");
		System.err.println("Preprocessor: " + Preprocessor);
		System.err.println("C compiler:  " + CCompiler);
		System.exit(0);
	}
	
	private CommandLine parseCommandLine(String[] args) 
	{
		CommandLineParser parser = new GnuParser();
		Options options = new Options();
		
		options.addOption("h", "help", false,
						"shows this message");
		
		options.addOption("o", "output", true,
						"sets output file");
		
		options.addOption("p", "preprocessor", true,
						"specifies preprocessor command line to use");
		
		options.addOption("c", "compiler", true,
						"specifies C compiler command line to use");

		options.addOption("I", "include", true,
						"adds include path");
		
		options.addOption("q", "quiet", false,
						"don't show timing information");
		
		options.addOption("C", "emit-c", false,
						"don't try to compile the C file, just emit it");

		options.addOption("X", "coption", true,
						"add option to C compiler command line");
		
		options.addOption("da", "dump-annotated-ast", false,
						"dump annotated AST to stdout");

		options.addOption("dt", "dump-typeref", true,
						"dump typeref graph to named file");

		options.addOption("di", "dump-ir", false,
						"dump IR code to stdout");

		options.addOption("dc", "dump-constructors", false,
						"dump constructors to stdout");
		
		options.addOption("dn", "dump-interfaces", false,
						"dump interfaces to stdout");
		
		CommandLine cli = null;
		try
		{
			if (args.length == 0)
				help(options);
			
			cli = parser.parse(options, args);
			
			if (cli.hasOption('h'))
				help(options);

			OutputFile = cli.getOptionValue('o');
			if (OutputFile == null)
				throw new ParseException("You must specify an output filename.");
			
			Quiet = cli.hasOption("q");
			EmitC = cli.hasOption("C");
			DumpTypeRef = cli.getOptionValue("dt");
			DumpAST = cli.hasOption("da");
			DumpIR = cli.hasOption("di");
			DumpConstructors = cli.hasOption("dc");
			DumpConcreteTypes = cli.hasOption("dn");
			
			if (cli.hasOption("p"))
				Preprocessor = cli.getOptionValue("p");
			if (cli.hasOption("c"))
				CCompiler = cli.getOptionValue("c");
			
			String[] includes = cli.getOptionValues("I");
			if (includes != null)
				for (String s : includes)
					Includes = Includes + " -I " + s;
			
			String[] coptions = cli.getOptionValues("X");
			if (coptions != null)
				for (String s : coptions)
					CompilerOptions = CompilerOptions + " " + s;
		}
		catch (ParseException e)
		{
			abort(e.getMessage());
		}

		return cli;
	}
	
	private Backend createBackend(Compiler compiler, OutputStream os)
	{
		return new CBackend(compiler, os);
	}

	private String expandCommandLine(String template, String... vars)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < vars.length; i += 2)
			map.put(vars[i], vars[i+1]);
		
		return StrSubstitutor.<String>replace(template, map, "${", "}");
	}

	private String readSourceFile(String filename)
	{
		String cli = expandCommandLine(Preprocessor,
				"inputfile", filename,
				"includes", Includes);
		
		try
		{
			Process preprocessor = Runtime.getRuntime().exec(cli);
			int result = preprocessor.waitFor();
			String data = IOUtils.toString(preprocessor.getInputStream());
			IOUtils.copy(preprocessor.getErrorStream(), System.err);
			
			if (result != 0)
				abort("preprocessor failed");
			
			return data;
		}
		catch (IOException e)
		{
			abort("preprocessor failed");
			throw null;
		}
		catch (InterruptedException e)
		{
			abort("preprocessor failed");
			throw null;
		}
	}
	
	private void compileOutputFile(String inputfilename, String outputfilename)
	{
		String cli = expandCommandLine(CCompiler,
				"inputfile", inputfilename,
				"outputfile", outputfilename,
				"options", CompilerOptions);
		
		try
		{
			Process preprocessor = Runtime.getRuntime().exec(cli);
			int result = preprocessor.waitFor();
			IOUtils.copy(preprocessor.getErrorStream(), System.err);
			
			if (result != 0)
				abort("compilation failed");
		}
		catch (IOException e)
		{
			abort("compilation failed");
			throw null;
		}
		catch (InterruptedException e)
		{
			abort("compilation failed");
			throw null;
		}
	}
	
	private void resetTimer()
	{
		_clock = System.currentTimeMillis();
	}
	
	private void reportTimer(String message)
	{
		long delta = System.currentTimeMillis() - _clock;
	
		if (!Quiet)
			System.err.println(message + ": " + delta + "ms");
	}
	
	public void onPreprocessBegin()
	{
		resetTimer();
	}
	
	public void onPreprocessEnd()
	{
		reportTimer("Preprocessing");
	}
	
	public void onCCompilationBegin()
	{
		resetTimer();
	}
	
	public void onCCompilationEnd()
	{
		reportTimer("C compilation");
	}
	
	@Override
	public void onParseBegin()
	{
		resetTimer();
	}
	
	@Override
	public void onParseEnd()
	{
		reportTimer("Parsing");
	}
	
	@Override
	public void onSymbolTableAnalysisBegin()
	{
		resetTimer();
	}
	
	@Override
	public void onSymbolTableAnalysisEnd()
	{
		reportTimer("Symbol tables");
		
		if (DumpAST)
			_compiler.dumpAnnotatedAST();
	}
	
	@Override
	public void onTypeInferenceBegin()
	{
		resetTimer();
	}
	
	@Override
	public void onTypeInferenceEnd()
	{
		reportTimer("Type inference");
		
		if (DumpTypeRef != null)
		{
			try
			{
				FileOutputStream fos = new FileOutputStream(DumpTypeRef);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				PrintWriter pw = new PrintWriter(bos);
				_compiler.dumpTypeRefGraph(pw);
				pw.flush();
				fos.close();
			}
			catch (IOException e)
			{
				System.err.println("I/O error: "+e.getMessage());
				System.exit(1);
			}
		}
		
		if (DumpConcreteTypes)
		{
			_compiler.dumpConcreteTypes();
		}
	}
	
	@Override
	public void onBasicBlockAnalysisBegin()
	{
		resetTimer();
	}
	
	@Override
	public void onBasicBlockAnalysisEnd()
	{
		reportTimer("Basic block analysis");
	}
	
	@Override
	public void onCodeGenerationBegin()
	{
		resetTimer();
	}
	
	@Override
	public void onCodeGenerationEnd()
	{
		reportTimer("Code generation");
	}

	public void instanceMain(String[] args)
	{
		CommandLine cli = parseCommandLine(args);
		args = cli.getArgs();
		
		if (args.length == 0)
			abort("You must specify at least one input filename.");
		if (args.length > 1)
			abort("Sorry, only one input filename is supported currently.");
		
		try
		{
			String filename = args[0];
			
			onPreprocessBegin();
			String data = readSourceFile(filename);
			onPreprocessEnd();
			
			Location loc = new Location(data, filename);
			
			String cfile;
			if (EmitC)
				cfile = OutputFile;
			else
			{
				File f = File.createTempFile("cowbel", ".c");
				f.deleteOnExit();
				cfile = f.getAbsolutePath();
			}
			
			FileOutputStream fos = new FileOutputStream(cfile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			//backend.prologue();

			_compiler = new Compiler();
			Backend backend = createBackend(_compiler, bos);
			_compiler.setListener(this);
			_compiler.setInput(loc);
			_compiler.setBackend(backend);
			_compiler.compile();
			bos.close();
			
			if (DumpConstructors)
				_compiler.dumpConstructors();
			if (DumpIR)
				_compiler.dumpBasicBlocks();
			
			if (!EmitC)
			{
				onCCompilationBegin();
				compileOutputFile(cfile, OutputFile);
				onCCompilationEnd();
			}
			
			System.exit(0);
		}
		catch (FailedParseException e)
		{
			FailedParse fp = e.getFailedParse();
			
			System.out.println("Parse failed:");
			System.out.println(fp.toString());
			System.exit(1);
		}
		catch (CompilationException e)
		{
			System.out.println("Compilation failed:");
			System.out.println(e.toString());
			System.exit(1);
		}
		catch (IOException e)
		{
			System.err.println("I/O error: "+e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] argv)
	{
		new Main().instanceMain(argv);
	}
}
