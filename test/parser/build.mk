define parser-test

parser-test-$1: bin/parser test/parser/$1.cow test/parser/$1.clean
	@echo TEST $$@
	$(hide)bin/parser < test/parser/$1.cow | aeson-pretty | diff -uBb test/parser/$1.clean -

tests += parser-test-$1
endef

tests :=
# No space after the comma here; make can't cope
$(eval $(call parser-test,empty))

.PHONY: parser-tests $(tests)
parser-tests: $(tests)

