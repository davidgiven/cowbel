-- © 2013 David Given
-- This file is made available under the terms of the two-clause BSD
-- license. See the file COPYING.BSD in the distribution directory for the
-- full license text.

local lpeg = require("lpeg")
local pretty = require("pl.pretty").dump

local P = lpeg.P
local R = lpeg.R
local B = lpeg.B
local S = lpeg.S
local C = lpeg.C
local V = lpeg.V
local Cc = lpeg.Cc
local Cg = lpeg.Cg
local Ct = lpeg.Ct
local Cp = lpeg.Cp
local Cmt = lpeg.Cmt

local keywords =
	{
		"break",
		"continue",
		"else",
		"function",
		"if",
		"return",
		"var",
	}

local newlinemap = {}
local function newline(s, pos)
	newlinemap[pos] = true
	return pos
end

local ws = (S(" \t\r") + Cmt(P("\n"), newline))^0

local id_start = R("AZ", "az") + S("_")
local id_middle = id_start + R("09")
local id = id_start * id_middle^0

-- Matches a keyword

local function K(k)
	return ws * P(k) * -id_middle
end

-- Matches an operator

local function O(o)
	return ws * P(o)
end

-- Matches an integer

local decimalint = P("-")^-1 * R("09")^1
local hexint = P("-")^-1 * P("0x") * (R("09") + R("af") + R("AF"))^1
local int = ws * C(hexint + decimalint)

-- Matches a non-keyword identifier

local name = C(id)
for _, v in pairs(keywords) do
	name = name - (P(v) * -id_middle)
end
name = ws * name

local function X(c, e)
	return c + function(s, i)
		error("parse failure: "..e.." at "..i)
	end
end

-- Forced matches: produces a parse error if the named operator or keyword
-- doesn't match.

local function XO(o)
	return X(O(o), "missing '"..o.."'")
end

local function XK(o)
	return X(K(o), "missing '"..o.."'")
end

local function astnode(t, c)
	if c then
		return Ct(
			Cg(Cc(t), "type") *
			Cg(Cp(), "pos") *
			c)
	else
		return Ct(
			Cg(Cc(t), "type") *
			Cg(Cp(), "pos"))
	end
end

local grammar = P(
	{
		"scopeconstructor";

		["statement"] = V("simplestmt") + V("compoundstmt"),
		["simplestmt"] =
			(
				V("expressionstmt") +
				V("qualifiedvar") +
				V("var") +
				V("return") +
				V("continue") +
				V("break")
			) * XO(";"),

		["compoundstmt"] =
			V("ifelse") +
			V("if") +
			V("scopeconstructor") +
			V("functionstmt"),

		-- Types

		["type"] = V("typeref") + V("interfacedecl"),
		["typeref"] = astnode("typeref", name),
		
		["interfacedecl"] = astnode(
				"interfacedecl",
				O("{") * XO("}")
			),

		["typequalifier"] = astnode(
				"typequalifier",
				O(":") * V("type")
			),

		["parameter"] = astnode(
				"parameter",
				name * O(":") * V("type")
			),

		["parameterlist"] = astnode(
				"parameterlist",
				O("(") * V("parameter")^-1 * (O(",") * V("parameter"))^0 * O(")")
			),

		-- Expressions
	
		["expression"] = V("ref") + V("constint"),

		["constint"] = astnode("constint", int),
		["ref"] = astnode("ref", name),

		-- Statements
	
		["functionstmt"] = astnode(
				"functionstmt",
				K("function") * X(name, "expected identifier")
					* V("parameterlist")
					* XO(":") * V("parameterlist")
					* V("scopeconstructor")
			),

		["var"] = astnode(
				"var",
				K("var") * name * XO("=") * V("expression")
			),

		["qualifiedvar"] = astnode(
				"var",
				K("var") * name * V("typequalifier") * XO("=") * V("expression")
			),

		["expressionstmt"] = astnode(
				"exprstmt",
				V("expression")
			),

		["if"] = astnode(
				"if",
				K("if") * V("expression") * V("statement")
			),

		["ifelse"] = astnode(
				"if",
				K("if") * V("expression") * V("statement") * K("else") * V("statement")
			),

		["return"] = astnode(
				"return",
				K("return") * V("expression")
			),

		["continue"] = astnode(
				"continue",
				K("continue")
			),

		["break"] = astnode(
				"break",
				K("break")
			),

		["scopeconstructor"] = astnode(
				"scopeconstructor",
				O("{") * V("statement")^0 * XO("}")
			)
	}
)

return {
	Parse = function(data)
		return grammar:match(data)
	end
}
