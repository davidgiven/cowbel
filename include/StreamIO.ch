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

#ifndef COWBEL_STREAMIO
#define COWBEL_STREAMIO

#include <Stdlib.ch>
#include <Application.ch>
#include <Maybe.ch>

/** Represents a simple, non-seekable input stream.
 **/

type InputStream =
{
	/** Reads a byte from the stream. Returns -1 on EOF.
	 **/
	 
	function readByte(): int;
};

/** Represents a simple, non-seekable output stream.
 **/
 
type OutputStream =
{
	/** Writes a byte to the stream.
	 **/
	 
	function writeByte(b: int);
	
	/** Flushes all queued data to the stream.
	 **/
	 
	function flush();
};

/** Returns an InputStream representing stdin.
 **/ 

function Stdin(): InputStream
{
	return
	{
		implements InputStream;
		
		function readByte(): (result: int)
			extern '${result} = fgetc(stdin);';
	};
}

/** Returns an OutputStream representing stdout.
 **/
 
function Stdout(): OutputStream
{
	return
	{
		implements OutputStream;
		
		function writeByte(value: int)
			extern 'fputc(${value}, stdout);';
			
		function flush()
			extern 'fflush(stdout);';
	};
}
	
/** Returns an OutputStream representing stderr.
 **/
 
function Stderr(): OutputStream
{
	return
	{
		implements OutputStream;
		
		function writeByte(value: int)
			extern 'fputc(${value}, stderr);';
			
		function flush()
			extern 'fflush(stderr);';
	};
}

/** Represents an abstract thing that reads Unicode code points.
 **/
 
type Reader =
{
	/** Reads the next Unicode code point. Returns -1 on EOF.
	 **/
	 
	function readCodePoint(): int;
};

/** Creates new Reader which reads from the specified InputStream.
 **/
 
function InputStreamReader(is: InputStream): Reader
{
	return
	{
		implements Reader;
		
		function readCodePoint(): (result: int)
		{
			var leadbyte = is.readByte();
			if (leadbyte == -1)
				return -1;

			var trailingbytes = 0;
			extern '${trailingbytes} = utf8_trailing_bytes[${leadbyte}];';
			if (trailingbytes == -1)
				return 65533;
			
			extern 'unsigned char buffer[4];';
			extern 'buffer[0] = ${leadbyte};';
			for i = 0, trailingbytes
			{
				var c = is.readByte();
				extern 'buffer[1+${i}] = ${c};';
			}
			
			extern 'const unsigned char* p = buffer;';
			extern '${result} = utf8_read(&p);';
		}
	};
}

/** Represents an abstract thing that writes Unicode code points.
 **/
 
type Writer =
{
	/** Writes a Unicode code point.
	 **/
	 
	function writeCodePoint(i: int);
	
	/** Flushes any pending output.
	 **/
	 
	function flush();
};

/** Creates new Writer which writes to the specified OutputStream.
 **/
 
function OutputStreamWriter(os: OutputStream): Writer
{
	return
	{
		implements Writer;
		
		function writeCodePoint(codepoint: int)
		{
			extern 'unsigned char buffer[4];';
			extern 'unsigned char* p = buffer;';
			extern 'utf8_write(&p, ${codepoint});';
			var numbytes = 0;
			extern '${numbytes} = p - buffer;';
			
			for i = 0, numbytes
			{
				var c = 0;
				extern '${c} = buffer[${i}];';
				os.writeByte(c);
			}
		}
		
		function flush()
			os.flush();
	};
}

#endif
