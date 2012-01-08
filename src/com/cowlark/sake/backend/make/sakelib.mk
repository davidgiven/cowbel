# Only include the sake prologue once.
ifeq ($(flavor sake.empty),undefined)

sake.empty =
sake.space = $(sake.empty) $(sake.empty)
sake.comma = ,
sake.discard =

sake.tail = $(wordlist 2,$(words $1),$1)
sake.buttail = $(wordlist 2,$(words $1),x $1)

sake.reverse = \
	$(if $(strip $1), \
		$(call sake.reverse, \
			$(wordlist 2,$(words $1),$1) \
		) \
		$(firstword $1) \
	)

sake.boolean.true = T
sake.boolean.false =

sake.string.space = ~S
sake.string.comma = ~C
sake.string.tilde = ~T

sake.string.unwrap = $(subst ~T,~,$(subst ~C,$(sake.comma),$(subst ~S,$(sake.space),$1)))
sake.string.wrap = $(subst $(sake.space),~S,$(subst $(sake.comma),~C,$(subst ~,~T,$1)))

sake.method.string._add = $1$2
sake.method.string.print = $(info $(call sake.string.unwrap,$1))
sake.method.string.replace = $(subst $2,$3,$1)
sake.method.string._equals = $(if $(filter ~<$1~>,~<$2~>),$(sake.boolean.true),$(sake.boolean.false))

sake.method.boolean._not = $(if $1,$(sake.boolean.false),$(sake.boolean.true))
sake.method.boolean._and = $(and $1,$2)
sake.method.boolean._or = $(or $1,$2)
sake.method.boolean._xor = $(if $1,$(if $2,$(sake.boolean.false),$(sake.boolean.true)),$(if $2,$(sake.boolean.true),$(sake.boolean.false)))
sake.method.boolean.toString = $(if $1,true,false)

sake.method.integer._equals = $(call sake.maths.equals,$1,$2)
sake.method.integer._notequals = $(call sake.maths.notequals,$1,$2)
sake.method.integer._gt = $(call sake.maths.gt,$1,$2)
sake.method.integer._le = $(call sake.method.boolean._not,$(call sake.maths.gt,$1,$2))
sake.method.integer._ge = $(call sake.maths.ge,$1,$2)
sake.method.integer._lt = $(call sake.method.boolean._not,$(call sake.maths.ge,$1,$2))
sake.method.integer._add = $(call sake.maths.add,$1,$2)
sake.method.integer._sub = $(call sake.maths.sub,$1,$2)
sake.method.integer._negate = $(call sake.maths.negate,$1)
sake.method.integer.toString = $(call sake.maths.decode_sint,$1)

sake.maths.split_number = \
	$(subst 0,$(sake.space)0, \
	$(subst 1,$(sake.space)1, \
	$(subst 2,$(sake.space)2, \
	$(subst 3,$(sake.space)3, \
	$(subst 4,$(sake.space)4, \
	$(subst 5,$(sake.space)5, \
	$(subst 6,$(sake.space)6, \
	$(subst 7,$(sake.space)7, \
	$(subst 8,$(sake.space)8, \
	$(subst 9,$(sake.space)9, \
		$1))))))))))
		
sake.maths.reverse_sequence = \
	$(if $(word 1,$1), \
		$(call sake.maths.reverse_sequence, \
			$(wordlist 2,$(words $1),$1)) \
		$(word 1,$1) \
	)
	
sake.maths.encode_pint = \
	$(call sake.maths.reverse_sequence, \
		$(call sake.maths.split_number,$1))

sake.maths.decode_pint = \
	$(subst $(sake.space),,$(call sake.maths.reverse_sequence,$1))
	
sake.maths.encode_xs = $(wordlist 1,$1,x x x x x x x x x x)

# Construct the addition table.

$(foreach a,0 1 2 3 4 5 6 7 8 9, \
	$(foreach b,0 1 2 3 4 5 6 7 8 9, \
		$(foreach c,0 1, \
			$(eval sake.maths._add_table_$a$b$c = \
				$(call sake.maths.encode_pint, \
					$(words \
						$(call sake.maths.encode_xs, $a) \
						$(call sake.maths.encode_xs, $b) \
						$(call sake.maths.encode_xs, $c) \
					) \
				) \
			) \
		) \
	) \
)

