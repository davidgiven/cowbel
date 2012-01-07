sake.empty =
sake.space = $(sake.empty) $(sake.empty)
sake.discard =

sake.boolean.true = T
sake.boolean.false =

sake.string.space = ~S
sake.string.comma = ~C
sake.string.tilde = ~T

sake.method.string._add = $(1)$(2)
sake.method.string.print = $(info $(1))
sake.method.string.replace = $(subst $(2),$(3),$(1))
sake.method.string._equals = $(if $(filter ~<$(1)~>,~<$2~>),$(sake.boolean.true),$(sake.boolean.false))
