	.file	"calc_ex.c"
	.section	.text.unlikely,"ax",@progbits
.LCOLDB3:
	.text
.LHOTB3:
	.globl	calc_ex
	.type	calc_ex, @function
calc_ex: 
.LFB20:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	fldl	8(%ebp) //загрузить вещественное число в стек
	fldl	16(%ebp)
	fldz //Загрузить число +0.0
	fxch	%st(1) //Поменять регистры местами
	fucomi	%st(1), %st //Неупорядоченное вещественное сравнение с установкой EFLAGS
	fstp	%st(1) //Записать и вытолкнуть вещественное число
	fld1 //Загрузить число +1.0
	jp	.L7
	je	.L8
.L7:
	fld1
	fld	%st(0)
	fld	%st(1)
	fxch	%st(5)
.L4:
	fucomi	%st(1), %st
	jbe	.L11
	fld	%st(4)
	fdiv	%st(2), %st //Деление вещественного числа
	fmulp	%st, %st(3) //умножение
	fxch	%st(5)
	fadd	%st(2), %st
	fxch	%st(1)
	fadd	%st(3), %st
	fxch	%st(1)
	fxch	%st(5)
	jmp	.L4
.L8:
	fstp	%st(1) //Записать и вытолкнуть вещественное число
	fstp	%st(1)
	jmp	.L2
.L11:
	fstp	%st(0)
	fstp	%st(0)
	fstp	%st(0)
	fstp	%st(0)
	fstp	%st(0)
.L2:
	popl	%ebp
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE20:
	.size	calc_ex, .-calc_ex
	.section	.text.unlikely
.LCOLDE3:
	.text
.LHOTE3:
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
