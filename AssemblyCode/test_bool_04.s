.text
.global _start

_start:

	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx

	MOVL x, %eax
	NEG  %eax
	INC  %eax
	MOVL %eax, x
exit:
	mov $1, %eax
	mov $1, %ebx
	int $0x80


.data
x:	.int 1

__minus:  .byte '-'
__negOne: .int -1
__negFlag: .byte '+'
