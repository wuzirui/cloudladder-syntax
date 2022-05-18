parser grammar CL;

options {
    tokenVocab=CLLexer;
}


// expression
expression
    : primaryExpression                             # expPrimaryExpression
    | Function LParen paramList? RParen compoundStatement   # expFunctionExpression
    | expression LBrack expression RBrack           # expArrayAccess
    | expression LParen argumentList? RParen        # expFunctionCall
    | expression Dot Identifier                     # expFieldAccess
    | op=(Sub | Exclamation) expression             # expUnaryExpression
    | expression op=(Mul | Div | Mod) expression    # expBinaryExpression
    | expression op=(Add | Sub) expression          # expBinaryExpression
    | expression op=(LT | GT | LE | GE) expression  # expBinaryExpression
    | expression op=(Equal | NotEqual) expression   # expBinaryExpression
    | expression op=And expression                  # expBinaryExpression
    | expression op=Or expression                   # expBinaryExpression
    | expression op=(Pipe | Arrow) expression       # expPipeAndArrow
    ;
// FIRST: NumberLiteral, BoolLiteral, StringLiteral, LBrack, LBrace, LParen, Identifier, Function, Sub, Exclamation

argumentList
    : exp+=expression (Comma exp+=expression)*
    ;

primaryExpression
    : literal                                       # peLiteral
    | arrayLiteral                                  # peArrayLiteral
    | objLiteral                                    # peObjLiteral
    | LParen expression RParen                      # peParenExpression
    | Identifier                                    # peIdentifier
    ;
// FIRST: NumberLiteral, BoolLiteral, StringLiteral, LBrack, LBrace, LParen, Identifier

literal
    : NumberLiteral                                 # peNumberLiteral
    | BoolLiteral                                   # peBoolLiteral
    | StringLiteral                                 # peStringLiteral
    ;
// FIRST: NumberLiteral, BoolLiteral, StringLiteral
// ok

arrayLiteral
    : LBrack exp+=expression? (Comma exp+=expression)* Comma? RBrack
    ;
// FIRST: LBrack

objLiteral
    : LBrace item+=objItem? (Comma item+=objItem)* Comma? RBrace
    ;
// FIRST: LBrace

objItem
    : Identifier (Colon expression)?                # staticObjItem
    | StringLiteral (Colon expression)              # stringObjItem
    | LBrack expression RBrack Colon expression     # dynamicObjItem
    ;
// FIRST: Identifier, StringLiteral, LBrack

// statements
statement
    : selectionStatement
    | assignmentStatement Semi
    | dataStatement Semi
    | compoundStatement
    | breakStatement Semi
    | continueStatement Semi
    | iterationStatement
    | returnStatement
    | expressionStatement Semi
    ;
// FIRST: If, Identifier, NumberLiteral, BoolLiteral, StringLiteral, LBrack, LBrace, LParen, Let, Break, Continue,
//        Return, While, For, Function, Sub, Exclamation

expressionStatement
    : expression
    ;
// FIRST: NumberLiteral, BoolLiteral, StringLiteral, LBrack, LBrace, LParen, Identifier, Function, Sub, Exclamation

returnStatement
    : Return expression Semi
    ;
// FIRST: Return

breakStatement
    : Break Identifier?
    ;
// FIRST: Break

continueStatement
    : Continue Identifier?
    ;
// FIRST: Continue

dataStatement
    : Let dataStatementItemList
    ;
// FIRST: Let

dataStatementItemList
    : item+=dataStatementItem (Comma item+=dataStatementItem)*
    ;

dataStatementItem
    : Identifier (Assign expression)?
    ;
// FIRST: Identifer

compoundStatement
    : LBrace (item+=statement)* RBrace
    ;
// FIRST: LBrace

selectionStatement
    : If LParen expression RParen statement (Else statement)?
    ;
// FIRST: If

assignmentStatement
    : leftHandSideItem assignmentOperator expression
    ;

leftHandSideItem
    : Identifier                                    # assIdentifier
    | primaryExpression LBrack expression RBrack    # assignArray1
    | leftHandSideItem LBrack expression RBrack     # assignArray2
    | primaryExpression Dot Identifier              # assignField1
    | leftHandSideItem Dot Identifier               # assignField2
    ;
// FIRST: Identifier, NumberLiteral, BoolLiteral, StringLiteral, LBrack, LBrace, LParen

assignmentOperator
    : op=(Assign | AddAssign | SubAssign | MulAssign | DivAssign | ModAssign)
    ;
// FIRST: Assign, AddAssign, SubAssign, MulAssign, DivAssign, ModAssign

iterationStatement
    : (Identifier Colon)? While LParen expression RParen statement  # whileStatement
    | (Identifier Colon)? For LParen assignmentStatement Semi expression Semi assignmentStatement RParen statement    # forStatement
    | (Identifier Colon)? For LParen dataStatement Semi expression Semi assignmentStatement RParen statement # forDeclStatement
    ;
// FIRST: Identifer, While, For

functionDefinition
    : Function Identifier LParen paramList? RParen compoundStatement
    ;
// FIRST: Function

paramList
    : item+=Identifier (Comma item+=Identifier)*
    ;
// FIRST: Identifier

importStatement
    : Import path=StringLiteral (As as=Identifier)? Semi
    ;
// FIRST: Import

exportStatement
    : Export Identifier Assign expression
    ;
// FIRST: Export

program
    : item+=programItem*
    ;

programItem
    : functionDefinition
    | statement
    | importStatement
    | exportStatement
    ;
// FIRST: Function, Import, Export, ?