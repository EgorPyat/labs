	.file	"rdiv.c"
	.text
	.type	read_time, @function
read_time:
.LFB2:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
#APP
# 9 "rdiv.c" 1
	rdtscp

# 0 "" 2
#NO_APP
	movl	%eax, -8(%rbp)
	movl	%edx, -4(%rbp)
	movl	-4(%rbp), %eax
	salq	$32, %rax
	movq	%rax, %rdx
	movl	-8(%rbp), %eax
	addq	%rdx, %rax
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE2:
	.size	read_time, .-read_time
	.section	.rodata
.LC8:
	.string	"Latency: %f\n"
.LC10:
	.string	"Throughput: %f\n"
	.text
	.globl	main
	.type	main, @function
main:
.LFB3:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$112, %rsp
	movq	$0, -40(%rbp)
	movsd	.LC0(%rip), %xmm0
	movsd	%xmm0, %xmm1 /*a - 1*/
	movsd	.LC1(%rip), %xmm0
	movsd	%xmm0, %xmm2 /*b - 2*/
	movsd	.LC2(%rip), %xmm0
	movsd	%xmm0, %xmm3 /*c - 3*/
	movsd	.LC3(%rip), %xmm0
	movsd	%xmm0, %xmm4 /*d - 4*/
	movsd	.LC4(%rip), %xmm0
	movsd	%xmm0, %xmm5 /*e - 5*/
	movsd	.LC5(%rip), %xmm0
	movsd	%xmm0, %xmm6 /*f - 6*/
	movl	$0, -100(%rbp)
	jmp	.L4
.L5:
	addl	$1, -100(%rbp)
.L4:
	cmpl	$1999999, -100(%rbp)
	jle	.L5
	call	read_time
	movq	%rax, -32(%rbp)
	movl	$0, -96(%rbp)
	jmp	.L6
.L7:
	divsd %xmm6, %xmm1
	divsd %xmm1, %xmm2
	divsd %xmm2, %xmm3
	divsd %xmm3, %xmm4
	divsd %xmm4, %xmm5
	divsd %xmm5, %xmm6
	addl	$1, -96(%rbp)
.L6:
	cmpl	$8999999, -96(%rbp)
	jle	.L7
	call	read_time
	movq	%rax, -24(%rbp)
	movq	-24(%rbp), %rax
	subq	-32(%rbp), %rax
	testq	%rax, %rax
	js	.L8
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rax, %xmm0
	jmp	.L9
.L8:
	movq	%rax, %rdx
	shrq	%rdx
	andl	$1, %eax
	orq	%rax, %rdx
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rdx, %xmm0
	addsd	%xmm0, %xmm0
.L9:
	movsd	.LC6(%rip), %xmm1
	divsd	%xmm1, %xmm0
	movsd	.LC7(%rip), %xmm1
	divsd	%xmm1, %xmm0
	leaq	.LC8(%rip), %rdi
	movl	$1, %eax
	call	printf@PLT
	call	read_time
	movq	%rax, -32(%rbp)
	movl	$0, -92(%rbp)
	jmp	.L10
.L11:
	divsd	%xmm3, %xmm2
	divsd	%xmm6, %xmm5
	addl	$1, -92(%rbp)
.L10:
	cmpl	$8999999, -92(%rbp)
	jle	.L11
	call	read_time
	movq	%rax, -24(%rbp)
	movq	-24(%rbp), %rax
	subq	-32(%rbp), %rax
	testq	%rax, %rax
	js	.L12
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rax, %xmm0
	jmp	.L13
.L12:
	movq	%rax, %rdx
	shrq	%rdx
	andl	$1, %eax
	orq	%rax, %rdx
	pxor	%xmm0, %xmm0
	cvtsi2sdq	%rdx, %xmm0
	addsd	%xmm0, %xmm0
.L13:
	movsd	.LC6(%rip), %xmm1
	divsd	%xmm1, %xmm0
	movsd	.LC9(%rip), %xmm1
	divsd	%xmm1, %xmm0
	leaq	.LC10(%rip), %rdi
	movl	$1, %eax
	call	printf@PLT
	movl	$0, %eax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE3:
	.size	main, .-main
	.section	.rodata
	.align 8
.LC0:
	.long	0
	.long	1073217536
	.align 8
.LC1:
	.long	0
	.long	1071644672
	.align 8
.LC2:
	.long	2576980378
	.long	1075288473
	.align 8
.LC3:
	.long	4236968058
	.long	1074340346
	.align 8
.LC4:
	.long	2233382994
	.long	-1070116373
	.align 8
.LC5:
	.long	208632331
	.long	1072693249
	.align 8
.LC6:
	.long	0
	.long	1096886920
	.align 8
.LC7:
	.long	0
	.long	1075314688
	.align 8
.LC9:
	.long	0
	.long	1073741824
	.ident	"GCC: (Ubuntu 6.3.0-12ubuntu2) 6.3.0 20170406"
	.section	.note.GNU-stack,"",@progbits