# Add together up to three whitespace-separated digits.

sake.maths.digit_or_zero = $(if $1,$1,0)

sake.maths.add_digits = \
	$(sake.maths._add_table_$(call sake.maths.digit_or_zero,$(word 1,$1))$(call sake.maths.digit_or_zero,$(word 2,$1))$(call sake.maths.digit_or_zero,$(word 3,$1)))

# Add $1, $2 and the carry value $3.

sake.maths.add_pint_with_carry = \
	$(if $(or $(strip $1),$(strip $2),$(strip $3)), \
		$(call sake.maths.__accumulate_addition, \
			$(call sake.tail,$1), \
			$(call sake.tail,$2), \
			$(call sake.maths.add_digits, $(word 1,$1) $(word 1,$2) $3) \
		) \
	)

# $1 = tail of first int; $2 = tail of second int; $3 = new digit+carry pair
sake.maths.__accumulate_addition = \
	$(word 1,$3)$(call sake.maths.add_pint_with_carry,$1,$2,$(word 2,$3))

# To subtract, we use the Paul Kuliniewicz's bizarre algorithm from here:
# http://www.kuliniewicz.org/blog/archives/2007/05/24/crazy-subtraction-algorithm/
# This has the advantage that we can do nearly all our subtraction with
# additions.

sake.maths.__10_minus_0 = 10
sake.maths.__10_minus_1 = 9
sake.maths.__10_minus_2 = 8
sake.maths.__10_minus_3 = 7
sake.maths.__10_minus_4 = 6
sake.maths.__10_minus_5 = 5
sake.maths.__10_minus_6 = 4
sake.maths.__10_minus_7 = 3
sake.maths.__10_minus_8 = 2
sake.maths.__10_minus_9 = 1

sake.maths.__1_sub_1 = 0
sake.maths.__2_sub_1 = 1
sake.maths.__3_sub_1 = 2
sake.maths.__4_sub_1 = 3
sake.maths.__5_sub_1 = 4
sake.maths.__6_sub_1 = 5
sake.maths.__7_sub_1 = 6
sake.maths.__8_sub_1 = 7
sake.maths.__9_sub_1 = 8
sake.maths.__10_sub_1 = 9
sake.maths.__11_sub_1 = 10
sake.maths.__12_sub_1 = 11
sake.maths.__13_sub_1 = 12
sake.maths.__14_sub_1 = 13
sake.maths.__15_sub_1 = 14
sake.maths.__16_sub_1 = 15
sake.maths.__17_sub_1 = 16
sake.maths.__18_sub_1 = 17
sake.maths.__19_sub_1 = 18

sake.maths.__19_minus_10 = 9
sake.maths.__18_minus_10 = 8
sake.maths.__17_minus_10 = 7
sake.maths.__16_minus_10 = 6
sake.maths.__15_minus_10 = 5
sake.maths.__14_minus_10 = 4
sake.maths.__13_minus_10 = 3
sake.maths.__12_minus_10 = 2
sake.maths.__11_minus_10 = 1
sake.maths.__10_minus_10 = 0

sake.maths.__19_below_10 = $(sake.boolean.false)
sake.maths.__18_below_10 = $(sake.boolean.false)
sake.maths.__17_below_10 = $(sake.boolean.false)
sake.maths.__16_below_10 = $(sake.boolean.false)
sake.maths.__15_below_10 = $(sake.boolean.false)
sake.maths.__14_below_10 = $(sake.boolean.false)
sake.maths.__13_below_10 = $(sake.boolean.false)
sake.maths.__12_below_10 = $(sake.boolean.false)
sake.maths.__11_below_10 = $(sake.boolean.false)
sake.maths.__10_below_10 = $(sake.boolean.false)
sake.maths.__9_below_10 = $(sake.boolean.true)
sake.maths.__8_below_10 = $(sake.boolean.true)
sake.maths.__7_below_10 = $(sake.boolean.true)
sake.maths.__6_below_10 = $(sake.boolean.true)
sake.maths.__5_below_10 = $(sake.boolean.true)
sake.maths.__4_below_10 = $(sake.boolean.true)
sake.maths.__3_below_10 = $(sake.boolean.true)
sake.maths.__2_below_10 = $(sake.boolean.true)
sake.maths.__1_below_10 = $(sake.boolean.true)
sake.maths.__0_below_10 = $(sake.boolean.true)

