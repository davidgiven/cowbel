package com.cowlang.sake;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.cowlang.sake.parser.core.FailedParse;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.nodes.Parser;
import com.cowlang.sake.parser.tokens.Node;

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
			
			ParseResult pr = Parser.ProgramParser.parse(loc);
			
			if (pr.failed())
			{
				FailedParse fp = (FailedParse) pr;
				System.out.println("Parse failed: "+fp.message()+" at "+fp.locationAsString());
			}
			else
			{
				System.out.println("Parse successful");
				((Node)pr).dump();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
