package com.cowlark.sake.ast.nodes;

import java.util.List;
import com.cowlark.sake.parser.core.Location;

public class BlockNode extends StatementListNode
{
	public BlockNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end, statements);
    }

	public BlockNode(Location start, Location end,
			StatementNode... statements)
    {
        super(start, end, statements);
    }
}