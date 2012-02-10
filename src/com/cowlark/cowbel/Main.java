/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import com.cowlark.cowbel.backend.Backend;
import com.cowlark.cowbel.backend.c.CBackend;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.FailedParseException;
import com.cowlark.cowbel.parser.core.FailedParse;
import com.cowlark.cowbel.parser.core.Location;

public class Main
{
	public static String OutputFile;
	public static String Preprocessor = "cpp -undef -nostdinc";
	public static boolean Quiet;
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
		System.err.println("\nPreprocessor: " + Preprocessor);
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
						"specifies preprocessor to use");
		
		options.addOption("I", "include", true,
						"adds include path");
		
		options.addOption("q", "quiet", false,
						"don't show timing information");
		
		options.addOption("da", "dump-ast", false,
						"dump AST to stdout");

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
			DumpAST = cli.hasOption("da");
			DumpIR = cli.hasOption("di");
			DumpConstructors = cli.hasOption("dc");
			DumpInterfaces = cli.hasOption("dn");
			
			if (cli.hasOption("p"))
				Preprocessor = cli.getOptionValue("p");
			
			String[] includes = cli.getOptionValues("I");
			if (includes != null)
				for (String s : includes)
					Preprocessor = Preprocessor + " -I " + s;
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
			Process preprocessor = Runtime.getRuntime().exec(Preprocessor+" "+filename);
			String data = IOUtils.toString(preprocessor.getInputStream());
			IOUtils.copy(preprocessor.getErrorStream(), System.err);
			int result = preprocessor.waitFor();
			timer.onPreprocessEnd();
			
			if (result != 0)
				System.exit(result);

			Location loc = new Location(data, filename);
			
			FileOutputStream fos = new FileOutputStream(OutputFile);
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
		catch (InterruptedException e)
		{
			System.err.println("Failed to invoke preprocessor");
			System.exit(1);
		}
	}
}
