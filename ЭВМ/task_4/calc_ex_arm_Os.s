	.arch armv5t
	.fpu softvfp
	.eabi_attribute 20, 1
	.eabi_attribute 21, 1
	.eabi_attribute 23, 3
	.eabi_attribute 24, 1
	.eabi_attribute 25, 1
	.eabi_attribute 26, 2
	.eabi_attribute 30, 4
	.eabi_attribute 34, 0
	.eabi_attribute 18, 4
	.file	"calc_ex.c"
	.global	__aeabi_dcmpeq
	.global	__aeabi_ddiv
	.global	__aeabi_dmul
	.global	__aeabi_dadd
	.global	__aeabi_dcmplt
	.text
	.align	2
	.global	calc_ex
	.type	calc_ex, %function
calc_ex:
	@ args = 0, pretend = 0, frame = 8
	@ frame_needed = 0, uses_anonymous_args = 0
	stmfd	sp!, {r0, r1, r2, r4, r5, r6, r7, r8, r9, sl, fp, lr}
	mov	sl, r2
	stmia	sp, {r0-r1}
	mov	fp, r3
	mov	r0, r2
	mov	r1, r3
	mov	r2, #0
	mov	r3, #0
	bl	__aeabi_dcmpeq
	cmp	r0, #0
	bne	.L8
	ldr	r5, .L9
	mov	r4, #0
	mov	r8, r4
	mov	r9, r5
	mov	r6, r4
	mov	r7, r5
	b	.L4
.L5:
	mov	r2, r8
	mov	r3, r9
	mov	r0, sl
	mov	r1, fp
	bl	__aeabi_ddiv
	mov	r2, r0
	mov	r3, r1
	mov	r0, r4
	mov	r1, r5
	bl	__aeabi_dmul
	mov	r4, r0
	mov	r5, r1
	mov	r0, r6
	mov	r1, r7
	mov	r2, r4
	mov	r3, r5
	bl	__aeabi_dadd
	mov	r2, #0
	mov	r6, r0
	mov	r7, r1
	mov	r0, r8
	mov	r1, r9
	ldr	r3, .L9
	bl	__aeabi_dadd
	mov	r8, r0
	mov	r9, r1
.L4:
	mov	r0, r8
	mov	r1, r9
	ldmia	sp, {r2-r3}
	bl	__aeabi_dcmplt
	cmp	r0, #0
	bne	.L5
	b	.L2
.L8:
	ldr	r7, .L9
	mov	r6, #0
.L2:
	mov	r0, r6
	mov	r1, r7
	add	sp, sp, #12
	ldmfd	sp!, {r4, r5, r6, r7, r8, r9, sl, fp, pc}
.L10:
	.align	2
.L9:
	.word	1072693248
	.size	calc_ex, .-calc_ex
	.ident	"GCC: (Ubuntu/Linaro 4.7.4-2ubuntu1) 4.7.4"
	.section	.note.GNU-stack,"",%progbits
