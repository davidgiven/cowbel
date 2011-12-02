package com.cowlark.sake;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			String filename = args[1];
			System.out.println("opening: "+filename);
			
			String data = FileUtils.readFileToString(new File(filename), "UTF-8");
			Location loc = new Location(data, filename);
			
			Compiler c = new Compiler();
			c.setInput(loc);
			c.compile();
			
			System.out.println("Parse successful");
			c.getAst().dump();
		}
		catch (CompilationException e)
		{
			System.out.println("Compilation failed:");
			System.out.println(e.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
