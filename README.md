# Fairytale-Compiler
This is a compiler for a programming language called Fairytale, which was created by me and Gianna Sheffield. Our code is able to take a Fairytale program and compile it into assembly code.

---------------------------------------------------

An example Fairytale hellworld program with correct syntax is as follows:

OnceUponATime

~s hello := "Hello World";

WRITE(hello);

TheEnd

---------------------------------------------------

The following is our Context Free Grammar that we dedsigned to form our language. *The little boxes should be arrows

<program >  #Start OnceUponATime <statement list> TheEnd

<statement list>  <statement> { <statement> }

<statement>  <ident> := <expression> ; #Assign #MustBeInSymbolTable

<statement>  <ident> := <string> ; #Assign #MustBeInSymbolTable

<statement>  <ident> := <boolean> ; #Assign #MustBeInSymbolTable

<statement>  <dataType> <ident> := intLiteral ; #Assign

<statement>  <dataType> <ident> := <ident> ; #Assign

<statement>  <dataType> <ident> := <boolLiteral> ; #Assign

<statement>  <dataType> <ident> := <stringLiteral> ; #Assign

<statement>  <dataType> <ident> ; #DeclareNotAssign

<statement>  methodName! #CallMethod

<statement>  <methodDef> #DefineMethod

<statement>  LOOP <intLiteral> => <statement list> DeLOOP #LoopNumTimes

<statement>  LOOP ( <conditional> ) => <statement list> DeLOOP #WhileLoop

<statement>  IF <conditional> => <statement list> ENDIF

<statement>  IFe <conditional> => <statement list> ELSE <statement list> ENDIF

<statement>  READ ( <id list> ) ; #ReadInVariables

<statement>  WRITE ( <id list> ) ; #WriteVariables

<statement>  WRITE ( <expr list> ) ; #WriteExpressions

<statement>  WRITE ( <string> ) ; #WriteStrings

<statement>  WRITE ( <bool list> ) ; #WriteBools

<methodDef>  DEF methodName <statementList> INED

<conditional>  <condStatement>

<conditional>  <condStatement> AND <conditional>

<conditional>  <condStatement> OR <conditional>

<condStatement>  <boolExpr>

<condStatement>  <expression> <comparison> <expression>

<comparison>  >

<comparison>  >=

<comparison>  <

<comparison>  <=

<comparison>  ==

<comparison>  !=

<id list>  <ident> {, <ident> } #ReadId #AllIdentsMustBeInSymbolTable

<expr list>  <expression> {, <expression> } #WriteExpr

<string list>  <stringLiteral> {, <stringLiteral> } #WriteString

<bool list>  <boolLiteral> {, <boolLiteral> } #WriteBoolean

<string>  stringLiteral { + stringLiteral #Concat }

<stringLiteral>  someString

<expression>  <factor> { <add op> <expression> }

<expression>  <factor> { <add op> <factor> }

<factor>  <primary> { <mult op> <primary> }

<factor>  <primary> { <mult op> <factor> }

<primary>  ( <expression> )

<primary>  <ident>

<primary>  IntLiteral #ProcessLiteral

<boolExpr>  <boolFactor>

<boolExpr>  <boolFactor> OR <boolExpr>

<boolFactor>  <boolean>

<boolFactor>  <boolean> AND <boolFactor>

<boolean>  <boolLiteral>

<boolean>  NOT <boolLiteral>

<boolean>  ( <boolExpr> )

<boolean>  <conditional>

<boolLiteral>  True #StoredAs1

<boolLiteral>  False #StoredAs0

<mult op>  MultOp #ProcessOp

<mult op>  DivOp #ProcessOp

<mult op>  Mod #ProcessOp

<add op>  PlusOp #ProcessOp

<add op>  MinusOp #ProcessOp

<ident>  Id #ProcessId

<data type>  ~i #Int

<data type>  ~s #String

<data type>  ~b #Boolean

<system goal>  <program > EofSym #Finish


