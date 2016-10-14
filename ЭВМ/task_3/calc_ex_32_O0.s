	.file	"calc_ex.c"
	.text
	.globl	calc_ex
	.type	calc_ex, @function
calc_ex:
.LFB2:
	.cfi_startproc
	pushl	%ebp 
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$48, %esp
	movl	8(%ebp), %eax
	movl	%eax, -40(%ebp)
	movl	12(%ebp), %eax
	movl	%eax, -36(%ebp)
	movl	16(%ebp), %eax
	movl	%eax, -48(%ebp)
	movl	20(%ebp), %eax
	movl	%eax, -44(%ebp)
	fld1
	fstpl	-24(%ebp)
	fld1
	fstpl	-8(%ebp)
	fldl	-48(%ebp)
	fldz
	fucomip	%st(1), %st
	fstp	%st(0)
	jp	.L8
	fldl	-48(%ebp)
	fldz
	fucomip	%st(1), %st
	fstp	%st(0)
	je	.L9
.L8:
	fld1
	fstpl	-16(%ebp)
	jmp	.L4
.L5:
	fldl	-48(%ebp)
	fdivl	-16(%ebp)
	fldl	-8(%ebp)
	fmulp	%st, %st(1)
	fstpl	-8(%ebp)
	fldl	-24(%ebp)
	faddl	-8(%ebp)
	fstpl	-24(%ebp)
	fldl	-16(%ebp)
	fld1
	faddp	%st, %st(1)
	fstpl	-16(%ebp)
.L4:
	fldl	-40(%ebp)
	fldl	-16(%ebp)
	fxch	%st(1)
	fucomip	%st(1), %st
	fstp	%st(0)
	ja	.L5
	jmp	.L10
.L9:
	fldl	-24(%ebp)
	jmp	.L7
.L10:
	fldl	-24(%ebp)
.L7:
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE2:
	.size	calc_ex, .-calc_ex
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
