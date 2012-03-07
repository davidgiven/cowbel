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
 
type int = {
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
};

var int = {
 type extern "s_int_t";
 implements int;
	
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
 		{ r=extern(boolean); extern "${r} = self < ${o};"; }
 
	function >= (o: int): (r: boolean)
 		{ r=extern(boolean); extern "${r} = self >= ${o};"; }
 
	function toString(): (r: string)
		{ r=extern(string); extern "S_METHOD_INT_TOSTRING(self, &${r});"; }
};

type real = {
	function - (o: real): real;
};

var real = {
	type extern "s_real_t";
	implements real;
 
	function - (o: real): (r: real) { r=extern(real); extern "REAL ADD"; }
};

type string = {
	function + (o: string): string;
};

var string = {
	type extern "s_string_t*";
	implements string;
	
	function + (o: string): (r: string)
		{ r = extern(string); extern "S_METHOD_STRING__ADD(self, ${o}, &${r});"; }
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

type __extern = {
};

var __extern = {
	type extern "void*";
	implements __extern;
};

#endif
