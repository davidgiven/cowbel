# Written in 2014 by David Given.
#
# To the extent possible under law, the author has dedicated all copyright
# and related and neighboring rights to this software to the public domain
# worldwide. This software is distributed without any warranty.
#
# Please see the file COPYING.CC0 in the distribution package for more
# information.

define parser-test

parser-test-$1: bin/parser test/parser/$1.cow test/parser/$1.clean
	@echo PARSERTEST $$@
	$(hide)bin/parser < test/parser/$1.cow | aeson-pretty | diff -uBb test/parser/$1.clean -

tests += parser-test-$1
endef

tests :=
# No space after the comma here; make can't cope
$(eval $(call parser-test,empty))
$(eval $(call parser-test,assign-boolean))
$(eval $(call parser-test,assign-real))
$(eval $(call parser-test,assign-integer))
$(eval $(call parser-test,assign-identifier))
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

.PHONY: parser-tests $(tests)
parser-tests: $(tests)

