default rel

global __PrintString
global __PrintlnString
global __PrintInt
global __PrintlnInt
global __ToString
global __GetString
global __GetInt
global __StringConcat
global __StringEqual
global __StringLess
global __StringParseInt
global __StringSubString
global __StringOrd
global main

extern strcmp
extern __stack_chk_fail
extern memcpy
extern __isoc99_scanf
extern __sprintf_chk
extern malloc
extern putchar
extern __printf_chk


SECTION .text   6

__PrintString:
        lea     rdx, [rdi+8H]
        mov     esi, L_012
        mov     edi, 1
        xor     eax, eax
        jmp     __printf_chk


        nop





ALIGN   16

__PrintlnString:
        lea     rdx, [rdi+8H]
        sub     rsp, 8
        mov     edi, 1
        mov     esi, L_012
        xor     eax, eax
        call    __printf_chk
        mov     edi, 10
        add     rsp, 8
        jmp     putchar







ALIGN   16

__PrintInt:
        mov     rdx, rdi
        mov     esi, L_013
        mov     edi, 1
        xor     eax, eax
        jmp     __printf_chk







ALIGN   16

__PrintlnInt:
        mov     rdx, rdi
        mov     esi, L_014
        mov     edi, 1
        xor     eax, eax
        jmp     __printf_chk







ALIGN   16

__ToString:
        push    rbp
        push    rbx
        mov     rbp, rdi
        mov     edi, 32
        sub     rsp, 8
        call    malloc
        lea     rdi, [rax+8H]
        mov     rbx, rax
        mov     r8, rbp
        mov     ecx, L_013
        mov     edx, 24
        mov     esi, 1
        xor     eax, eax
        call    __sprintf_chk
        cdqe
        mov     qword [rbx], rax
        add     rsp, 8
        mov     rax, rbx
        pop     rbx
        pop     rbp
        ret







ALIGN   16

__GetString:
        push    rbp
        push    rbx
        mov     esi, __buffer.3535
        mov     edi, L_012
        xor     eax, eax
        mov     ebx, __buffer.3535
        sub     rsp, 8
        call    __isoc99_scanf
L_001:  mov     edx, dword [rbx]
        add     rbx, 4
        lea     eax, [rdx-1010101H]
        not     edx
        and     eax, edx
        and     eax, 80808080H
        jz      L_001
        mov     edx, eax
        shr     edx, 16
        test    eax, 8080H
        cmove   eax, edx
        lea     rdx, [rbx+2H]
        mov     ecx, eax
        cmove   rbx, rdx
        add     cl, al
        sbb     rbx, L_011
        lea     edi, [rbx+8H]
        movsxd  rdi, edi
        call    malloc
        mov     rbp, rax
        lea     rdx, [rbx+1H]
        movsxd  rax, ebx
        lea     rdi, [rbp+8H]
        mov     qword [rbp], rax
        mov     esi, __buffer.3535
        call    memcpy
        add     rsp, 8
        mov     rax, rbp
        pop     rbx
        pop     rbp
        ret







ALIGN   16

__GetInt:
        sub     rsp, 24
        mov     edi, L_013


        mov     rax, qword [fs:abs 28H]
        mov     qword [rsp+8H], rax
        xor     eax, eax
        mov     rsi, rsp
        call    __isoc99_scanf
        mov     rdx, qword [rsp+8H]


        xor     rdx, qword [fs:abs 28H]
        mov     rax, qword [rsp]
        jnz     L_002
        add     rsp, 24
        ret

L_002:  call    __stack_chk_fail
        nop
ALIGN   16

__StringConcat:
        push    r15
        push    r14
        mov     r14, rdi
        push    r13
        push    r12
        mov     r13, rsi
        push    rbp
        push    rbx
        sub     rsp, 8
        mov     rbx, qword [rdi]
        mov     rbp, qword [rsi]
        lea     r15, [rbx+rbp]
        lea     rdi, [r15+9H]
        call    malloc
        test    rbx, rbx
        mov     r12, rax
        mov     qword [rax], r15
        jle     L_003
        lea     rdi, [rax+8H]
        lea     rsi, [r14+8H]
        mov     rdx, rbx
        call    memcpy
L_003:  test    rbp, rbp
        jle     L_005
        add     rbx, 8
        lea     rsi, [r13+8H]
        mov     rdx, rbp
        lea     rdi, [r12+rbx]
        call    memcpy
L_004:  add     rbp, r12
        mov     rax, r12
        mov     byte [rbp+rbx], 0
        add     rsp, 8
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        pop     r15
        ret





ALIGN   8
L_005:  add     rbx, 8
        jmp     L_004






ALIGN   8

__StringEqual:
        sub     rsp, 8
        add     rsi, 8
        add     rdi, 8
        call    strcmp
        test    eax, eax
        sete    al
        add     rsp, 8
        movzx   eax, al
        ret






ALIGN   8

__StringLess:
        sub     rsp, 8
        add     rsi, 8
        add     rdi, 8
        call    strcmp
        cdqe
        add     rsp, 8
        shr     rax, 63
        ret






ALIGN   8

__StringParseInt:
        movsx   edx, byte [rdi+8H]
        cmp     dl, 45
        jz      L_008
        lea     eax, [rdx-30H]
        cmp     al, 9
        ja      L_009
        lea     rcx, [rdi+8H]
        xor     edi, edi
L_006:  xor     eax, eax




ALIGN   16
L_007:  sub     edx, 48
        lea     rax, [rax+rax*4]
        add     rcx, 1
        movsxd  rdx, edx
        lea     rax, [rdx+rax*2]
        movsx   edx, byte [rcx]
        lea     esi, [rdx-30H]
        cmp     sil, 9
        jbe     L_007
        mov     rdx, rax
        neg     rdx
        test    edi, edi
        cmovne  rax, rdx
        ret





ALIGN   8
L_008:  movsx   edx, byte [rdi+9H]
        lea     rcx, [rdi+9H]
        lea     eax, [rdx-30H]
        cmp     al, 9
        ja      L_009
        mov     edi, 1
        jmp     L_006

L_009:  xor     eax, eax
        ret






ALIGN   8

__StringSubString:
        sub     esi, edi
        push    r14
        mov     r14d, edi
        lea     edi, [rsi+0AH]
        push    r13
        lea     r13d, [rsi+1H]
        push    r12
        push    rbp
        mov     r12, rdx
        push    rbx
        movsxd  rdi, edi
        mov     ebx, esi
        call    malloc
        test    r13d, r13d
        mov     rbp, rax
        movsxd  rax, r13d
        mov     qword [rbp], rax
        jle     L_010
        mov     ecx, ebx
        movsxd  rax, r14d
        lea     rdi, [rbp+8H]
        lea     rdx, [rcx+1H]
        lea     rsi, [r12+rax+8H]
        call    memcpy
L_010:  add     ebx, 9
        mov     rax, rbp
        movsxd  rbx, ebx
        mov     byte [rbp+rbx], 0
        pop     rbx
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        ret






ALIGN   8

__StringOrd:
        movsx   rax, byte [rsi+rdi+8H]
        ret



SECTION .data   


SECTION .bss    align=32

__buffer.3535:
        resb    3

L_011:
        resb    1048573


SECTION .text.unlikely 


SECTION .text.startup 6


SECTION .rodata.str1.1 

L_012:
        db 25H, 73H, 00H

L_013:
        db 25H, 6CH, 64H, 00H

L_014:
        db 25H, 6CH, 64H, 0AH, 00H


