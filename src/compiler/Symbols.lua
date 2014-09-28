-- © 2014 David Given
-- This file is redistributable under the terms of the
-- New BSD License. Please see the COPYING file in the
-- project root for the full text.

local Utils = require("Utils")
local AST = require("AST")

local function Resolve(ast)
	local nodes =
	{
	}

	AST.ForEachChild(ast,
		function(node)
			local t = nodes[node.type]
			if t then
				t(node)
			end
		end
	)
end

return
{
	Resolve = Resolve
}
