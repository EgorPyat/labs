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
	pushq	%rbx //помещает значение регистра rdx в стек
	.cfi_def_cfa_offset 16 //modifies a rule for computing CFA. Register remains the same, but offset is new.
	.cfi_offset 3, -16 //Previous value of register is saved at offset offset from CFA. (cfi = call frame information) Регистр 3 лежит по смещению -16 от CFA
	subq	$16, %rsp Subtracts the immediate data from the destination operand, and stores the result in the destination location.
	.cfi_def_cfa_offset 32
	cmpl	$3, %edi //сравнение
	jne	.L2 //если не эквив. то переход
	movq	8(%rsi), %rdi //обратиться по адресу %rsi + 8 и снова поместить в %rsi
	movq	%rsi, %rbx
	call	atof // вызов atof
	movq	16(%rbx), %rdi
	movsd	%xmm0, 8(%rsp) //Перемещение скалярного Double в регистр
	call	atof
	movapd	%xmm0, %xmm1 //Перемещение упакованных выровненных Double
	xorps	%xmm0, %xmm0 //Поразрядное логическое исключающее ИЛИ над Float
	movsd	8(%rsp), %xmm2 //перемещение %rsp + 8 в %xmm0
	ucomisd	%xmm2, %xmm0 //Неупорядоченное скалярное сравнение Double с установкой флагов в EFLAGS
	jb	.L7 //n<=0 если выполнено то переход
	movl	$.LC1, %esi //поместить данные в LC1 в регистр
	movl	$1, %edi //поместить 1 в регистр
	xorl	%eax, %eax //зануление
	call	__printf_chk //вызов prinf/chk check for stack overflow before computing a result
	movl	$1, %eax
	jmp	.L5 //безусловный переход
.L7:
	movapd	%xmm2, %xmm0
	call	calc_ex //вызов функции calc
	movl	$.LC2, %esi //поместить данные из LC2 в регистр
	movl	$1, %edi
	movb	$1, %al
	call	__printf_chk //вызов функции print
	jmp	.L9
.L2:
	movl	$.LC3, %esi
	movl	$1, %edi
	xorl	%eax, %eax
	call	__printf_chk
.L9:
	xorl	%eax, %eax
.L5:
	addq	$16, %rsp //сложение
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
