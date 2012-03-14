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

#ifndef COWBEL_GLFW
#define COWBEL_GLFW

#include <Stdlib.ch>

extern '#include <GL/glfw.h>';

IMPORT_CONST(GLFW_WINDOW, int)

IMPORT_CONST(GLFW_MOUSE_BUTTON_LEFT, int)
IMPORT_CONST(GLFW_MOUSE_BUTTON_MIDDLE, int)
IMPORT_CONST(GLFW_MOUSE_BUTTON_RIGHT, int)

var GLFW =
{
	function Init(): (r: int)
	{
		r = extern(int);
		extern '${r} = glfwInit();';
	}
	
	function Terminate()
	{
		extern 'glfwTerminate();';
	}
	
	function SetWindowTitle(title: string)
	{
		extern 'glfwSetWindowTitle(s_string_cdata(${title}));';
	}
	
	function OpenWindow(width: int, height: int,
		redbits: int, greenbits: int, bluebits: int,
		alphabits: int, depthbits: int, stencilbits: int,
		mode: int): (r: int)
	{
		r = extern(int);
		extern '${r} = glfwOpenWindow(${width}, ${height}, ${redbits}, ${greenbits}, ${bluebits}, ${alphabits}, ${depthbits}, ${stencilbits}, ${mode});';
	}
	
	function CloseWindow()
	{
		extern 'glfwCloseWindow();';
	}
	
	function SwapBuffers()
	{
		extern 'glfwSwapBuffers();';
	}
	
	function GetKey(key: int): (r: boolean)
	{
		r = extern(boolean);
		extern '${r} = glfwGetKey(${key});';
	}
	
	function GetMousePos(): (x: int, y: int)
	{
		x = extern(int);
		y = extern(int);
		extern 'glfwGetMousePos(&${x}, &${y});';
	}
	
	function GetMouseButton(button: int): (r: boolean)
	{
		r = extern(boolean);
		extern '${r} = glfwGetMouseButton(${button});';
	}
	
	function GetTime(): (r: real)
	{
		r = extern(real);
		extern '${r} = glfwGetTime();';
	}
};

#endif
