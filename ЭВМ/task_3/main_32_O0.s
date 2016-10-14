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
	leal	4(%esp), %ecx
	.cfi_def_cfa 1, 0
	andl	$-16, %esp
	pushl	-4(%ecx)
	pushl	%ebp
	.cfi_escape 0x10,0x5,0x2,0x75,0
	movl	%esp, %ebp
	pushl	%ebx
	pushl	%ecx
	.cfi_escape 0xf,0x3,0x75,0x78,0x6
	.cfi_escape 0x10,0x3,0x2,0x75,0x7c
	subl	$32, %esp
	movl	%ecx, %ebx
	cmpl	$3, (%ebx)
	jne	.L2
	movl	4(%ebx), %eax
	addl	$4, %eax
	movl	(%eax), %eax
	subl	$12, %esp
	pushl	%eax
	call	atof
	addl	$16, %esp
	fstpl	-32(%ebp)
	movl	4(%ebx), %eax
	addl	$8, %eax
	movl	(%eax), %eax
	subl	$12, %esp
	pushl	%eax
	call	atof
	addl	$16, %esp
	fstpl	-24(%ebp)
	fldz
	fldl	-32(%ebp)
	fxch	%st(1)
	fucomip	%st(1), %st
	fstp	%st(0)
	jb	.L8
	subl	$12, %esp
	pushl	$.LC1
	call	printf
	addl	$16, %esp
	movl	$1, %eax
	jmp	.L5
.L8:
	pushl	-20(%ebp)
	pushl	-24(%ebp)
	pushl	-28(%ebp)
	pushl	-32(%ebp)
	call	calc_ex
	addl	$16, %esp
	fstpl	-16(%ebp)
	subl	$4, %esp
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	$.LC2
	call	printf
	addl	$16, %esp
	jmp	.L6
.L2:
	subl	$12, %esp
	pushl	$.LC3
	call	printf
	addl	$16, %esp
.L6:
	movl	$0, %eax
.L5:
	leal	-8(%ebp), %esp
	popl	%ecx
	.cfi_restore 1
	.cfi_def_cfa 1, 0
	popl	%ebx
	.cfi_restore 3
	popl	%ebp
	.cfi_restore 5
	leal	-4(%ecx), %esp
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE2:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
