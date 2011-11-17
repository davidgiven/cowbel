package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class InterfaceDeclarationNode extends StatementNode
{
	public InterfaceDeclarationNode(Location start, Location end,
			IdentifierNode name, TypeParameterDeclarationListNode typeparams,
			List<MethodDeclarationNode> methods)
    {
		super(start, end);
		addChild(name);
		addChild(typeparams);
		addChildren(methods);
    }	
}
