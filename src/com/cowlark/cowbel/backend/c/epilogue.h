/* BEGIN cowbel runtime library
 *
 * Written in 2012 by David Given.
 *
 * To the extent possible under law, the author of the cowbel runtime
 * library (of which this code, up to the string 'END cowbel runtime library',
 * is part), has dedicated all copyright and related and neighboring rights
 * to this software to the public domain worldwide. This software is
 * distributed without any warranty.
 *
 * Please see the file COPYING.CC0 in the distribution package for more
 * information.
 */

int main(int argc, const char* argv[])
{
	int i;

#if defined COWBEL_USE_GC
	GC_INIT();
#endif

	s_argc = argc;
	s_argv = S_ALLOC_GC(sizeof(s_string_t*) * s_argc);
	for (i = 0; i < argc; i++)
		s_argv[i] = s_create_string_constant(argv[i]);

	cowbel_main();
	return 0;
}

/* END cowbel runtime library */
