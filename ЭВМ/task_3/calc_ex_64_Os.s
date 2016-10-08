	.file	"calc_ex.c"
	.section	.text.unlikely,"ax",@progbits
.LCOLDB2:
	.text
.LHOTB2:
	.globl	calc_ex
	.type	calc_ex, @function
calc_ex:
.LFB20:
	.cfi_startproc
	xorps	%xmm2, %xmm2
	ucomisd	%xmm2, %xmm1
	movsd	.LC0(%rip), %xmm2
	jp	.L7
	movapd	%xmm2, %xmm3
	je	.L2
.L7:
	movapd	%xmm2, %xmm5
	movapd	%xmm2, %xmm4
	movapd	%xmm2, %xmm3
.L4:
	ucomisd	%xmm4, %xmm0
	jbe	.L2
	movapd	%xmm1, %xmm6
	divsd	%xmm4, %xmm6
	addsd	%xmm2, %xmm4
	mulsd	%xmm6, %xmm5
	addsd	%xmm5, %xmm3
	jmp	.L4
.L2:
	movapd	%xmm3, %xmm0
	ret
	.cfi_endproc
.LFE20:
	.size	calc_ex, .-calc_ex
	.section	.text.unlikely
.LCOLDE2:
	.text
.LHOTE2:
	.section	.rodata.cst8,"aM",@progbits,8
	.align 8
.LC0:
	.long	0
	.long	1072693248
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.2) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
