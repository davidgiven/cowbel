sake.empty =
sake.space = $(sake.empty) $(sake.empty)
sake.discard =

sake.boolean.true = T
sake.boolean.false =

sake.string.space = ~S
sake.string.comma = ~C
sake.string.tilde = ~T

sake.string.unwrap = $1

sake.method.string._add = $1$2
sake.method.string.print = $(info $(call sake.string.unwrap,$1))
sake.method.string.replace = $(subst $2,$3,$1)
sake.method.string._equals = $(if $(filter ~<$1~>,~<$2~>),$(sake.boolean.true),$(sake.boolean.false))

sake.method.boolean._not = $(if $1,$(sake.boolean.false),$(sake.boolean.true))
sake.method.boolean._and = $(and $1,$2)
sake.method.boolean._or = $(or $1,$2)
sake.method.boolean._xor = $(if $1,$(if $2,$(sake.boolean.false),$(sake.boolean.true)),$(if $2,$(sake.boolean.true),$(sake.boolean.false)))
