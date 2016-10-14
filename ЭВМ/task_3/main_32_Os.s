	.file	"main.c"
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC1:
	.string	"Enter N > 0"
.LC2:
	.string	"e^x = %.10f\n"
.LC3:
	.string	"Bad arguments"
	.section	.text.unlikely,"ax",@progbits
.LCOLDB5:
	.section	.text.startup,"ax",@progbits
.LHOTB5:
	.globl	main
	.type	main, @function
main:
.LFB20:
	.cfi_startproc
	leal	4(%esp), %ecx //Load effective address
	.cfi_def_cfa 1, 0
	andl	$-16, %esp //побитовое И
	pushl	-4(%ecx)
	pushl	%ebp
	.cfi_escape 0x10,0x5,0x2,0x75,0
	movl	%esp, %ebp
	pushl	%ebx
	pushl	%ecx
	.cfi_escape 0xf,0x3,0x75,0x78,0x6 //Позволяет пользователю добавлять произвольные байты в развернутой информацией
	.cfi_escape 0x10,0x3,0x2,0x75,0x7c
	subl	$16, %esp
	cmpl	$3, (%ecx) //сравнение с 3
	movl	4(%ecx), %ebx
	jne	.L2
	subl	$12, %esp
	pushl	4(%ebx)
	call	atof
	popl	%eax
	fstpl	-16(%ebp) //Записать и вытолкнуть вещественное число
	pushl	8(%ebx)
	call	atof
	fldz //Загрузить число +0.0
	addl	$16, %esp
	fldl	-16(%ebp) //загрузить вещественное число в стек
	fxch	%st(1) //Поменять регистры местами
	fucomip	%st(1), %st Неупорядоченное вещественное сравнение с установкой EFLAGS и выталкиванием
	jb	.L7
	fstp	%st(0) //Записать и вытолкнуть вещественное число
	fstp	%st(0)
	pushl	%ecx
	pushl	%ecx
	pushl	$.LC1
	pushl	$1
	call	__printf_chk
	addl	$16, %esp
	movl	$1, %eax
	jmp	.L5
.L7:
	fxch	%st(1)
	subl	$16, %esp
	fstpl	8(%esp) //Записать и вытолкнуть вещественное число
	fstpl	(%esp)
	call	calc_ex
	pushl	%edx
	pushl	%edx
	fstpl	(%esp)
	pushl	$.LC2
	pushl	$1
	call	__printf_chk
	addl	$32, %esp
	jmp	.L9
.L2:
	pushl	%eax
	pushl	%eax
	pushl	$.LC3
	pushl	$1
	call	__printf_chk
	addl	$16, %esp
.L9:
	xorl	%eax, %eax
.L5:
	leal	-8(%ebp), %esp
	popl	%ecx
	.cfi_restore 1 //говорит, что правило для регистра такие же, как это было в начале функции, после того, как все начальные инструкции добавлены .cfi_startproc были выполнены.
	.cfi_def_cfa 1, 0
	popl	%ebx
	.cfi_restore 3
	popl	%ebp
	.cfi_restore 5
	leal	-4(%ecx), %esp
	.cfi_def_cfa 4, 4 //определяет правило для вычисления CFA, как: принимать адрес из регистра и добавить смещение к нему.
	ret //выход из подпрограммы
	.cfi_endproc
.LFE20:
	.size	main, .-main
	.section	.text.unlikely
.LCOLDE5:
	.section	.text.startup
.LHOTE5:
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
