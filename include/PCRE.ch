/* cowbel test suite
 *
 * Written in 2012 by David Given.
 *
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 *
 * Please see the file COPYING.CC0 in the distribution package for more
 * information.
 */

#ifndef COWBEL_PCRE
#define COWBEL_PCRE

#include <Stdlib.ch>
#include <SimpleIO.ch>
#include <Application.ch>

extern '#include <pcre.h>';

/** Represents a compiled pattern. */

type Pattern =
{
	/** Matches the pattern against a string and returns a new Match object. */
	
	function match(s: string): Match;
};

/** Represents a single match. */

type Match =
{
	/** If the pattern matched at all, returns true.
	 **/
	 
	function matched(): boolean;
	
	/** Returns the number of captures for this match. (0 if nothing matched;
	 ** otherwise, at least 1.)
	 **/
	 
	function captures(): int;
	
	/** Returns a specific capture. 0 is the entire pattern; groups are
	 ** returned in captures 1 and above.
	 **/
	 
	function capture(i: int): string;
};

var PCRE =
{
	/** Compiles a regular expression into a pattern.
	 **/
	 
	function Compile(regexp: string): (result: Pattern)
	{
		var pattern = extern(__extern);
		var extra = extern(__extern);
		var errore = extern(__extern);
		var offset = extern(int);
		
		extern '${pattern} = pcre_compile(s_string_cdata(${regexp}), PCRE_UTF8, (const char**)&${errore}, &${offset}, NULL);';
		if (pattern.isNull())
		{
			var error = extern(string);
			extern '${error} = s_create_string_constant((const char*) ${errore}, 0);';
			Abort("PCRE compilation error: " + error + " at offset " + offset.toString());
		}
		
		extern '${extra} = pcre_study((pcre*)${pattern}, 0, (const char**)&${errore});';
		
		var capturecount = extern(int);
		extern 'pcre_fullinfo((pcre*)${pattern}, (pcre_extra*)${extra}, PCRE_INFO_CAPTURECOUNT, &${capturecount});';
		
		return
		{
			implements Pattern;
			
			function match(s: string): Match
			{
				var ovector = extern(__extern);
				var ovectorsize = (capturecount+1) * 3;
				extern '${ovector} = S_ALLOC_DATA(${ovectorsize} * sizeof(int));';
				if (ovector.isNull())
					AbortOutOfMemory();
				
				var sdata = extern(__extern);
				extern '${sdata} = (void*)s_string_cdata(${s});';
				
				var num_captures = extern(int);
				extern '${num_captures} = pcre_exec((pcre*)${pattern}, (pcre_extra*)${extra}, (const char*)${sdata}, ${s}->totallength, 0, 0, (int*)${ovector}, ${ovectorsize});';
				if (num_captures < 0)
					num_captures = 0;

				var captures = Array<string>(num_captures, "");
				for i = 0, num_captures
				{
					var startoffset = extern(int);
					extern "${startoffset} = ((int*)${ovector})[${i} * 2];";
					
					var endoffset = extern(int);
					extern "${endoffset} = ((int*)${ovector})[${i} * 2 + 1];";
					
					var length = endoffset - startoffset;					
					
					var c = extern(string);
					extern '${c} = s_create_string_constant(((const char*)${sdata}) + ${startoffset}, ${length});';
					captures.set(i, c);
				}
				
				return
				{
					implements Match;
					
					function matched(): boolean
						return (num_captures != 0);
						
					function captures(): int
						return num_captures;
						
					function capture(index: int): (result: string)
						return captures.get(index);
				};
			}
		};
	}
};

#endif
