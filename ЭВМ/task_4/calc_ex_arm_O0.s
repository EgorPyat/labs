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
	@ args = 0, pretend = 0, frame = 40
	@ frame_needed = 1, uses_anonymous_args = 0
	stmfd	sp!, {r4, fp, lr} //Push work registers and lr / начало функции
	add	fp, sp, #8 //fp = sp + 8
	sub	sp, sp, #44
	str	r0, [fp, #-44] //store with offset
	str	r1, [fp, #-40]
	str	r2, [fp, #-52]
	str	r3, [fp, #-48]
	mov	r3, #0
	ldr	r4, .L11
	str	r3, [fp, #-36]
	str	r4, [fp, #-32]
	mov	r3, #0
	ldr	r4, .L11 //load data from L11
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
	sub	r1, fp, #52
	ldmia	r1, {r0-r1} //pop / Registers are loaded in numerical order, with the lowest numbered register at the address initially in Rn.
	mov	r2, #0
	mov	r3, #0
	bl	__aeabi_dcmpeq // compare equal x!= 0
	mov	r3, r0
	cmp	r3, #0
	bne	.L9 // если не выполнено переход (not equal)
.L8:
	mov	r3, #0
	ldr	r4, .L11
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
	b	.L4 // вход в for
.L5: //цикл for
	sub	r1, fp, #52
	ldmia	r1, {r0-r1}
	sub	r3, fp, #28
	ldmia	r3, {r2-r3}
	bl	__aeabi_ddiv // вызов функции деления
	mov	r3, r0
	mov	r4, r1
	sub	r1, fp, #20
	ldmia	r1, {r0-r1}
	mov	r2, r3
	mov	r3, r4
	bl	__aeabi_dmul // вызов функции умножения
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-20]
	str	r4, [fp, #-16]
	sub	r1, fp, #36
	ldmia	r1, {r0-r1}
	sub	r3, fp, #20
	ldmia	r3, {r2-r3}
	bl	__aeabi_dadd // вызов функции сложения
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-36]
	str	r4, [fp, #-32]
	sub	r1, fp, #28
	ldmia	r1, {r0-r1}
	mov	r2, #0
	ldr	r3, .L11
	bl	__aeabi_dadd
	mov	r3, r0
	mov	r4, r1
	str	r3, [fp, #-28]
	str	r4, [fp, #-24]
.L4:
	sub	r1, fp, #28
	ldmia	r1, {r0-r1}
	sub	r3, fp, #44
	ldmia	r3, {r2-r3}
	bl	__aeabi_dcmplt //compare larger i < n
	mov	r3, r0
	cmp	r3, #0
	bne	.L5 // вход в тело цикла
	b	.L10 //при оконцании цикла переход в L10
.L9:
	sub	r4, fp, #36
	ldmia	r4, {r3-r4}
	b	.L7
.L10:
	sub	r4, fp, #36
	ldmia	r4, {r3-r4}
.L7:
	mov	r0, r3
	mov	r1, r4
	sub	sp, fp, #8
	ldmfd	sp!, {r4, fp, pc} //Pop work registers and pc / выход из функции
.L12:
	.align	2
.L11:
	.word	1072693248
	.size	calc_ex, .-calc_ex
	.ident	"GCC: (Ubuntu/Linaro 4.7.4-2ubuntu1) 4.7.4"
	.section	.note.GNU-stack,"",%progbits
