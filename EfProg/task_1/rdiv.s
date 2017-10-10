	.file	"rdiv.c"
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC2:
	.string	"Latency: %f\n"
.LC4:
	.string	"Throughput: %f\n"
	.section	.text.startup,"ax",@progbits
	.p2align 4,,15
	.globl	main
	.type	main, @function
main:
.LFB39:
	.cfi_startproc
	subq	$8, %rsp
	.cfi_def_cfa_offset 16
#APP
# 9 "rdiv.c" 1
	rdtscp

# 0 "" 2
#NO_APP
	movl	%edx, %ecx
	movl	%eax, %esi
#APP
# 9 "rdiv.c" 1
	rdtscp

# 0 "" 2
#NO_APP
	salq	$32, %rdx
	movl	%eax, %eax
	subq	%rsi, %rdx
	addq	%rdx, %rax
	movq	%rcx, %rdx
	salq	$32, %rdx
	subq	%rdx, %rax
	js	.L2
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rax, %xmm0
.L3:
	divsd	.LC0(%rip), %xmm0
	leaq	.LC2(%rip), %rsi
	movl	$1, %edi
	movl	$1, %eax
	divsd	.LC1(%rip), %xmm0
	call	__printf_chk@PLT
#APP
# 9 "rdiv.c" 1
	rdtscp

# 0 "" 2
#NO_APP
	movl	%edx, %ecx
	movl	%eax, %esi
#APP
# 9 "rdiv.c" 1
	rdtscp

# 0 "" 2
#NO_APP
	salq	$32, %rdx
	movl	%eax, %eax
	subq	%rsi, %rdx
	addq	%rdx, %rax
	movq	%rcx, %rdx
	salq	$32, %rdx
	subq	%rdx, %rax
	js	.L4
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rax, %xmm0
.L5:
	divsd	.LC0(%rip), %xmm0
	leaq	.LC4(%rip), %rsi
	movl	$1, %edi
	movl	$1, %eax
	mulsd	.LC3(%rip), %xmm0
	call	__printf_chk@PLT
	xorl	%eax, %eax
	addq	$8, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 8
	ret
.L2:
	.cfi_restore_state
	movq	%rax, %rdx
	pxor	%xmm0, %xmm0
	shrq	%rdx
	andl	$1, %eax
	orq	%rax, %rdx
	cvtsi2sdq	%rdx, %xmm0
	addsd	%xmm0, %xmm0
	jmp	.L3
.L4:
	movq	%rax, %rdx
	pxor	%xmm0, %xmm0
	shrq	%rdx
	andl	$1, %eax
	orq	%rax, %rdx
	cvtsi2sdq	%rdx, %xmm0
	addsd	%xmm0, %xmm0
	jmp	.L5
	.cfi_endproc
.LFE39:
	.size	main, .-main
	.section	.rodata.cst8,"aM",@progbits,8
	.align 8
.LC0:
	.long	0
	.long	1096886920
	.align 8
.LC1:
	.long	0
	.long	1075314688
	.align 8
.LC3:
	.long	0
	.long	1071644672
	.ident	"GCC: (Ubuntu 6.3.0-12ubuntu2) 6.3.0 20170406"
	.section	.note.GNU-stack,"",@progbits
