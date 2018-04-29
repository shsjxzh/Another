grammar Mx;

@header {
package shsjxzh.compiler.Parser;
}

program
    :   decl* EOF
    ;

decl
    :   classDecl           //# classDeclLabel
    |   funcDecl            //# funcDeclLabel
    |   varDecl';'          //# varDeclLabel
    ;

//Statement
stat
    :   blockStat           # block
    |   exprStat            # expression
    |   selectStat          # select
    |   iterStat            # iter
    |   jumpStat            # jump
    |   varDecl';'          # vardecl
    |   ';'                 # empty
    ;

blockStat
    : '{' stat* '}'
    ;

exprStat
    : expr ';'
    ;

selectStat
    :   IF judgeExpr stat (ELSE IF judgeExpr stat)* (ELSE stat)?
    ;

iterStat
    :   WHILE judgeExpr stat                                    # while
    |   FOR '(' (expr)? ';' (expr)? ';' (expr)? ')' stat        # for
    |   FOR '(' (varDecl)? ';' (expr)? ';' (expr)? ')' stat     # for
    ;

jumpStat
    :   BREAK ';'
    |   CONTINUE ';'
    |   RETURN expr?';'
    ;

judgeExpr
    :   '(' expr ')' ;

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
    :   CLASS ID '{' (classStat)* (constractDecl)? (classStat)* '}'
    ;

classStat
    :   varDecl';'
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

varDecl
    :   type ID varInitializer?
    ;

varInitializer
    :   '=' expr
    ;
// Expression
// ToDo: 前4个修正
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

    |   expr op = ('*'|'/'|'%') expr                    # MultiDivModu
    |   expr op = ('+'|'-') expr                        # AddMinus
    |   expr op = ('<<'|'>>') expr                      # L_R_Shift
    |   expr op = ('<='|'>='|'<'|'>') expr              # Compare
    |   expr op = ('=='|'!=') expr                      # EqualCompare
    |   expr op = '&' expr                              # BitAnd
    |   expr op = '^' expr                              # BitXor
    |   expr op = '|' expr                              # BitOr
    |   expr op = '&&' expr                             # LogAnd
    |   expr op = '||' expr                             # LogOr
    |   <assoc=right> expr op = '=' expr                # Assign
    |   literal                                         # LiteralLeaf
    |   THIS                                            # This
    |   ID                                              # IDLeaf
    |   '(' expr ')'                                    # Bracket
    ;

exprList
    :   expr (',' expr)*
    ;

literal
    :   NULL_LITERAL
    |   INT_LITERAL
    |   STRING_LITERAL
    |   BOOL_LITERAL
    ;

creator
    :   nonArrayType ('[' expr ']')+ ('['']')*
    |   nonArrayType
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