# Replace each digit in the number with 10 minus that digit. (0s become a 10.)

sake.maths.__sub_mangle_b = \
	$(if $1, \
		$(sake.maths.__10_minus_$(word 1,$1)) \
			$(call sake.maths.__sub_mangle_b,$(call sake.tail,$1)) \
	)

sake.maths.digit_or_10 = $(if $1,$1,10)

sake.maths.__sub_sum = \
	$(if $(or $(strip $1),$(strip $2)), \
		$(words $(call sake.maths.encode_xs,$(word 1,$1)) $(call sake.maths.encode_xs,$(call sake.maths.digit_or_10,$(word 1,$2)))) \
		$(call sake.maths.__sub_sum,$(call sake.tail,$1),$(call sake.tail,$2)) \
	)  

sake.maths.__sub_decremented_tail = \
	$(if $(strip $1), \
		$(sake.maths.__$(word 1,$1)_sub_1), \
		$(error subtraction underflow) \
	) $(call sake.tail,$1)
	
sake.maths.__sub_ripple = \
	$(if $(findstring x0x,x$(strip $1)x), \
		, \
		$(if $(strip $1), \
			$(if $(sake.maths.__$(word 1,$1)_below_10), \
				$(word 1,$1) \
				$(call sake.maths.__sub_ripple, \
					$(call sake.maths.__sub_decremented_tail,$(call sake.tail,$1)) \
				), \
				$(sake.maths.__$(word 1,$1)_minus_10) \
				$(call sake.maths.__sub_ripple, \
					$(call sake.tail,$1) \
				) \
			) \
		) \
	)

sake.maths.sub_pint = \
	$(call sake.maths.__sub_ripple, \
		$(call sake.maths.__sub_sum, \
			$1, \
			$(call sake.maths.__sub_mangle_b, $2) \
		) \
	)

# Clean up a sint (remove trailing zeros, ensure that 'N 0' -> 'P 0', etc).

sake.maths.clean_sint = \
	$(call sake.maths.__n0_to_p0,$(call sake.maths.__strip_zeros,$1))

sake.maths.__n0_to_p0 = \
	$(strip \
		$(if \
			$(findstring x1x,x$(words $1)x), \
			P 0, \
			$1 \
		) \
	)
	
sake.maths.__strip_zeros = \
	$(if $(strip $1), \
		$(if \
			$(findstring x0x,x$(lastword $1)x), \
			$(call sake.maths.__strip_zeros,$(call sake.buttail,$1)), \
			$1 \
		) \
	)

# Is digit $1 equal to digit $2?
sake.maths.eq_digit = \
	$(strip \
		$(if \
			$(findstring x$2x,x$1x), \
			$(sake.boolean.true), \
			$(sake.boolean.false) \
		) \
	)
	
# Is digit $1 not equal to digit $2?
sake.maths.ne_digit = \
	$(strip \
		$(if \
			$(findstring x$2x,x$1x), \
			$(sake.boolean.false), \
			$(sake.boolean.true) \
		) \
	)

# Is digit $1 greater than or equal to digit $2?
sake.maths.ge_digit = \
	$(strip \
		$(if \
			$(findstring $(strip x $(call sake.maths.encode_xs,$2)), \
				x $(strip $(call sake.maths.encode_xs,$1)) \
			), \
			$(sake.boolean.true), \
			$(sake.boolean.false) \
		) \
	)

# Is digit $1 greater than digit $2?
sake.maths.gt_digit = \
	$(and $(call sake.maths.ne_digit,$1,$2),$(call sake.maths.ge_digit,$1,$2))

# Is pint $1 greater than pint $2?

sake.maths.gt_pint = \
	$(strip \
		$(if $(call sake.maths.eq_digit,$(words $1),$(words $2)), \
			$(call sake.maths.__gt_pint_digits, \
				$(call sake.reverse,$1), \
				$(call sake.reverse,$2) \
			), \
			$(if $(call sake.maths.gt_digit,$(words $1),$(words $2)), \
				$(sake.boolean.true), \
				$(sake.boolean.false) \
			) \
		) \
	)

