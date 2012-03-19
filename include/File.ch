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

#ifndef COWBEL_FILE
#define COWBEL_FILE

#include <Stdlib.ch>
#include <StreamIO.ch>

extern '#include <errno.h>';
extern '#include <unistd.h>';

/** Represents an open file.
 **/
 
type File =
{
	/** Attempts to close the file. Returns 0 on success, or an errno on
	 ** error. */
	 
	function close(): int;

	/** Flushes the file. Returns 0 on success, or an errno on error.
	 **/
	 
	function flush(): int;
	
	/** Returns the length of the file.
	 **/
	 
	function length(): int;
	
	/** Writes a byte of data from the file. Returns 0 on success, or an
	 ** errno on error.
	 **/
	 
	function write(position: int, byte: int): int; 
	
	/** Reads a block of data to the file. Returns the byte on success, or
	 ** -1 on error.
	 **/
	 
	function read(position: int): int;
};

var File =
{
	/** Attempts to change the current working directory. Returns 0 on success,
	 ** or an errno on error. */
	 
	function ChDir(filename: string): (result: int)
	{
		result = extern(int);
		extern 'errno = 0; chdir(s_string_cdata(${filename})); ${result} = errno;';
	}
	
	/** Returns the current working directory.
	 **/
	 
	function GetCWD(): (result: string)
	{
		var e = extern(int);
		result = extern(string);
		
		extern 'char buffer[PATH_MAX];';
		extern 'errno = 0; getcwd(buffer, sizeof(buffer)); ${e} = errno';
		if (e != 0)
			Application.Abort("Unable to fetch CWD: " + e.toString());
			
		extern '${result} = s_create_string_val(buffer, strlen(buffer));';
	}
		
	/** Attempts to remove a file. Returns 0 on success, or an errno on
	 ** error. */
	 
	function Remove(filename: string): (result: int)
	{
		result = extern(int);
		extern 'errno = 0; remove(s_string_cdata(${filename})); ${result} = errno;';
	}
	
	/** Attempts to create a directory. Returns 0 on success, or an errno on
	 ** error. */
	
	function MkDir(filename: string): (result: int)
	{
		result = extern(int);
		extern 'errno = 0; mkdir(s_string_cdata(${filename}), 0755); ${result} = errno;';
	}
	
	/** Attempts to remove a directory. Returns 0 on success, or an errno on
	 ** error. */
	
	function RmDir(filename: string): (result: int)
	{
		result = extern(int);
		extern 'errno = 0; rmdir(s_string_cdata(${filename})); ${result} = errno;';
	}
	
	/** Attempts to open a file. Returns a new File object and an errno. On
	 ** error, the file object will abort if used. */
	 
	function Open(filename: string, mode: string): (file: File, errno: int)
	{
		var fp = extern(__extern);
		errno = extern(int);
		
		mode = mode + "b";
		extern 'errno = 0; ${fp} = fopen(s_string_cdata(${filename}), s_string_cdata(${mode})); ${errno} = errno;';
		
		function _check_fp()
		{
			if (fp.isNull())
				Application.Abort("invalid File object");
		}
		
		return
		{
			implements File;
			
			function close(): (errno: int)
			{
				_check_fp();
				errno = extern(int);
				extern 'errno = 0; fclose((FILE*) ${fp}); ${errno} = errno;';
				extern '${fp} = NULL;';
			}
		
			function flush(): (errno: int)
			{
				_check_fp();
				errno = extern(int);
				extern 'errno = 0; fflush((FILE*) ${fp}); ${errno} = errno;';
			}
			
			function length(): (length: int)
			{
				_check_fp();
				length = extern(int);
				extern '${length} = ftell((FILE*) ${fp});';
			}
			
			function write(position: int, byte: int): (errno: int)
			{
				_check_fp();
				errno = extern(int);
				extern 'errno = 0;';
				extern 'fseek((FILE*) ${fp}, ${position}, SEEK_SET);';
				extern 'fputc(${byte}, (FILE*) ${fp});';
				extern '${errno} = errno;';
			} 
	
			function read(position: int): (result: int)
			{
				_check_fp();
				result = extern(int);
				extern 'errno = 0;';
				extern 'fseek((FILE*) ${fp}, ${position}, SEEK_SET);';
				extern '${result} = fgetc((FILE*) ${fp});';
				extern 'if (errno != 0) ${result} = -1;';
			}
		};
	}
	
	/** Creates an InputStream from a File object. The InputStream tracks its
	 ** own position independently of any other streams on the File.
	 **/
	 
	function InputStream(file: File, pos: int): InputStream
	{
		return
		{
			implements InputStream;
			
			function isEOF(): boolean
				return pos >= file.length();
			
			function readByte(): (result: int)
			{
				result = file.read(pos);
				if (result != -1)
					pos = pos + 1;
			}
		};
	}
	
	/** Create an OutputStream from a File object. The OutputStream tracks its
	 ** own position independently of any other streams on the File.
	 **/
	
	function OutputStream(file: File, pos: int): OutputStream
	{
		return
		{
			implements OutputStream;
			
			function writeByte(value: int)
			{
				var e = file.write(pos, value);
				if (e == 0)
					pos = pos + 1;
			}
				
			function flush()
				file.flush();
		};
	}
};


#endif
