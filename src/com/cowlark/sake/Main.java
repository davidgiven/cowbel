package com.cowlark.sake;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import com.cowlark.sake.backend.Backend;
import com.cowlark.sake.backend.make.MakeBackend;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedParseException;
import com.cowlark.sake.parser.core.FailedParse;
import com.cowlark.sake.parser.core.Location;

public class Main
{
	public static String OutputFile;
	public static String Backend;
	public static boolean DumpAST;
	public static boolean DumpIR;
	
	private static void abort(String message)
	{
		System.err.println(message);
		System.exit(1);
	}
	
	private static void help(Options options)
	{
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("sake [<options>] <input files...>", options);
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
		
		options.addOption("b", "backend", true,
						"specifies backend to use");
		
		options.addOption("da", "dump-ast", false,
						"dump AST to stdout");

		options.addOption("di", "dump-ir", false,
						"dump IR code to stdout");

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
			
			Backend = cli.getOptionValue('b');
			if (Backend == null)
				throw new ParseException("You must specify a backend.");
			
			DumpAST = cli.hasOption("da");
			DumpIR = cli.hasOption("di");
		}
		catch (ParseException e)
		{
			abort(e.getMessage());
		}

		return cli;
	}
	
	private static Backend createBackend(OutputStream os)
	{
		if (Backend.equals("make"))
			return new MakeBackend(os);
		
		abort("The backend '"+Backend+"' is not recognised.");
		return null;
	}
	
	public static void main(String[] args)
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
			
			String data = FileUtils.readFileToString(new File(filename), "UTF-8");
			Location loc = new Location(data, filename);
			
			FileOutputStream fos = new FileOutputStream(OutputFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			Backend backend = createBackend(bos);
			
			backend.prologue();

			Compiler c = new Compiler();
			c.setListener(new CompilerTimer());
			c.setInput(loc);
			c.setBackend(backend);
			c.compile();

			if (DumpAST)
				c.getAst().dump();
			if (DumpIR)
				c.dumpBasicBlocks();
			
			backend.epilogue();
			bos.close();
			
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
