# Written in 2014 by David Given.
#
# To the extent possible under law, the author has dedicated all copyright
# and related and neighboring rights to this software to the public domain
# worldwide. This software is distributed without any warranty.
#
# Please see the file COPYING.CC0 in the distribution package for more
# information.

define deploy-compiler-lua
$(call copy, src/compiler/$1, $(bindir)/$1)
luas += $(bindir)/$1
endef

define build-compiler
$(call deploy-compiler-lua,main.lua)
$(call deploy-compiler-lua,Utils.lua)
$(call deploy-compiler-lua,AST.lua)
$(call deploy-compiler-lua,Symbols.lua)
endef

clean::
	@echo CLEAN $(objs)
	$(hide)rm -f $(objs)

bindir = $(BINDIR)/cowbel-compiler
objs :=
luas :=
$(eval $(call build-compiler))
$(BINDIR)/cowbel: $(luas)
$(eval $(call copy, src/compiler/cowbel, $(BINDIR)/cowbel))

clean::
	@echo CLEAN $(BINDIR)/cowbel
	$(hide)rm -f $(luas) $(BINDIR)/cowbel

