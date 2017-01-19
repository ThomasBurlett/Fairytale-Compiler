.text
.global _start

_start:

	movl y,%eax
	cmpl $0, %eax
jne __nonzeroPrint1
	push $'0'
	movl $4, %eax       /* The system call for write (sys_write) */
	movl $1, %ebx       /* File descriptor 1 - standard output */
	movl $1, %edx     /* Place number of characters to display */
	leal (%esp), %ecx   /* Put effective address of zero into ecx */
	int $0x80	    /* Call to the Linux OS */
popl %eax
	jmp __writeExit   /* Needed to jump over the reversePrint code since we printed the zero */ 
__nonzeroPrint1:
	pushl %eax
	call __reversePrint    /* The return address is at top of stack! */
	popl  %eax    /* Remove value pushed onto the stack */
	jmp __writeExit
__reversePrint: 
	/* Save registers this method modifies */
	pushl %eax
	pushl %edx
	pushl %ecx
	pushl %ebx
	cmpw $0, 20(%esp)
	jge __positive
	/* Display minus on console */
	movl $4, %eax       /* The system call for write (sys_write) */
	movl $1, %ebx       /* File descriptor 1 - standard output */
	movl $1, %edx     /* Place number of characters to display */
	movl $__minus, %ecx   /* Put effective address of stack into ecx */
	int $0x80	    /* Call to the Linux OS */
	__positive:
	xorl %eax, %eax       /* eax = 0 */
	xorl %ecx, %ecx       /* ecx = 0, to track characters printed */
	/** Skip 16-bytes of register data stored on stack and 4 bytes
	of return address to get to first parameter on stack 
	*/   
	movw 20(%esp), %ax     /* ax = parameter on stack */
	cmpw $0, %ax
	jge __reverseLoop
	mulw __negOne

__reverseLoop:
	cmpw $0, %ax
	je   __reverseExit
	/* Do div and mod operations */
	movl $10, %ebx         /* ebx = 10 as divisor  */
	xorl %edx, %edx        /* edx = 0 to get remainder */
	idivl %ebx             /* edx = eax % 10, eax /= 10 */
	addb $'0', %dl         /* convert 0..9 to '0'..'9'  */
	decl %esp              /* use stack to store digit  */
	movb %dl, (%esp)       /* Save character on stack.  */
	incl %ecx              /* track number of digits.   */
	jmp __reverseLoop
__reverseExit:
__printReverse:
	/* Display characters on _stack_ on console */
	movl $4, %eax       /* The system call for write (sys_write) */
	movl $1, %ebx       /* File descriptor 1 - standard output */
	movl %ecx, %edx     /* Place number of characters to display */
	leal (%esp), %ecx   /* Put effective address of stack into ecx */
	int $0x80	    /* Call to the Linux OS */
	 /* Clean up data and registers on the stack */
	addl %edx, %esp
	popl %ebx
	popl %ecx
	popl %edx
	 popl %eax
	ret
__writeExit:
exit:
	mov $1, %eax
	mov $1, %ebx
	int $0x80


.data
y:	.int 0

__minus:  .byte '-'
__negOne: .int -1
__negFlag: .byte '+'
