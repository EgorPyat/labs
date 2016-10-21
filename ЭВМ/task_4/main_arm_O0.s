  .arch armv5t
	.fpu softvfp
	.eabi_attribute 20, 1
	.eabi_attribute 21, 1
	.eabi_attribute 23, 3
	.eabi_attribute 24, 1
	.eabi_attribute 25, 1
	.eabi_attribute 26, 2
	.eabi_attribute 30, 6
	.eabi_attribute 34, 0
	.eabi_attribute 18, 4
	.file	"main.c"
	.global	__aeabi_dcmple
	.section	.rodata
	.align	2
.LC0:
	.ascii	"Enter N > 0\000"
	.align	2
.LC1:
	.ascii	"e^x = %.10f\012\000"
	.align	2
.LC2:
	.ascii	"Bad arguments\000"
	.text
	.align	2
	.global	main
	.type	main, %function
main:
	@ args = 0, pretend = 0, frame = 32
	@ frame_needed = 1, uses_anonymous_args = 0
	stmfd	sp!, {fp, lr}
	add	fp, sp, #4
	sub	sp, sp, #32
	str	r0, [fp, #-32]
	str	r1, [fp, #-36]
	ldr	r3, [fp, #-32]
	cmp	r3, #3 // n == 3
	bne	.L2
	ldr	r3, [fp, #-36]
	add	r3, r3, #4
	ldr	r3, [r3, #0]
	mov	r0, r3
	bl	atof
	str	r0, [fp, #-28]
	str	r1, [fp, #-24]
	ldr	r3, [fp, #-36]
	add	r3, r3, #8
	ldr	r3, [r3, #0]
	mov	r0, r3
	bl	atof
	str	r0, [fp, #-20]
	str	r1, [fp, #-16]
	sub	r1, fp, #28
	ldmia	r1, {r0-r1}
	mov	r2, #0
	mov	r3, #0
	bl	__aeabi_dcmple
	mov	r3, r0
	cmp	r3, #0
	beq	.L8
.L7:
	ldr	r0, .L9
	bl	printf
	mov	r3, #1
	b	.L5
.L8:
	sub	r1, fp, #28
	ldmia	r1, {r0-r1}
	sub	r3, fp, #20
	ldmia	r3, {r2-r3}
	bl	calc_ex
	str	r0, [fp, #-12]
	str	r1, [fp, #-8]
	ldr	r0, .L9+4
	sub	r3, fp, #12
	ldmia	r3, {r2-r3}
	bl	printf
	b	.L6
.L2:
	ldr	r0, .L9+8
	bl	printf
.L6:
	mov	r3, #0
.L5:
	mov	r0, r3
	sub	sp, fp, #4
	ldmfd	sp!, {fp, pc}
.L10:
	.align	2
.L9:
	.word	.LC0
	.word	.LC1
	.word	.LC2
	.size	main, .-main
	.ident	"GCC: (Ubuntu/Linaro 4.7.4-2ubuntu1) 4.7.4"
	.section	.note.GNU-stack,"",%progbits
