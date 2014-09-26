# Written in 2014 by David Given.
#
# To the extent possible under law, the author has dedicated all copyright
# and related and neighboring rights to this software to the public domain
# worldwide. This software is distributed without any warranty.
#
# Please see the file COPYING.CC0 in the distribution package for more
# information.

define parser-test

$(objdir)/$1.dirty: bin/cowbel-parser test/parser/$1.cow test/parser/$1.clean
	@echo PARSERTEST $1
	@mkdir -p $$(dir $$@)
	$(hide)bin/cowbel-parser < test/parser/$1.cow | aeson-pretty > $$@ || true
	$(hide)diff -uBb test/parser/$1.clean $$@

tests += $(objdir)/$1.dirty
endef

define parser-test-fail

$(objdir)/$1.dirty: bin/cowbel-parser test/parser/$1.cow test/parser/$1.clean
	@echo PARSERTESTFAIL $1
	@mkdir -p $$(dir $$@)
	$(hide)bin/cowbel-parser <test/parser/$1.cow 2>$$@ >/dev/null || true
	$(hide)diff -uBb test/parser/$1.clean $$@

tests += $(objdir)/$1.dirty
endef

objdir := .obj/tests/parser
tests :=
# No space after the comma here; make can't cope
$(eval $(call parser-test,empty))
$(eval $(call parser-test,assign-boolean))
$(eval $(call parser-test,assign-real))
$(eval $(call parser-test,assign-integer))
$(eval $(call parser-test,assign-identifier))
$(eval $(call parser-test,assign-string))
$(eval $(call parser-test,assign-single-string))
$(eval $(call parser-test,infix))
$(eval $(call parser-test,prefix))
$(eval $(call parser-test,while))
$(eval $(call parser-test,object))
$(eval $(call parser-test,short-circuit))
$(eval $(call parser-test,simple-statements))
$(eval $(call parser-test,function))
$(eval $(call parser-test,object-interface))
$(eval $(call parser-test,typedef))
$(eval $(call parser-test,operators-vs-assign))
$(eval $(call parser-test,line))
$(eval $(call parser-test,extern))
$(eval $(call parser-test,methodcall))
$(eval $(call parser-test-fail,fail-semicolon))
$(eval $(call parser-test-fail,fail-missing-parenthesis))
$(eval $(call parser-test-fail,fail-missing-brace))
$(eval $(call parser-test-fail,fail-bad-operator))

.PHONY: parser-tests
parser-tests: $(tests)

