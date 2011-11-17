package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class StatementListNode extends Node
{
	public StatementListNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end);
        addChildren(statements);
    }
}