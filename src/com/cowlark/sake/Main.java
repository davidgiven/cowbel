package com.cowlark.sake;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.cowlark.sake.backend.Backend;
import com.cowlark.sake.backend.make.MakeBackend;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.FailedParseException;
import com.cowlark.sake.parser.core.FailedParse;
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
			
			//System.out.println("Parse successful");
			//c.dumpBasicBlocks();
			
			FileOutputStream fos = new FileOutputStream("out.mk");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			Backend backend = new MakeBackend(bos);
			backend.prologue();
			c.emitCode(backend);
			backend.epilogue();
			bos.close();
		}
		catch (FailedParseException e)
		{
			FailedParse fp = e.getFailedParse();
			
			System.out.println("Parse failed:");
			System.out.println(fp.toString());
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
