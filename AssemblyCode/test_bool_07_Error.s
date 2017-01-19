.text
.global _start

_start:

Syntax error! Parsing token type 5 at line number 3
	/* Clear out EAX and EBX registers */
	XORL %eax, %eax
	XORL %ebx, %ebx

	MOVL x, %eax
	MOVL %eax, x
Syntax error! Parsing token type 9 at line number 3
Syntax error! Parsing token type 2 at line number 4
exit:
	mov $1, %eax
	mov $1, %ebx
	int $0x80


.data
x:	.int 1

__minus:  .byte '-'
__negOne: .int -1
__negFlag: .byte '+'