sake.maths.__gt_pint_digits = \
	$(if $(strip $1), \
		$(if \
			$(call sake.maths.eq_digit,$(word 1,$1),$(word 1,$2)), \
			$(call sake.maths.__gt_pint_digits, \
				$(call sake.tail,$1), \
				$(call sake.tail,$2) \
			), \
			$(if \
				$(call sake.maths.gt_digit,$(word 1,$1),$(word 1,$2)), \
				$(sake.boolean.true), \
				$(sake.boolean.false) \
			) \
		), \
		$(sake.boolean.false) \
	)

# Is sint $1 equal to sint $2?

sake.maths.equals = \
	$(strip \
		$(if $(findstring x$(strip $1)x,x$(strip $2)x), \
			$(sake.boolean.true), \
			$(sake.boolean.false) \
		) \
	)
	
# Is sint $1 not equal to sint $2?

sake.maths.notequals = \
	$(strip \
		$(if $(findstring x$(strip $1)x,x$(strip $2)x), \
			$(sake.boolean.false), \
			$(sake.boolean.true) \
		) \
	)
	
# Is sint $1 greater than sint $2?

sake.maths.gt = \
	$(strip \
		$(call sake.maths.__gt_$(word 1,$1)$(word 1,$2), \
			$(call sake.tail,$1),$(call sake.tail,$2)) \
	)
	
sake.maths.__gt_PP = \
	$(if \
		$(call sake.maths.gt_pint,$1,$2), \
		$(sake.boolean.true), \
		$(sake.boolean.false) \
	)

sake.maths.__gt_NN = \
	$(if \
		$(call sake.maths.gt_pint,$1,$2), \
		$(sake.boolean.false), \
		$(sake.boolean.true) \
	)

sake.maths.__gt_PN = $(sake.boolean.true)
sake.maths.__gt_NP = $(sake.boolean.false)

# Is sint $1 greater than or equal to sint $2?

sake.maths.ge = \
	$(strip \
		$(or \
			$(call sake.maths.equals,$1,$2), \
			$(call sake.maths.gt,$1,$2) \
		) \
	)

# Encode/decode sints.

sake.maths.encode_sint = \
	$(strip \
		$(if \
			$(findstring -,$1), \
			N $(call sake.maths.encode_pint,$(subst -,,$1)), \
			P $(call sake.maths.encode_pint,$1) \
		) \
	)
				
sake.maths.decode_sint = \
	$(if $(findstring N,$(word 1,$1)),-)$(call sake.maths.decode_pint,$(call sake.tail,$1))
				
# Add two sints.

sake.maths.add = \
	$(call sake.maths.clean_sint, \
		$(call sake.maths.__add_$(word 1,$1)$(word 1,$2), \
			$(call sake.tail,$1), \
			$(call sake.tail,$2) \
		) \
	)
	
sake.maths.__add_PP = \
	P $(call sake.maths.add_pint_with_carry,$1,$2,0)  

sake.maths.__add_NN = \
	N $(call sake.maths.add_pint_with_carry,$1,$2,0)
	
sake.maths.__add_PN = \
	$(call sake.maths.sub,P $1,P $2)
	
sake.maths.__add_NP = \
	$(call sake.maths.sub,P $2,P $1)
	
# Subtract two sints.

sake.maths.sub = \
	$(call sake.maths.clean_sint, \
		$(call sake.maths.__sub_$(word 1,$1)$(word 1,$2), \
			$(call sake.tail,$1), \
			$(call sake.tail,$2) \
		) \
	)

sake.maths.__sub_PP = \
	$(if \
		$(call sake.maths.gt_pint,$1,$2), \
		P $(call sake.maths.sub_pint,$1,$2), \
		N $(call sake.maths.sub_pint,$2,$1) \
	) 

sake.maths.__sub_NN = \
	$(call sake.maths.__sub_PP,$2,$1)
	
sake.maths.__sub_PN = \
	P $(call sake.maths.add_pint_with_carry,$1,$2,0)
	
sake.maths.__sub_NP = \
	N $(call sake.maths.add_pint_with_carry,$1,$2,0)

# Flip the sign of a sint.

sake.maths.negate = \
	$(sake.maths.__neg_$(word 1,$1)) $(call sake.tail,$1)

sake.maths.__neg_P = N
sake.maths.__neg_N = P

endif
