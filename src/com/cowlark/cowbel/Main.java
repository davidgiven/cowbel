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
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedParseException;
import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class Main
{
	public static String OutputFile;
	public static String Preprocessor = "cpp -undef -nostdinc ${includes} ${inputfile}";
	public static String CCompiler = "gcc -o ${outputfile} ${inputfile} -lgc ${options}";
	public static String Includes = "";
	public static String CompilerOptions = "";
	public static boolean Quiet;
	public static boolean EmitC;
	public static boolean DumpAST;
	public static boolean DumpIR;
	public static boolean DumpConstructors;
	public static boolean DumpInterfaces;
	
	private static void abort(String message)
	{
		System.err.println(message);
		System.exit(1);
	}
	
	private static void help(Options options)
	{
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("cowbel [<options>] <input files...>", options);
		System.err.println("");
		System.err.println("Preprocessor: " + Preprocessor);
		System.err.println("C compiler:   " + CCompiler);
		System.exit(0);
	}
	
	private static CommandLine parseCommandLine(String[] args) 
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
			DumpAST = cli.hasOption("da");
			DumpIR = cli.hasOption("di");
			DumpConstructors = cli.hasOption("dc");
			DumpInterfaces = cli.hasOption("dn");
			
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
	
	private static Backend createBackend(Compiler compiler, OutputStream os)
	{
		return new CBackend(compiler, os);
	}

	private static String expandCommandLine(String template, String... vars)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < vars.length; i += 2)
			map.put(vars[i], vars[i+1]);
		
		return StrSubstitutor.<String>replace(template, map, "${", "}");
	}

	private static String readSourceFile(String filename)
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
	
	private static void compileOutputFile(String inputfilename, String outputfilename)
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
	
	public static void main(String[] args)
	{
		CommandLine cli = parseCommandLine(args);
		args = cli.getArgs();
		
		if (args.length == 0)
			abort("You must specify at least one input filename.");
		if (args.length > 1)
			abort("Sorry, only one input filename is supported currently.");
		
		CompilerTimer timer = new CompilerTimer(Quiet);
		
		try
		{
			String filename = args[0];
			
			timer.onPreprocessBegin();
			String data = readSourceFile(filename);
			timer.onPreprocessEnd();
			
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

			Compiler c = new Compiler();
			Backend backend = createBackend(c, bos);
			c.setListener(timer);
			c.setInput(loc);
			c.setBackend(backend);
			c.compile();
			bos.close();
			
			if (DumpAST)
				c.dumpAnnotatedAST();
			if (DumpConstructors)
				c.dumpConstructors();
			if (DumpInterfaces)
				c.dumpInterfaces();
			if (DumpIR)
				c.dumpBasicBlocks();
			
			if (!EmitC)
			{
				timer.onCCompilationBegin();
				compileOutputFile(cfile, OutputFile);
				timer.onCCompilationEnd();
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
}
