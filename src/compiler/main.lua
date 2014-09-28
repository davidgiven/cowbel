-- © 2014 David Given
-- This file is made available under the terms of the two-clause BSD
-- license. See the file COPYING.BSD in the distribution directory for the
-- full license text.

-- Add the directory containing this script to the package path.

do
	local path = arg[0]:gsub("[^/]+$", "")
	package.path = path .. "?.lua;" .. path .. "?/init.lua;" .. package.path
end

local cjson = require("cjson")
local pretty = require("pl.pretty").dump
local Utils = require("Utils")
local AST = require("AST")
local Symbols = require("Symbols")

local inputfile
local outputfile

do
	local function do_help(arg)
		io.stderr:write("cowbel © 2013 David Given\n"..
		                "Usage: cowbel [<options>] -o outputfile.c sourcefile.cow\n"..
						"\n"..
						"Options:\n"..
						"  -h  --help           produces this message\n"..
						"  -oF --output F       write output to file F\n"
					)
		os.exit(0)
	end

	local function do_outputfile(arg)
		if outputfile then
			Utils.UserError("you can only specify one output file (try --help)")
		end
		outputfile = arg
		return 1
	end

	Utils.ParseCommandLine({...},
		{
			["h"] = do_help,
			["help"] = do_help,

			["o"] = do_outputfile,
			["output"] = do_outputfile,

			[" unrecognised"] = function(arg)
				Utils.UserError("option '"..arg.."' not recognised (try --help)")
			end,

			[" filename"] = function(arg)
				if inputfile then
					Utils.UserError("you can only specify one input file (try --help)")
				end
				inputfile = arg
				return 1
			end
		}
	)
end

if not inputfile then
	Utils.UserError("nothing to do (try --help)")
end
if not outputfile then
	outputfile = inputfile:gsub("%.[a-zA-Z]+$", ".c")
end

-- Load file.

local ast
Utils.Time("Parse", function()
		ast = AST.Load(inputfile)
		AST.Annotate(ast)
	end)

Utils.Time("Resolve symbols", function()
		Symbols.Resolve(ast)
	end)

pretty(ast)

