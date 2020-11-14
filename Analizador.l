import java.lang.*;
import java.io.*;
import java.util.*;
%%
%line
%column
%char
%unicode
%int
%{
public static final int IDENTIFIER=0, INT_LIT = 1, FLOAT_LIT = 2, IMAGINARY_LIT = 3;
public static final int STRING_LIT = 4, TRUE = 5, FALSE = 6,Asig = 7,Parentesisabre = 8;
public static final int Parentesiscierra = 9,Incremento = 10, Disyuncion = 11,And = 12;
public static final int Igualque = 13,Diferente = 14, Menor = 15,Mayor = 16, Menorigual = 17;
public static final int Mayorigual = 18, Suma = 19, Resta = 20,Mul = 21,Div = 22, Modulo = 23;
public static final int Negacion = 24, Direccionmem = 25;
public static final int Var=26,Const=27,Struct=28,Switch=29,Case=30,If=31,Else=32,Default=33,For=34;
public static final int Break=35,Uint8=36,Uint16=37,Int8=38,Int16=39,Int32=40,Float=41,Float64=42,Complex64=43;
public static final int Byte=44,String=45,Bool=46,Continue=47,Return=48,Package=49,Func=50,Defer=51;

%}

//IDENTIFICADORES
Digito = [0-9]
Letra = [a-zA-Z]
Letras_ = ({Letra}|\_)
identifier = {Letras_}({Digito}|{Letras_})*

//Espacios
espacio=[" "\n\tn]
esp = " "
esps = {espacio}+

//numeros enteros
int_lit = ((({Digito}+)(\_)?({Digito}+))+)|({Digito})+

//Decimales
Expo = [Ee][\+\-]?({int_lit})+
Decimal = (({int_lit}*\.{Digito}+)|({int_lit}+\.{Digito}*))
float_lit = (({Decimal})({Expo}?))|(({int_lit})({Expo}))

//imaginary_lit
imaginary_lit =((\-)?({int_lit}|{float_lit})\i)|(({int_lit}|{float_lit})(\+|\-)({int_lit}|{float_lit})\i)

// string_lit
Carac = [\\\"] 
string_lit = (\"({Letra}|{Digito}|{Carac}|{esp}|{OP})+\"|\'({Letra}|{Digito}|{Carac}|{espacio}|{OP})+\')

//Booleanos
True = "true"
False = "false"

//operadores
Asig = \=
Parentesisabre = \(
Parentesiscierra = \)
Incremento = "++"
Disyuncion = "||"
And = "&&"
Igualque = "=="
Diferente = "!="
Menor = "<"
Mayor = ">"
Menorigual = "<="
Mayorigual = ">="
Suma = \+
Resta = \-
Mul = \*
Div = \/
Modulo = \%
Negacion =\!
Direccionmem = \&
OP =({Asig}|{Parentesisabre}|{Parentesiscierra}|{Incremento}|{Disyuncion}|{And}|{Igualque}|{Diferente}|{Menor}|{Mayor}|{Menorigual}|{Mayorigual}|{Suma}|{Resta}|{Mul}|{Div}|{Modulo}|{Negacion}|{Direccionmem})

//palabras reservadas
Var=var
Const=const
Struct=struct
Switch=switch
Case=case
If=if
Else=else
Default=default
For=for
Break=break
Uint8=uint8
Uint16=uint16
Int8=int8
Int16=int16
Int32=int32
Float=float32
Float64=float64
Complex64=complex64
Byte=byte
String=string
Bool=bool
Continue=continue
Return=return 
Package=package
Func=func
Defer=defer 

%%

{esps} {/*ignorar*/}
{int_lit} {return(INT_LIT);}
{float_lit} {return(FLOAT_LIT);}
{imaginary_lit} {return(IMAGINARY_LIT);}
{string_lit} {return(STRING_LIT);}
{True} {return (TRUE);}
{False} {return(FALSE);}
{Asig} {return(Asig);}
{Parentesisabre} {return(Parentesisabre);}
{Parentesiscierra} {return(Parentesiscierra);} 
{Incremento} {return(Incremento);}
{Disyuncion} {return(Disyuncion);} 
{And} {return(And);}
{Igualque} {return(Igualque);}
{Diferente} {return(Diferente);}
{Menor} {return(Menor);}
{Mayor} {return(Mayor);}
{Menorigual} {return(Menorigual);}
{Mayorigual} {return(Mayorigual);}
{Suma} {return(Suma);}
{Resta} {return(Resta);}
{Mul} {return(Mul);}
{Div} {return(Div);}
{Modulo} {return(Modulo);}
{Negacion} {return(Negacion);} 
{Direccionmem} {return(Direccionmem);} 
{Var} {return(Var);}
{Const} {return(Const);}
{Struct} {return(Struct);}
{Switch} {return(Switch);}
{Case} {return(Case);}
{If} {return(If);}
{Else} {return(Else);}
{Default} {return(Default);}
{For} {return(For);}
{Break} {return(Break);}
{Uint8} {return(Uint8);}
{Uint16} {return(Uint16);}
{Int8} {return(Int8);}
{Int16} {return(Int16);}
{Int32} {return(Int32);}
{Float} {return(Float);}
{Float64} {return(Float64);}
{Complex64} {return(Complex64);}
{Byte} {return(Byte);}
{String} {return(String);}
{Bool} {return(Bool);}
{Continue} {return(Continue);}
{Return} {return(Return);}
{Package} {return(Package);}
{Func} {return(Func);}
{Defer} {return(Defer);}
{identifier} {return(IDENTIFIER);}


