.text
.global _start

_start:

	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx
	MOVL $1, %eax
	MOVL $0, %ebx
	/* Generate AND expression */
	ANDL %ebx, %eax
	MOVL %eax, temp0
	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx

	MOVL temp0, %eax
	MOVL %eax, r
	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx
	MOVL $0, %eax
	MOVL $0, %ebx
	/* Negate and increment to enforce the NOT */
	NEG  %ebx
	INC  %ebx
	/* Generate OR expression */
	ORL %ebx, %eax
	MOVL %eax, temp1
	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx

	MOVL temp1, %eax
	MOVL %eax, s
exit:
	mov $1, %eax
	mov $1, %ebx
	int $0x80


.data
x:	.int 1
z:	.int 0
r:	.int 0
s:	.int 0
temp0:	.int 0
temp1:	.int 0

__minus:  .byte '-'
__negOne: .int -1
__negFlag: .byte '+'
