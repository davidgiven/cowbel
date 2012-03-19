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
#include <Buffer.ch>

/** Represents a simple, non-seekable input stream.
 **/

type InputStream =
{
	/** Returns true if there is nothing left to read on this stream.
	 **/
	 
	function isEOF(): boolean;
	
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
		
		function isEOF(): (result: boolean)
		{
			result = extern(boolean);
			extern '${result} = feof(stdin);';
		}
		
		function readByte(): (result: int)
		{
			result = extern(int);
			extern '${result} = fgetc(stdin);';
		}
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
	/** Returns true if there is nothing left to read.
	 **/
	
	function isEOF(): boolean;
	
	/** Reads the next Unicode code point. Returns -1 on EOF.
	 **/
	 
	function readCodePoint(): int;
	
	/** Reads in a line of text, terminated by a \n or an EOF. \r characters
	 ** are ignored.
	 **/
	 
	function readString(): string;
};

/** Creates new Reader which reads from the specified InputStream.
 **/
 
function InputStreamReader(is: InputStream): Reader
{
	return
	{
		implements Reader;
		
		function isEOF(): boolean
			return is.isEOF();
			
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
		
		function readString(): string
		{
			var buffer = Buffer.New(0);
			
			while (true)
			{
				var c = is.readByte();
				if (c == -1)
					break;
				if (c == 10)
					break;
				if (c == 13)
					continue;
				buffer.append(c);
			}
			
			return buffer.toString();
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
	
	/** Writes a string.
	 **/
	
	function writeString(s: string);
	
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
				var c = extern(int);
				extern '${c} = buffer[${i}];';
				os.writeByte(c);
			}
		}
		
		function writeString(s: string)
		{
			var buffer = Buffer.NewFromString(s);
			var lo, hi = buffer.bounds();
			for i = lo, hi
			{
				var c = buffer.get(i);
				os.writeByte(c);
			}
		}
		
		function flush()
			os.flush();
	};
}

#endif
