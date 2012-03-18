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

/** Represents a file.
 **/
 
type File =
{
	/** Attempts to remove the file. Returns 0 on success, or an errno on
	 ** error. Produces a runtime error if the file is open.
	 **/
	 
	function remove(): int;
			
	/** Attempts to close the file. Returns 0 on success, or an errno on
	 ** error. */
	 
	function close(): int;
			
	/** Attempts to create the file. Returns 0 on success, or an errno on
	 ** error. */
	 
	function create(): int;
		
	/** Attempts to open the file for read-only access. Returns 0 on success,
	 ** or an errno on error. Produces a runtime error if the file is already
	 ** open. */
	 
	function openReadOnly(): int;
	
	/** Attempts to open the file for write-only access. Returns 0 on success,
	 ** or an errno on error. Produces a runtime error if the file is already
	 ** open. */
	 
	function openWriteOnly(): int;
	
	/** Attempts to open the file for read-write access. Returns 0 on success,
	 ** or an errno on error. Produces a runtime error if the file is already
	 ** open. */
	 
	function openReadWrite(): int;
	
	/** Returns the readable stream for the file. Produces a run-time error
	 ** if the file is not open or is open for writing only. */
	 
	function getInputStream(): InputStream;
	
	/** Returns the writeable stream for the file. Produces a run-time error
	 ** if the file is not open or is open for reading only. */
	 
	function getOutputStream(): OutputStream;
};

/** Creates a new File object for a specified path.
 **/
 
function File(path: string): File
{
	var fp: __extern = extern(__extern);
	var open: boolean = false;
	var inputstream = Maybe<InputStream>();
	var outputstream = Maybe<OutputStream>();
	
	function _create_input_stream(fp: __extern): InputStream
	{
		return
		{
			implements InputStream;
			
			function isEOF(): (result: boolean)
			{
				result = extern(boolean);
				extern '${result} = feof((FILE*) ${fp});';
			}
			
			function readByte(): (result: int)
			{
				result = extern(int);
				extern '${result} = fgetc((FILE*) ${fp});';
			}
		};
	}
	
	function _create_output_stream(fp: __extern): OutputStream
	{
		return
		{
			implements OutputStream;
			
			function writeByte(value: int)
				extern 'fputc(${value}, (FILE*) ${fp});';
				
			function flush()
				extern 'fflush((FILE*) ${fp});';
		};
	}
	
	return
	{
		implements File;
		
		function getInputStream(): InputStream
			return inputstream.get();
			
		function getOutputStream(): OutputStream
			return outputstream.get();
		
		function remove(): (result: int)
		{
			result = extern(int);
			if (open)
				AbortInvalidObjectState();
			extern 'errno = 0; remove(s_string_cdata(${path})); ${result} = errno;';
		}
				
		function close(): (result: int)
		{
			result = extern(int);
			if (!open)
				AbortInvalidObjectState();
			if (outputstream.valid())
				outputstream.get().flush();
			extern 'errno = 0; fclose((FILE*) ${fp}); ${result} = errno;';
			open = false;
		}
		
		function create(): (result: int)
		{
			result = extern(int);
			if (open)
				AbortInvalidObjectState();
				
			extern '#include <sys/types.h>';
			extern '#include <sys/stat.h>';
			extern '#include <fcntl.h>';
			
			extern 'errno = 0;';
			extern 'int fd = open(s_string_cdata(${path}), O_CREAT|O_EXCL|O_RDWR, 0644);';
			extern 'if (fd != -1) ${fp} = fdopen(fd, "r+b");';
			extern '${result} = errno;';
			
			if (result != 0)
				return;
				
			open = true;
			inputstream = Maybe<InputStream>(_create_input_stream(fp));
			outputstream = Maybe<OutputStream>(_create_output_stream(fp));
		}
		
		function openReadOnly(): (result: int)
		{
			result = extern(int);
			if (open)
				AbortInvalidObjectState();
			
			extern 'errno = 0;';
			extern '${fp} = fopen(s_string_cdata(${path}), "rb");';
			extern '${result} = errno;';
			
			if (result != 0)
				return;
				
			open = true;
			inputstream = Maybe<InputStream>(_create_input_stream(fp));
		}
		
		function openWriteOnly(): (result: int)
		{
			result = extern(int);
			if (open)
				AbortInvalidFile();
			
			extern 'errno = 0;';
			extern '${fp} = fopen(s_string_cdata(${path}), "wb");';
			extern '${result} = errno;';
			
			if (result != 0)
				return;
				
			open = true;
			outputstream = Maybe<OutputStream>(_create_output_stream(fp));
		}
		
		function openReadWrite(): (result: int)
		{
			result = extern(int);
			if (open)
				AbortInvalidFile();
			
			extern 'errno = 0;';
			extern '${fp} = fopen(s_string_cdata(${path}), "r+b");';
			extern '${result} = errno;';
			
			if (result != 0)
				return;
				
			open = true;
			inputstream = Maybe<InputStream>(_create_input_stream(fp));
			outputstream = Maybe<OutputStream>(_create_output_stream(fp));
		}
	};
}

function AbortInvalidFile()
	Abort("invalid operation on File object");
	
#endif
