package com.cowlang2.parser.tokens;

import java.util.List;
import com.cowlang2.parser.core.Location;

public class BlockNode extends StatementListNode
{
	public BlockNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end, statements);
    }
}