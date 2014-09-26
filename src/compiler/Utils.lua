-- © 2014 David Given
-- This file is redistributable under the terms of the
-- New BSD License. Please see the COPYING file in the
-- project root for the full text.

local posix = require("posix")
local ServerDir = ServerDir

--- Halts with a user error (no stack trace).

local function UserError(...)
	local args = {}
	for i = 1, select("#", ...) do
		local a = select(i, ...)
		args[#args+1] = tostring(a)
	end
	io.stderr:write("cowbel: ", table.concat(args), "\n")
	os.exit(1)
end

--- Halts with a fatal error.

local function FatalError(...)
	local args = {}
	for i = 1, select("#", ...) do
		local a = select(i, ...)
		args[#args+1] = tostring(a)
	end
	error(table.concat(args))
end

--- Writes out a debug message.

local function Debug(...)
	local args = {}
	for i = 1, select("#", ...) do
		local a = select(i, ...)
		args[#args+1] = tostring(a)
	end
	io.stderr:write(table.concat(args), "\n")
end

--- Opens a resource file (but does not read it in).

local function OpenFile(filename)
	local fp, e = io.open(filename, "r")
	if e then
		FatalError(e)
	end
	
	return fp
end 

--- Loads a resource file.
-- Files are looked up in the server directory.

local function LoadFile(filename)
	local fp = OpenFile(filename)
	local data = fp:read("*a")
	fp:close()
	return data
end

--- Checks the first parameter for an error code; if so, halts with a
--- fatal error.
--
--     e: error code, or nil for success
--     ...: message (error code is appended)

local function Check(e, ...)
	if e then
		local args = {...}
		args[#args+1] = ": "
		args[#args+1] = e
		FatalError(unpack(args))
	end
end

--- Checks the first parameter for false; if so, halts with a fatal error.
--
--     e: any value
--     ...: message (error code is appended)

local function Assert(e, ...)
	if not e then
		FatalError(...)
	end
end

--- Argifies a string.
-- Quotation marks are not matched.
--
--    s: a string
--    returns: an array

local function Argify(s)
	local t = {}
	for w in string.gmatch(s, "([^%s]+)") do
		t[#t+1] = w
	end
	return t
end

--- Shellifies a list into an argument string.
--
--    list: list of arguments
--    returns: a string

local function Shellify(list)
	local s = {}
	for _, word in pairs(list) do
		word = string.gsub(word, "([\\\"'$])", "\\%0")
		s[#s+1] = '"'..word..'"'
	end
	return table.concat(s, " ")
end

--- Parses command line parameters.
--
--    args: list of arguments
--    cbtab: callback table
--
-- Callbacks in cbtab consist of:
--
--    <option>: (arg): a command line option has been seen; arg is the
--                 argument; if used, return 1, otherwise 0
--    " filename": (filename): a bare filename has been seen
--    " unrecognised": (option): an unrecognised option has been seen

local function ParseCommandLine(args, cbtab)
	local function do_unrecognisedarg(option)
		cbtab[" unrecognised"](option)
	end
	
	local function do_filename(filename)
		cbtab[" filename"](filename)
	end
	
	local i = 1
	while (i <= #args) do
        local o = args[i]
        local op
        
        if (o:byte(1) == 45) then
            -- This is an option.
            if (o:byte(2) == 45) then
                -- ...with a -- prefix.
                o = o:sub(3)
                local fn = cbtab[o]
                if not fn then
                    do_unrecognisedarg("--"..o)
                    return
                end
                local op = args[i+1]
                i = i + fn(op)
            else
                -- ...without a -- prefix.
                local od = o:sub(2, 2)
                local fn = cbtab[od]
                if not fn then
                    do_unrecognisedarg("-"..od)
                end
                op = o:sub(3)
                if (op == "") then
                    op = args[i+1]
                    i = i + fn(op)
                else
                    fn(op)
                end
            end
        else
        	do_filename(o)
        end
        
        i = i + 1
    end

end

--- Massages a static description string.
-- Used for text in [[...]] blocks.
--
--    s: string
--    returns: another string

local function Unindent(input)
	out = "\n" .. input
	local out = out:gsub("\n[ \t]*", "\n")
	out = out:gsub("\n\n", "¡")
	out = out:gsub("\n", " ")
	out = out:gsub("¡", "\n")
	out = out:gsub("%s+", " ")
	out = out:gsub("%s*$", "")
	out = out:gsub("^%s*", "")
	
	return out
end

--- Expands the parameters into a string.
-- Works with non-string values, including nil.
--
--    ...: any values
--    returns: a string

local function Stringify(...)
	local args = {...}
	local argslen = select("#", ...)
	for i = 1, argslen do
		args[i] = tostring(args[i])
	end
	return table.concat(args, "", 1, argslen)
end

--- Throws an exception (with parameter massaging).
--
--    e: exception value

local function Throw(e)
	if not e then
		e = "attempt to throw nil"
	end
	error(e)
end

--- Returns the smallest key in a table.
--
--   t: table

local function FindSmallestKey(t)
	local c = next(t)
	if not c then
		return nil
	end

	for k in pairs(t) do
		if (k < c) then
			c = k
		end
	end
	return c
end

--- Runs a command and returns the output.
-- Throws on error if the command fails.

local function ReadPipe(command)
	local fp, e = io.popen(command, "r")
	if e then
		FatalError(e)
	end

	local data = fp:read("*a")
	local e, _, exitcode = fp:close()
	if not e then
		FatalError("command failed: "..exitcode)
	end
	return data
end

--- Runs a shell command.
-- Throws an error if the command fails.

local function System(command)
	local e, _, exitcode = os.execute(command)
	if not e then
		FatalError("command failed: "..exitcode)
	end
end

--- Times a function, and reports the result.

local function Time(name, func)
	local function now()
		local t = posix.times()
		return t.stime + t.cstime + t.utime + t.cutime
	end

	local starttime = now()
	func()
	local endtime = now()
	local elapsed = endtime - starttime
	print(string.format("%s: %dms", name, elapsed*1000))
end

--- Converts a vararg set of arguments to a set.
-- Returns a table where each argument is a key to a table. The values are all true.

local function Set(...)
	local t = {}
	for _, v in ipairs({...}) do
		t[v] = true
	end
	return t
end	

--- Calls a callback on an object or list of objects.
local function ForEach(object, callback)
	if object.type then
		callback(object)
	else
		for _, o in ipairs(object) do
			ForEach(o, callback)
		end
	end
end

return
{
	OpenFile = OpenFile,
	LoadFile = LoadFile,
	UserError = UserError,
	FatalError = FatalError,
	Debug = Debug,
	Check = Check,
	Assert = Assert,
	Argify = Argify,
	Shellify = Shellify,
	ParseCommandLine = ParseCommandLine,
	Unindent = Unindent,
	Stringify = Stringify,
	Throw = Throw,
	FindSmallestKey = FindSmallestKey,
	ReadPipe = ReadPipe,
	System = System,
	Time = Time,
	Set = Set,
	ForEach = ForEach,
}
