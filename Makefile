# Written in 2014 by David Given.
#
# To the extent possible under law, the author has dedicated all copyright
# and related and neighboring rights to this software to the public domain
# worldwide. This software is distributed without any warranty.
#
# Please see the file COPYING.CC0 in the distribution package for more
# information.

hide = @
.DELETE_ON_ERROR:

CC = gcc
LEMON = lemon
FLEX = flex
OBJ = .obj
BINDIR = bin

override CFLAGS += \
	-g \
	--std=gnu99 \
	-ffunction-sections \
	-fdata-sections

override LDFLAGS += \

all: bin/cowbel-parser bin/cowbel parser-tests

clean::

# --- Builds a single C file ------------------------------------------------

define cfile

$(objdir)/$(1:.c=.o): $1 Makefile
	@echo CC $$@
	@mkdir -p $$(dir $$@)
	$(hide)$(CC) $(CFLAGS) $(cflags) $(INCLUDES) -c -o $$@ $1

$(objdir)/$(1:.c=.d): $1 Makefile
	@echo DEPEND $$@
	@mkdir -p $$(dir $$@)
	$(hide)$(CC) $(CFLAGS) $(cflags) $(INCLUDES) \
		-MP -MM -MT $(objdir)/$(1:.c=.o) -MF $$@ $1

DEPENDS += $(objdir)/$(1:.c=.d)
objs += $(objdir)/$(1:.c=.o)
endef

# --- Processes and builds a Lemon file -------------------------------------

define yfile

$(objdir)/$(1:.y=.h): $(objdir)/$(1:.y=.c)
$(objdir)/$(1:.y=.c): $1 Makefile
	@echo LEMON $$@
	@mkdir -p $$(dir $$@)
	$(hide)$(LEMON) $1
	$(hide)mv $(1:.y=.c) $(1:.y=.h) $$(dir $$@)

clean::
	@echo CLEAN $(objdir)/$(1:.y=.c)
	$(hide)rm -f $(objdir)/$(1:.y=.c) $(objdir)/$(1:.y=.h)

$(call cfile, $(objdir)/$(1:.y=.c))
endef

# --- Processes and builds a Flex file --------------------------------------

define lfile

$(objdir)/$(1:.l=.h): $(objdir)/$(1:.l=.c)
$(objdir)/$(1:.l=.c): $1 Makefile
	@echo FLEX $$@
	@mkdir -p $$(dir $$@)
	$(hide)$(FLEX) \
		--outfile=$(objdir)/$(1:.l=.c) \
		--header-file=$(objdir)/$(1:.l=.h) $1

clean::
	@echo CLEAN $(objdir)/$(1:.l=.c)
	$(hide)rm -f $(objdir)/$(1:.l=.c) $(objdir)/$(1:.l=.h)

$(call cfile, $(objdir)/$(1:.l=.c))
endef

# --- Links a C program -----------------------------------------------------

define clink

$(exe): $(objs) Makefile
	@echo LINK $$@
	@mkdir -p $$(dir $$@)
	$(hide)$(CC) $(CFLAGS) $(cflags) $(LDFLAGS) -o $$@ $(objs) $(ldflags)

clean::
	@echo CLEAN $(exe)
	$(hide)rm -f $(exe) $(objs)
endef

# --- Deploys a binary ------------------------------------------------------

define copy
$2: $1
	@echo CP $$@
	@mkdir -p $$(dir $$@)
	$(hide) cp $$< $$@

objs += $2
endef

include src/parser/build.mk
include src/compiler/build.mk
include test/parser/build.mk
-include $(DEPENDS)
