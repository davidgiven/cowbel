# Written in 2014 by David Given.
#
# To the extent possible under law, the author has dedicated all copyright
# and related and neighboring rights to this software to the public domain
# worldwide. This software is distributed without any warranty.
#
# Please see the file COPYING.CC0 in the distribution package for more
# information.

define build-parser
$(call cfile, src/parser/main.c)
$(call yfile, src/parser/grammar.y)
$(call lfile, src/parser/lexer.l)
$(objdir)/src/parser/lexer.c: $(objdir)/src/parser/grammar.c
src/parser/main.c: $(objdir)/src/parser/lexer.h
endef

cflags := -Ilib/include -Isrc/parser -I$(OBJ)/parser/src/parser
ldflags := -ljansson -lunistring
objdir := $(OBJ)/parser
objs :=
exe := bin/cowbel-parser
$(eval $(build-parser))
$(eval $(clink))

