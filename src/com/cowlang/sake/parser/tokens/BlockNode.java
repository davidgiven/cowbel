package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class BlockNode extends StatementListNode
{
	public BlockNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end, statements);
    }
}