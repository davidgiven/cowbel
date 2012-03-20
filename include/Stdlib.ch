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

#ifndef COWBEL_STDLIB
#define COWBEL_STDLIB

/* This file must always be included first; defines the various primitive
 * types everything else uses. */

/* Interfaces which the cowbel primitive types adhere to. Note that boxing
 * and unboxing isn't support yet; don't try to implement these in your own
 * code. It won't work. */
  
type int = {
	function - (): int;
	function + (o: int): int;
	function - (o: int): int;
	function * (o: int): int;
	function / (o: int): int;
	function % (o: int): int;
	
	function == (o: int): boolean;
	function != (o: int): boolean;
	function < (o: int): boolean;
	function <= (o: int): boolean;
	function > (o: int): boolean;
	function >= (o: int): boolean;
	
	function toString(): string;
	function toReal(): real;
};

type real = {
	function - (): real;
	function + (o: real): real;
	function - (o: real): real;
	function * (o: real): real;
	function / (o: real): real;
	
	function == (o: real): boolean;
	function != (o: real): boolean;
	function < (o: real): boolean;
	function <= (o: real): boolean;
	function > (o: real): boolean;
	function >= (o: real): boolean;
	
	function toString(): string;
};

type string = {
	function + (o: string): string;
	
	function == (o: string): boolean;
	function != (o: string): boolean;
	function < (o: string): boolean;
	function <= (o: string): boolean;
	function > (o: string): boolean;
	function >= (o: string): boolean;
};

type boolean = {
	function == (o: boolean): boolean;
	function != (o: boolean): boolean;
	
	function ! (): boolean;
	function & (o: boolean): boolean;
	function | (o: boolean): boolean;
	function ^ (o: boolean): boolean;
	
	function toString(): string;
};

type __extern = {
	function isNull(): boolean;
};

/* ----------------------------------------------------------------------- */

/* Implementations of the above. The variable names are magic and are 
 * used internally by the compiler. */
 
var int = {
	type extern "s_int_t";
	implements int;
	
	function - (): (r: int)
 		{ r=extern(int); extern "${r} = -self;"; }
 
	function + (o: int): (r: int)
 		{ r=extern(int); extern "${r} = self + ${o};"; }
 
	function - (o: int): (r: int)
 		{ r=extern(int); extern "${r} = self - ${o};"; }
 
	function * (o: int): (r: int)
 		{ r=extern(int); extern "${r} = self * ${o};"; }
 
	function / (o: int): (r: int)
 		{ r=extern(int); extern "${r} = self / ${o};"; }
 
	function % (o: int): (r: int)
 		{ r=extern(int); extern "${r} = self % ${o};"; }
 
	function == (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self == ${o};"; }
 
	function != (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self != ${o};"; }
 
	function < (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self < ${o};"; }
 
	function <= (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self <= ${o};"; }
 
	function > (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self > ${o};"; }
 
	function >= (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self >= ${o};"; }
 
	function toString(): (r: string)
		{ r=extern(string); extern "S_METHOD_INT_TOSTRING(self, &${r});"; }
		
	function toReal(): (r: real)
		{ r=extern(real); extern "${r} = self;"; }
};

var real = {
	type extern "s_real_t";
	implements real;
	
	function - (): (r: real)
 		{ r=extern(real); extern "${r} = -self;"; }
 
	function + (o: real): (r: real)
 		{ r=extern(real); extern "${r} = self + ${o};"; }
 
	function - (o: real): (r: real)
 		{ r=extern(real); extern "${r} = self - ${o};"; }
 
	function * (o: real): (r: real)
 		{ r=extern(real); extern "${r} = self * ${o};"; }
 
	function / (o: real): (r: real)
 		{ r=extern(real); extern "${r} = self / ${o};"; }
 
	function == (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self == ${o};"; }
 
	function != (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self != ${o};"; }
 
	function < (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self < ${o};"; }
 
	function <= (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self <= ${o};"; }
 
	function > (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self > ${o};"; }
 
	function >= (o: real): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self >= ${o};"; }
 
	function toString(): (r: string)
		{ r=extern(string); extern "S_METHOD_REAL_TOSTRING(self, &${r});"; }
};

var string = {
	type extern "s_string_t*";
	implements string;
	
	function + (o: string): (r: string)
		{ r = extern(string); extern "S_METHOD_STRING__ADD(self, ${o}, &${r});"; }

	function == (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) == 0;"; }
 
	function != (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) != 0;"; }
 
	function < (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) < 0;"; }
 
	function <= (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) <= 0;"; }
 
	function > (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) > 0;"; }
 
	function >= (o: string): (r: boolean)
 		{ r=extern(boolean); extern "${r} = s_string_cmp(self, ${o}) >= 0;"; }
};

var boolean = {
	type extern "s_boolean_t";
	implements boolean;
	
	function == (o: boolean): (r: boolean)
		{ r = extern(boolean); extern "${r} = self == ${o};"; }
		
	function != (o: boolean): (r: boolean)
		{ r = extern(boolean); extern "${r} = self != ${o};"; }
		
	function ! (): (r: boolean)
		{ r = extern(boolean); extern "${r} = !self;"; }
		
	function & (o: boolean): (r: boolean)
		{ r = extern(boolean); extern "${r} = self & ${o};"; }
		
	function | (o: boolean): (r: boolean)
		{ r = extern(boolean); extern "${r} = self | ${o};"; }
		
	function ^ (o: boolean): (r: boolean)
		{ r = extern(boolean); extern "${r} = self ^ ${o};"; }
		
	function toString(): (r: string)
		{ r = extern(string); extern "${r} = self ? &s_true_label : &s_false_label;"; }
};

var __extern = {
	type extern "void*";
	implements __extern;
	
	function isNull(): (result: boolean)
	{
		result = extern(boolean);
		extern '${result} = !self;';
	}
};

#endif
