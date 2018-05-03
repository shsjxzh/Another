grammar Mx;

@header {
package shsjxzh.compiler.Parser;
}

program
    :   decl* EOF
    ;

decl
    :   classDecl
    |   funcDecl
    |   fieldVarDecl
    ;

//Statement
stat
    :   blockStat
    |   exprStat
    |   selectStat
    |   iterStat
    |   jumpStat
    |   varDeclStat
    |   emptyStat           //这一个语句不会为它专门创建节点
    ;

blockStat
    : '{' stat* '}'
    ;

exprStat
    : expr ';'
    ;

selectStat
    :   IF judgeExpr stat elseBody?
    ;
/*
elseIfBody
    :   ELSE IF judgeExpr stat
    ;
*/
elseBody
    :   ELSE stat
    ;

iterStat
    :   WHILE judgeExpr stat                                                                     # while
    |   FOR '(' initE = expr? initV = varDecl? ';' cond = expr? ';' step = expr? ')' stat        # for
    ;

jumpStat
    :   BREAK ';'                           # break
    |   CONTINUE ';'                        # continue
    |   RETURN expr?';'                     # return
    ;

varDeclStat
    :   varDecl SEMI
    ;

emptyStat
    :   SEMI
    ;

judgeExpr
    :   '(' expr ')'
    ;

//Declaration
funcDecl
    :   (type|VOID) ID '(' formalParameters? ')' blockStat
    ;

formalParameters
    :   varDecl (',' varDecl)*
    ;
/*
formalParameter
    :   type ID
    ;
*/

classDecl
    :   CLASS ID '{' classStat* (constractDecl)? classStat* '}'
    ;

classStat
    :   fieldVarDecl
    |   funcDecl
    ;
/*classStat
    :   memberDecl
    |   methodDecl
    ;
memberDecl
    :   varDecl';'
    ;
methodDecl
    :   funcDecl
    ;
*/
constractDecl
    :   ID '('')' blockStat                             //考虑如何处理它
    ;

fieldVarDecl
    :   varDecl SEMI
    ;

varDecl
    :   type ID varInitializer?
    ;

varInitializer
    :   '=' expr
    ;
// Expression

expr
    :   expr '[' expr ']'                               # Index
    |   ID  '(' exprList? ')'                           # Call
    |   expr '.' ID                                     # FieldAccess
    |   expr '.' ID '(' exprList? ')'                   # ClassFuncAccess
    |   expr op = ('++'|'--')                           # PostfixSelfAddSub

    |   <assoc=right> op = ('+'|'-') expr               # Unary
    |   <assoc=right> op = ('++'|'--') expr             # Unary
    |   <assoc=right> op = ('!'|'~') expr               # Unary
    |   <assoc=right> NEW creator                       # New

    |   expr op = ('*'|'/'|'%') expr                    # Binary
    |   expr op = ('+'|'-') expr                        # Binary
    |   expr op = ('<<'|'>>') expr                      # Binary
    |   expr op = ('<='|'>='|'<'|'>') expr              # Binary
    |   expr op = ('=='|'!=') expr                      # Binary
    |   expr op = '&' expr                              # Binary
    |   expr op = '^' expr                              # Binary
    |   expr op = '|' expr                              # Binary
    |   expr op = '&&' expr                             # Binary
    |   expr op = '||' expr                             # Binary
    |   <assoc=right> expr op = '=' expr                # Binary
    |   literal                                         # Constant
    |   THIS                                            # This
    |   ID                                              # IDLeaf
    |   '(' expr ')'                                    # Bracket
    ;

exprList
    :   expr (',' expr)*
    ;

literal
    :   literalType = NULL_LITERAL
    |   literalType = INT_LITERAL
    |   literalType = STRING_LITERAL
    |   literalType = BOOL_LITERAL
    ;

creator
    :   nonArrayType ('('')')? (LBRACK expr RBRACK)+ (LBRACK RBRACK)+ (LBRACK expr RBRACK)+    # wrongCreator
    |   nonArrayType ('('')' | newDim)?                                                        # rightCreator
    ;

newDim
    :   (LBRACK expr RBRACK)+ (LBRACK RBRACK)*
    ;

nonArrayType
    :   BOOL
    |   INT
    |   STRING
    |   ID
    ;

type
    //:   nonArrayType DIM*
    :   nonArrayType (LBRACK RBRACK)*
    ;

//注意文本内容可能的歧义情况
// something more
// Literals
INT_LITERAL:        DIGIT+ ;

STRING_LITERAL:      '"' (ESC | .)*? '"' ;

BOOL_LITERAL:       TRUE|FALSE;

NULL_LITERAL:       NULL;

// lexer
// Keywords
BOOL:       'bool';
INT:        'int';
STRING:     'string';
NULL:       'null';
VOID:       'void';

TRUE:       'true';
FALSE:      'false';

IF:         'if';
ELSE:       'else';
FOR:        'for';
WHILE:      'while';

BREAK:      'break';
CONTINUE:   'continue';
RETURN:     'return';

NEW:        'new';
CLASS:      'class';
THIS:       'this';

// Operators
ADD:                '+';
SUB:                '-';
MUL:                '*';
DIV:                '/';
MOD:                '%';

GT:                 '>';
LT:                 '<';
EQ:                 '==';
LE:                 '<=';
GE:                 '>=';
NEQ:                '!=';

LOG_AND:            '&&';
LOG_OR:             '||';
LOG_NOT:            '!';

L_SHIFT:            '<<';
R_SHIFT:            '>>';
BIT_NOT:            '~';
BIT_AND:            '&';
BIT_OR:             '|';
BIT_XOR:            '^';

INC:                '++';
DEC:                '--';

ASSIGN:             '=';

//Seperators
LPAREN:             '(';
RPAREN:             ')';
LBRACE:             '{';
RBRACE:             '}';
LBRACK:             '[';
RBRACK:             ']';
SEMI:               ';';
COMMA:              ',';

// Comments
LINE_COMMENT  : '//' ~[\r\n]* -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;

// Whitespace
WS : [ \t\n\r]+ -> skip;

ID:                 LETTER (LETTER | DIGIT | '_')* ;

fragment
LETTER:         [a-zA-Z] ;
DIGIT:          [0-9];
ESC:            '\\' [nr"\\] ;  //\n, \r ...


