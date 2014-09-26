-- © 2014 David Given
-- This file is redistributable under the terms of the
-- New BSD License. Please see the COPYING file in the
-- project root for the full text.

local cjson = require("cjson")
local Utils = require("Utils")

--- Lookup table of AST node properties which correspond to children.

local astchildren =
{
	["assign"] = Utils.Set("names", "values"),
	["block"] = Utils.Set("body"),
	["boolean"] = {},
	["call"] = Utils.Set("interfaces", "receiver", "parameters"),
	["declare"] = Utils.Set("variables"),
	["extern"] = {},
	["function"] = Utils.Set("functionspec", "body"),
	["functionspec"] = Utils.Set("typeparams", "inparams", "outparams"),
	["identifier"] = {},
	["ifelse"] = Utils.Set("condition", "iftrue", "iffalse"),
	["integer"] = {},
	["interfacedef"] = Utils.Set("body"),
	["object"] = Utils.Set("statements"),
	["objectimplements"] = Utils.Set("interface", "delegates"),
	["real"] = {},
	["string"] = {},
	["typedef"] = Utils.Set("typeparams", "body"),
	["typefunction"] = Utils.Set("functionspec"),
	["typeref"] = Utils.Set("typeargs"),
	["while"] = Utils.Set("condition", "body");
}

--- Loads a Cowbel file off disk.
-- Returns the unprocessed AST corresponding to the contents of the file.

local function Load(filename)
	local tempfile = os.tmpname()
	Utils.System(Utils.Shellify({"cpp", "-Iinclude", filename, "-o", tempfile}))
	local parsecmd = Utils.Shellify({"bin/cowbel-parser"}).."<"..tempfile
	local data = Utils.ReadPipe(parsecmd)
	os.remove(tempfile)
	return cjson.decode(data)
end

--- Calls a callback for each child of an AST node.

local function ForEachChild(ast, callback)
	local t = ast.type
	local children = astchildren[t]
	if not children then
		Utils.FatalError("bad AST node '"..t.."'")
	end

	for k in pairs(children) do
		local node = ast[k]
		if node then
			Utils.ForEach(ast[k], callback)
		end
	end
end

--- Walks an AST tree and annotates each node with a pointer to its parent.

local function Annotate(ast)
	ForEachChild(ast,
		function(node)
			node.parent = ast
			Annotate(node)
		end
	)
end

return
{
	Load = Load,
	ForEachChild = ForEachChild,
	Annotate = Annotate,
}

