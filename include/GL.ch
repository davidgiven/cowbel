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

#ifndef COWBEL_GL
#define COWBEL_GL

#include <Stdlib.ch>

extern '#include <GL/gl.h>';
extern '#include <GL/glu.h>';

IMPORT_CONST(GL_COLOR_BUFFER_BIT, int)
IMPORT_CONST(GL_DEPTH_BUFFER_BIT, int)

IMPORT_CONST(GL_PROJECTION, int)
IMPORT_CONST(GL_MODELVIEW, int)

IMPORT_CONST(GL_DEPTH_TEST, int)

IMPORT_CONST(GL_LEQUAL, int)

IMPORT_CONST(GL_TRIANGLES, int)

var GL =
{
	function Begin(t: int)
	{
		extern 'glBegin(${t});';
	}
	
	function ClearColor(red: real, green: real, blue: real, alpha: real)
	{
		extern 'glClearColor(${red}, ${green}, ${blue}, ${alpha});';
	}
	
	function Clear(bits: int)
	{
		extern 'glClear(${bits});';
	}
	
	function ClearDepth(depth: real)
	{
		extern 'glClearDepth(${depth});';
	}

	function DepthFunc(func: int)
	{
		extern 'glDepthFunc(${func});';
	}
		
	function Enable(feature: int)
	{
		extern 'glEnable(${feature});';
	}
	
	function End()
	{
		extern 'glEnd();';
	}
	
	function LoadIdentity()
	{
		extern 'glLoadIdentity();';
	}
	
	function MatrixMode(mode: int)
	{
		extern 'glMatrixMode(${mode});';
	}
	
	function Rotate(angle: real, x: real, y: real, z: real)
	{
		extern 'glRotated(${angle}, ${x}, ${y}, ${z});';
	}
	
	function Vertex3f(x: real, y: real, z: real)
	{
		extern 'glVertex3f(${x}, ${y}, ${z});';
	}
	
	function Viewport(x: int, y: int, w: int, h: int)
	{
		extern 'glViewport(${x}, ${y}, ${w}, ${h});';
	}
};

var GLU =
{
	function Perspective(fovy: real, aspect: real, zNear: real, zFar: real)
	{
		extern 'gluPerspective(${fovy}, ${aspect}, ${zNear}, ${zFar});';
	}
};

#endif
