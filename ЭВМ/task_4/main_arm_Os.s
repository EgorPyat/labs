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
.file	"main.c"
.global	__aeabi_dcmple
.section	.text.startup,"ax",%progbits
.align	2
.global	main
.type	main, %function
main:
@ args = 0, pretend = 0, frame = 0
@ frame_needed = 0, uses_anonymous_args = 0
cmp	r0, #3
stmfd	sp!, {r3, r4, r5, r6, r7, lr}
mov	r6, r1
bne	.L2
ldr	r0, [r1, #4]
bl	atof
mov	r4, r0
ldr	r0, [r6, #8]
mov	r5, r1
bl	atof
mov	r2, #0
mov	r6, r0
mov	r7, r1
mov	r0, r4
mov	r1, r5
mov	r3, #0
bl	__aeabi_dcmple
cmp	r0, #0
beq	.L7
ldr	r1, .L9
mov	r0, #1
bl	__printf_chk
mov	r0, #1
ldmfd	sp!, {r3, r4, r5, r6, r7, pc}
.L7:
mov	r2, r6
mov	r3, r7
mov	r0, r4
mov	r1, r5
bl	calc_ex
mov	r2, r0
mov	r3, r1
mov	r0, #1
ldr	r1, .L9+4
bl	__printf_chk
b	.L8
.L2:
mov	r0, #1
ldr	r1, .L9+8
bl	__printf_chk
.L8:
mov	r0, #0
ldmfd	sp!, {r3, r4, r5, r6, r7, pc}
.L10:
.align	2
.L9:
.word	.LC0
.word	.LC1
.word	.LC2
.size	main, .-main
.section	.rodata.str1.1,"aMS",%progbits,1
.LC0:
.ascii	"Enter N > 0\000"
.LC1:
.ascii	"e^x = %.10f\012\000"
.LC2:
.ascii	"Bad arguments\000"
.ident	"GCC: (Ubuntu/Linaro 4.7.4-2ubuntu1) 4.7.4"
.section	.note.GNU-stack,"",%progbits
