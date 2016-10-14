	.file	"calc_ex.c"
	.section	.text.unlikely,"ax",@progbits //text unlikely перегруппировка для маловероятных выполняемых функций
.LCOLDB2:
	.text
.LHOTB2:
	.globl	calc_ex
	.type	calc_ex, @function
calc_ex:
.LFB20:
	.cfi_startproc
	xorps	%xmm2, %xmm2 //Поразрядное логическое исключающее ИЛИ над Float
	ucomisd	%xmm2, %xmm1 //Неупорядоченное скалярное сравнение Double с установкой флагов в EFLAGS
	movsd	.LC0(%rip), %xmm2 //Перемещение строки двойных слов / rip - регистр-счетчик команд / The data from .LC0 is loaded into XMM2
	jp	.L7 //Переход, если число единичных бит чётное
	movapd	%xmm2, %xmm3 //Перемещение упакованных выровненных Double
	je	.L2 //jump to .L2 if equal flag is set
.L7:
	movapd	%xmm2, %xmm5
	movapd	%xmm2, %xmm4
	movapd	%xmm2, %xmm3
.L4:
	ucomisd	%xmm4, %xmm0 //Неупорядоченное скалярное сравнение Double с установкой флагов в EFLAGS
	jbe	.L2 //Переход, если не выше/ниже или равно
	movapd	%xmm1, %xmm6
	divsd	%xmm4, %xmm6 //Скалярное деление Double
	addsd	%xmm2, %xmm4 
	mulsd	%xmm6, %xmm5
	addsd	%xmm5, %xmm3
	jmp	.L4 //Безусловный переход
.L2:
	movapd	%xmm3, %xmm0
	ret // Возврат из подпрограммы
	.cfi_endproc
.LFE20:
	.size	calc_ex, .-calc_ex
	.section	.text.unlikely
.LCOLDE2:
	.text
.LHOTE2:
	.section	.rodata.cst8,"aM",@progbits,8
	.align 8 //add enough padding bytes
.LC0:
	.long	0
	.long	1072693248
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
