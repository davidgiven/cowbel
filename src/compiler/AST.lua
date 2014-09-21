-- © 2014 David Given
-- This file is redistributable under the terms of the
-- New BSD License. Please see the COPYING file in the
-- project root for the full text.

local cjson = require("cjson")
local Utils = require("Utils")

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

return
{
	Load = Load,
}

