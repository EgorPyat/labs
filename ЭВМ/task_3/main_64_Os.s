	.file	"main.c"
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC1:
	.string	"Enter N > 0"
.LC2:
	.string	"e^x = %.10f\n"
.LC3:
	.string	"Bad arguments"
	.section	.text.unlikely,"ax",@progbits
.LCOLDB4:
	.section	.text.startup,"ax",@progbits
.LHOTB4:
	.globl	main
	.type	main, @function
main:
.LFB20:
	.cfi_startproc
	pushq	%rbx
	.cfi_def_cfa_offset 16
	.cfi_offset 3, -16
	subq	$16, %rsp
	.cfi_def_cfa_offset 32
	cmpl	$3, %edi
	jne	.L2
	movq	8(%rsi), %rdi
	movq	%rsi, %rbx
	call	atof
	movq	16(%rbx), %rdi
	movsd	%xmm0, 8(%rsp)
	call	atof
	movapd	%xmm0, %xmm1
	xorps	%xmm0, %xmm0
	movsd	8(%rsp), %xmm2
	ucomisd	%xmm2, %xmm0
	jb	.L7
	movl	$.LC1, %esi
	movl	$1, %edi
	xorl	%eax, %eax
	call	__printf_chk
	movl	$1, %eax
	jmp	.L5
.L7:
	movapd	%xmm2, %xmm0
	call	calc_ex
	movl	$.LC2, %esi
	movl	$1, %edi
	movb	$1, %al
	call	__printf_chk
	jmp	.L9
.L2:
	movl	$.LC3, %esi
	movl	$1, %edi
	xorl	%eax, %eax
	call	__printf_chk
.L9:
	xorl	%eax, %eax
.L5:
	addq	$16, %rsp
	.cfi_def_cfa_offset 16
	popq	%rbx
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE20:
	.size	main, .-main
	.section	.text.unlikely
.LCOLDE4:
	.section	.text.startup
.LHOTE4:
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
