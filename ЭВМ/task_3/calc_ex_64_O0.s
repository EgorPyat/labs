	.file	"calc_ex.c"
	.text
	.globl	calc_ex
	.type	calc_ex, @function
calc_ex:
.LFB2:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movsd	%xmm0, -40(%rbp)
	movsd	%xmm1, -48(%rbp)
	movsd	.LC0(%rip), %xmm0
	movsd	%xmm0, -24(%rbp)
	movsd	.LC0(%rip), %xmm0
	movsd	%xmm0, -8(%rbp)
	pxor	%xmm0, %xmm0
	ucomisd	-48(%rbp), %xmm0
	jp	.L8
	pxor	%xmm0, %xmm0
	ucomisd	-48(%rbp), %xmm0
	je	.L9
.L8:
	movsd	.LC0(%rip), %xmm0
	movsd	%xmm0, -16(%rbp)
	jmp	.L4
.L5:
	movsd	-48(%rbp), %xmm0
	divsd	-16(%rbp), %xmm0
	movsd	-8(%rbp), %xmm1
	mulsd	%xmm1, %xmm0
	movsd	%xmm0, -8(%rbp)
	movsd	-24(%rbp), %xmm0
	addsd	-8(%rbp), %xmm0
	movsd	%xmm0, -24(%rbp)
	movsd	-16(%rbp), %xmm1
	movsd	.LC0(%rip), %xmm0
	addsd	%xmm1, %xmm0
	movsd	%xmm0, -16(%rbp)
.L4:
	movsd	-40(%rbp), %xmm0
	ucomisd	-16(%rbp), %xmm0
	ja	.L5
	jmp	.L10
.L9:
	movsd	-24(%rbp), %xmm0
	jmp	.L7
.L10:
	movsd	-24(%rbp), %xmm0
.L7:
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE2:
	.size	calc_ex, .-calc_ex
	.section	.rodata
	.align 8
.LC0:
	.long	0
	.long	1072693248
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
