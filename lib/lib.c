#include <stdio.h>
#include <stdlib.h>
#include <string.h>
/*
        tmp = new Function("__PrintString");

        tmp = new Function("__PrintlnString");

        tmp = new Function("__PrintInt");

        tmp = new Function("__PrintlnInt");

        tmp = new Function("__ToString");

        tmp = new Function("__GetString");

        tmp = new Function("__GetInt");

        tmp = new Function("__StringConcat");

        tmp = new Function("__StringEqual");

        tmp = new Function("__StringLess");

        tmp = new Function("__StringParseInt");

        tmp = new Function("__StringSubString");

        tmp = new Function("__StringOrd");
*/

/* Thanks for Ding Yaoyao's help!*/

typedef long int64_type;
typedef char* pointer_type;

int main(){
    return 0;
}

void __PrintString(pointer_type str){
    printf("%s", str + 8);
}

void __PrintlnString(pointer_type str){
    printf("%s", str + 8);
    printf("\n");
}

void __PrintInt(int64_type a){
    printf("%d", a);
}

void __PrintlnInt(int64_type a){
    printf("%d\n", a);
}


pointer_type __ToString(int64_type a){
    //Todo!! why 32 bite
    pointer_type ret_value = malloc(8 + 24);
    *((int64_type*) ret_value) = sprintf(ret_value + 8, "%ld", a);
    return ret_value;
}

pointer_type __GetString(){
    //1 MB buffer
    static char __buffer[1024 * 1024];
    scanf("%s", __buffer);
    int len = strlen(__buffer);
    pointer_type ret_value = malloc(len + 8);
    *((int64_type *)ret_value) = len;
    strcpy(ret_value + 8, __buffer);
    return ret_value;
}

int64_type __GetInt(){
    int64_type ret_value;
    scanf("%ld", &ret_value);
    return ret_value;
}

pointer_type __StringConcat(pointer_type left, pointer_type right){
    int64_type l_len = *((int64_type*)left);
    int64_type r_len = *((int64_type*)right);
    pointer_type ret_value = malloc(l_len + r_len + 1 + 8);
    *((int64_type*)ret_value) = l_len + r_len;
    int i = 0;
    for (; i < l_len; ++i){
        ret_value[8 + i] = left[8 + i];
    }
    for (i = 0; i < r_len; ++i){
        ret_value[8 + l_len + i] = right[8 + i];
    }
    ret_value[8 + l_len + r_len] = '\0';
    return ret_value;
}

int64_type __StringEqual(pointer_type left, pointer_type right){
    return strcmp(left + 8, right + 8) == 0;
}

int64_type __StringLess(pointer_type left, pointer_type right){
    return strcmp(left + 8, right + 8) < 0;
}

//if this is not enough, then we can add __StringGreater, __StringGE, etc

int64_type __StringParseInt(pointer_type str){
    int64_type ret_value = 0;
    int neg_flag = 0;
    str += 8;
    if (*str == '-'){
        neg_flag = 1;
        ++str;
    }
    while('0' <= *str && *str <= '9'){
        ret_value = 10 * ret_value + (*str - '0');
        ++str;
    }
    if (neg_flag) return -ret_value;
    else return ret_value;
}

pointer_type __StringSubString(int left, int right, pointer_type str){
    int len = right - left + 1;
    pointer_type ret_value = malloc(8 + len + 1);
    *((int64_type*)ret_value) = len;
    int i;
    for(i = 0; i < len; i++){
        ret_value[8 + i] = str[8 + left + i];
    }
    ret_value[8 + len] = 0;
    return ret_value;
}

int64_type __StringOrd(int64_type pos, pointer_type str) {
	return str[8 + pos];
}
