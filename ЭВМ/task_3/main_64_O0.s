	.file	"main.c"
	.section	.rodata
.LC1:
	.string	"Enter N > 0"
.LC2:
	.string	"e^x = %.10f\n"
.LC3:
	.string	"Bad arguments"
	.text
	.globl	main
	.type	main, @function
main:
.LFB2:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6 //modifes a rule for computing CFA. From now on register will be used instead of the old one. Offset remains the same.
	subq	$64, %rsp
	movl	%edi, -36(%rbp) # argc, argc
	movq	%rsi, -48(%rbp) # argv, argv
	cmpl	$3, -36(%rbp) //сравнение #, argc
	jne	.L2
	movq	-48(%rbp), %rax # argv, tmp93
	addq	$8, %rax
	movq	(%rax), %rax
	movq	%rax, %rdi
	call	atof
	movq	%xmm0, %rax
	movq	%rax, -24(%rbp)
	movq	-48(%rbp), %rax # argv, tmp95
	addq	$16, %rax
	movq	(%rax), %rax
	movq	%rax, %rdi
	call	atof
	movq	%xmm0, %rax
	movq	%rax, -16(%rbp)
	pxor	%xmm0, %xmm0
	ucomisd	-24(%rbp), %xmm0 # n, tmp97
	jb	.L8
	movl	$.LC1, %edi
	movl	$0, %eax
	call	printf
	movl	$1, %eax
	jmp	.L5
.L8:
	movsd	-16(%rbp), %xmm0 # x, tmp98
	movq	-24(%rbp), %rax # n, tmp99
	movapd	%xmm0, %xmm1
	movq	%rax, -56(%rbp)
	movsd	-56(%rbp), %xmm0
	call	calc_ex 					//вызов calc
	movq	%xmm0, %rax
	movq	%rax, -8(%rbp)
	movq	-8(%rbp), %rax # ex, tmp101
	movq	%rax, -56(%rbp)
	movsd	-56(%rbp), %xmm0
	movl	$.LC2, %edi
	movl	$1, %eax
	call	printf
	jmp	.L6
.L2:
	movl	$.LC3, %edi
	movl	$0, %eax
	call	printf
.L6:
	movl	$0, %eax
.L5:
	leave // destroys a stack frame
	.cfi_def_cfa 7, 8 // defines a rule for computing CFA as: take address from register and add offset to it.
	ret
	.cfi_endproc
.LFE2:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
