import java.lang.*;
%%
%type Token
%eofval{
    return nextToken("", 0,yyline +1);
%eofval}
%line
%{
	public Token nextToken(String lex, int num,int line){
        return new Token(num, lex, line);
    }

    public Token nextToken(String lex, int num, int type, int line){
        return new Token(num, lex, type, line);
    }

    
%}

//IDENTIFICADORES
Digito = [0-9]
Letra = [a-zA-Z]
Letras_ = ({Letra}|\_)
Identificador = {Letras_}({Digito}|{Letras_})*

//Espacios
espacio=[ \r\n\tn]
esp=[ ]
esps = {espacio}+

//numeros enteros
Enteros = ((({Digito}+)(\_)?({Digito}+))+)|({Digito})+

//Decimales
Expo = [Ee][\+\-]?({Enteros})+
Decimal = (({Enteros}*\.{Digito}+)|({Enteros}+\.{Digito}*))
ExpoFlo = (({Decimal})({Expo}?))|(({Enteros})({Expo}))

// Cadenas
Carac = [\\\"]
Cadenas = (\"({Letra}|{Digito}|{Carac}|{esp}|{OP})+\"|\'({Letra}|{Digito}|{Carac}|{espacio}|{OP})+\')

//comentarios
inicioC = "<*"
finC = "*>"
CadenaC = (({Letra}|{Digito}|{Carac}|{esp}|{OP}|";"|"{"|"}"|":"|","|"."|{espacio}|"["|"]")+)
comentM = ({inicioC})({CadenaC})({finC}) //comentario Multilinea
CadenaU = (({Letra}|{Digito}|{Carac}|{esp}|{OP}|";"|"{"|"}"|":"|","|"."|"["|"]")+)
comentU = ("--")({CadenaU})

//Booleanos
True = true
False = false

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
Int = int
Float = float
Char =char
Double = double
While = while
Do = do
Switch=switch
Case=case
If=if
Else=else
Default=default
Break=break
Func=func
Void = void
Return = return
Print = print
Scan = scan

%%
{esps} {/*ignorar*/}
{comentM} {/*ignorar*/}
{comentU} {/*ignorar*/}
{Switch} {return nextToken(yytext(), 1,yyline +1);}
{Case} {return nextToken(yytext(), 2,yyline +1);}
{If} {return nextToken(yytext(), 3,yyline +1);}
{Int} {return nextToken(yytext(), 4,yyline +1);}
{Float} {return nextToken(yytext(), 5,yyline +1);}
{Char} {return nextToken(yytext(), 6,yyline +1);}
{Double} {return nextToken(yytext(), 7,yyline +1);}
{Do} {return nextToken(yytext(), 8,yyline +1);}
{While} {return nextToken(yytext(), 9,yyline +1);}
{Else} {return nextToken(yytext(), 10,yyline +1);}
{Default} {return nextToken(yytext(), 11,yyline +1);}
{Break} {return nextToken(yytext(), 12,yyline +1);}
{Return} {return nextToken(yytext(), 47,yyline +1);}
{Func} {return nextToken(yytext(), 13,yyline +1);}
{Void} {return nextToken(yytext(), 14,yyline +1);}//agregue void, y recorri los numeros por la precedencia La May 11/01/21
{Print} {return nextToken(yytext(),48,yyline +1);}
{Scan} {return nextToken(yytext(),49,yyline +1);}
";" {return nextToken(yytext(), 15,yyline +1);}
":" {return nextToken(yytext(), 16,yyline +1);}
"," {return nextToken(yytext(), 17,yyline +1);} //se agregaron los token faltantes JEF 11/01/21
"{" {return nextToken(yytext(), 18,yyline +1);} // Se agregaron los token faltantes AF 12/01/21
"}" {return nextToken(yytext(), 19,yyline +1);}
"[" {return nextToken(yytext(), 45,yyline +1);}
"]" {return nextToken(yytext(), 46,yyline +1);}
{Asig} {return nextToken(yytext(), 20,yyline +1);}
{Parentesisabre} {return nextToken(yytext(), 21,yyline +1);}
{Parentesiscierra} {return nextToken(yytext(), 22,yyline +1);}
{Incremento} {return nextToken(yytext(), 23,yyline +1);}
{Disyuncion} {return nextToken(yytext(), 24,yyline +1);}
{And} {return nextToken(yytext(), 25,yyline +1);}
{Igualque} {return nextToken(yytext(), 26,yyline +1);}
{Diferente} {return nextToken(yytext(), 27,yyline +1);}
{Menor} {return nextToken(yytext(), 28,yyline +1);}
{Mayor} {return nextToken(yytext(), 29,yyline +1);}
{Menorigual} {return nextToken(yytext(), 30,yyline +1);}
{Mayorigual} {return nextToken(yytext(), 31,yyline +1);}
{Suma} {return nextToken(yytext(), 32,yyline +1);}
{Resta} {return nextToken(yytext(), 33,yyline +1);}
{Mul} {return nextToken(yytext(), 34,yyline +1);}
{Div} {return nextToken(yytext(), 35,yyline +1);}
{Modulo} {return nextToken(yytext(), 36,yyline +1);}
{Negacion} {return nextToken(yytext(), 37,yyline +1);}
{Direccionmem} {return nextToken(yytext(), 38,yyline +1);}
{Enteros} {return nextToken(yytext(), 39,yyline +1);}
{ExpoFlo} {return nextToken(yytext(), 40,yyline +1);}
{Cadenas} {return nextToken(yytext(), 41,yyline +1);}
{True} {return nextToken(yytext(), 42,yyline +1);}
{False} {return nextToken(yytext(), 43,yyline +1);}
{Identificador} {return nextToken(yytext(), 44,yyline +1);}

. {System.out.println("Error Léxico en linea: "+(yyline+1)+" \nGenerado por: "+yytext()); System.exit(0);}
