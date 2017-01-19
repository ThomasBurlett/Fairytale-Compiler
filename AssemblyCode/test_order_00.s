.text
.global _start

_start:

	MOVL $4, %eax
	MOVL $3, %ebx
	IMULL %ebx
	MOVL %eax, temp0
	MOVL $2, %eax
	MOVL temp0, %ebx
	ADD %ebx, %eax
	MOVL %eax, temp1
	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx

	MOVL temp1, %eax
	MOVL %eax, x
exit:
	mov $1, %eax
	mov $1, %ebx
	int $0x80


.data
x:	.int 0
temp0:	.int 0
temp1:	.int 0

__minus:  .byte '-'
__negOne: .int -1
__negFlag: .byte '+'
