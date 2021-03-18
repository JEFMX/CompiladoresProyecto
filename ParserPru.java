import java.io.IOException;
import java.lang.*;
public class ParserPru{
	public static final int SWITCH = 1;
	public static final int CASE = 2;
	public static final int IF = 3;
	public static final int INT = 4;
	public static final int FLOAT = 5;
	public static final int CHAR = 6;
	public static final int DOUBLE = 7;
	public static final int DO = 8;
	public static final int WHILE = 9;
	public static final int ELSE = 10;
	public static final int DEFAULT = 11;
	public static final int BREAK = 12;
	public static final int FUNC = 13;
	public static final int VOID = 14;
	public static final int PUNTOYC = 15;
	public static final int DOSPUNTOS = 16;
	public static final int COMA = 17;
	public static final int LLAVEA = 18;
	public static final int LLAVEC = 19;
	public static final int ASIG = 20;
	public static final int PARENTA = 21;
	public static final int PARENTC = 22;
	public static final int INCREMENTO = 23;
	public static final int DISYUNCION = 24;
	public static final int AND = 25;
	public static final int IGUALQUE = 26;
	public static final int DIFERENTE = 27;
	public static final int MENOR = 28;
	public static final int MAYOR = 29;
	public static final int MENORIGUAL = 30;
	public static final int MAYORIGUAL = 31;
	public static final int SUMA = 32;
	public static final int RESTA = 33;
	public static final int MUL = 34;
	public static final int DIV = 35;
	public static final int MODULO = 36;
	public static final int NEGACION = 37;
	public static final int DIRECCIONMEM= 38;
	public static final int ENTEROS = 39;
	public static final int EXPOFLO = 40;
	public static final int CADENA = 41;
	public static final int TRUE = 42;
	public static final int FALSE = 43;
	public static final int ID = 44;
	public static final int CORCHA = 45;
	public static final int CORCHC = 46;
	public static final int RETURN = 47;
	public static final int PRINT = 48;
	public static final int SCAN = 49;

	Yylex lexer;
	Token tokenActual;
	int acp = 0;//bandera para error
	int dir = 0; //contador de direcciones
	TabladeSimbolos TS;
	//Stack <TabladeSimbolos> pilaTS;  

	public ParserPru(Yylex lexer)throws IOException{
		this.lexer = lexer;
		TS = new TabladeSimbolos();
		//pilaTS = new Stack<String>();
	}

	public void init()throws IOException{
		this.tokenActual = lexer.yylex();
		programa();
		if (this.acp == 0 && tokenActual.equals(0))
			System.out.println("Cadena aceptada");
		else
			System.out.println("Error en la linea: "+tokenActual.linea+" Error sintactico");
	}

	void error(String msj)throws IOException{
		System.out.println("Error en la linea: "+tokenActual.linea+" "+msj);
		//System.out.println(msj);
		System.out.println(tokenActual.valor);

		this.acp = -1;
	}
	
	void programa()throws IOException{
		declaraciones();
		funciones();
	}
	//declaraciones → tipo lista_var; declaraciones | epsilon
	void declaraciones()throws IOException{
		if(tokenActual.equals(INT)|| tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)|| tokenActual.equals(DOUBLE)|| tokenActual.equals(VOID)){
			tipo();
			lista_var();
			if(tokenActual.equals(PUNTOYC)){
				tokenActual = lexer.yylex();
				declaraciones();
			}else{
				error("Error Sintáctico: Se esperaba ;");
			}
		}
	}

	//basico → int|float|char|double|void
	void basico()throws IOException{
 		if(tokenActual.equals(INT)|| tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)|| tokenActual.equals(DOUBLE)|| tokenActual.equals(VOID)){		
			tokenActual = lexer.yylex();
			
 		}
 		else
 		{
 			error("Error Sintáctico: No se reconoce el tipo de dato");
 		}
 	}
 	//tipo → basico compuesto
	void tipo()throws IOException{
			basico();
			compuesto();
	}
	//compuesto → (numero) compuesto | epsilon
	private void compuesto()throws IOException{
 		if(tokenActual.equals(CORCHA)){
 			tokenActual = lexer.yylex();
 			if(tokenActual.equals(ENTEROS)){
 				tokenActual = lexer.yylex();
 				if(tokenActual.equals(CORCHC)){
 					tokenActual = lexer.yylex();
 					compuesto();
 				}
 				else
 					error("Error Sintáctico: Se esperaba un ]");
 			}
 			else
 				error("Error Sintáctico: Se esperaba un entero ");
 		}
 		
 	}
 	/*lista_var → lista_var,id|id
		Recursividad izquierda:
		lista_var→ id lista_var’
		lista_var’→, id lista_var’|epsilon
 	*/
 	 void lista_var()throws IOException{
 		if(tokenActual.equals(ID)){
 			tokenActual = lexer.yylex();
 			lista_varP();
 		}
 		else
 			error("Error Sintáctico:Se esperaba un identificador");
 	}
//lista_var'→  , id lista_var'|epsilon
 	void lista_varP()throws IOException{
 		if(tokenActual.equals(COMA)){
 			tokenActual = lexer.yylex();
 			if(tokenActual.equals(ID)){
	 			tokenActual = lexer.yylex();
 				lista_varP();
 			}
 			else
 				error("Error Sintáctico: Se esperaba un identificador");
 		}
 		
 	}
 	// funciones → func tipo id (argumentos) bloque funciones | epsilon
 	void funciones()throws IOException{
 		if(tokenActual.equals(FUNC)){
 			tokenActual = lexer.yylex();
 			tipo();
 			if(tokenActual.equals(ID)){
 				tokenActual = lexer.yylex();
 				if(tokenActual.equals(PARENTA)){
 					tokenActual = lexer.yylex();
 					argumentos();
 					if(tokenActual.equals(PARENTC)){
						tokenActual = lexer.yylex();
 						bloque();
 						funciones();
 					}
 					else
 						error("Error Sintáctico: Se esperaba un )");
 				}
 				else
 					error("Error Sintáctico: Se esperaba un (");
 			}
 			else 
 				error("Error Sintáctico: Se esperaba un identificador");
 		}
 	}
 	// argumentos → lista_args|epsilon
	public void argumentos() throws IOException
	{
		if(tokenActual.equals(INT)||tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)||tokenActual.equals(DOUBLE)||tokenActual.equals(VOID))
			lista_args();
	}

	/*
		lista_args → lista_args, tipo id| tipo id
		ELIMINACION de recursividad
		lista_args → tipo id lista_args’
		lista_args’ → , tipo id lista_args’| epsilon
	*/
	// lista_args → tipo id lista_args’
	public void lista_args() throws IOException
	{
		tipo();
		if(tokenActual.equals(ID))
		{
			tokenActual = lexer.yylex();
			lista_argsP();
		}
		else
		{
			error("Error Sintáctico: se esperaba identificador");
		}
	}
	// lista_args’ → , tipo id lista_args’| epsilon
	public void lista_argsP() throws IOException
	{
		if(tokenActual.equals(COMA))
		{
			tokenActual = lexer.yylex();
			tipo();
			if(tokenActual.equals(ID))
			{
				tokenActual = lexer.yylex();
				lista_argsP();
			}
			else
			{
				error("Error Sintáctico: Se esperaba identificador");
			}
		}
		
	}

	// bloque → {declaraciones instrucciones}
	public void bloque() throws IOException
	{
		if(tokenActual.equals(LLAVEA))
		{

			tokenActual = lexer.yylex();
			declaraciones();
			instrucciones();
			if(tokenActual.equals(LLAVEC))
			{
				tokenActual = lexer.yylex();
			}
			else
			{
				error("Error Sintáctico: Se esperaba }");
			}
		}
		else
		{
			error("Error Sintáctico: Se esperaba {");
		}
	}

	/*
		instrucciones → instrucciones sentencia | sentencia
		ELIMINACION de recursividad
		instrucciones → sentencia instrucciones‘
		instrucciones’ → sentencia instrucciones‘|epsilon
	*/

	// instrucciones → sentencia instrucciones‘
	public void instrucciones() throws IOException
	{
		sentencia();
		instruccionesP();
	}

	// instrucciones’ → sentencia instrucciones‘|epsilon
	public void instruccionesP() throws IOException
	{
		if(tokenActual.equals(IF) || tokenActual.equals(WHILE) || tokenActual.equals(DO) || tokenActual.equals(BREAK) || tokenActual.equals(RETURN) || tokenActual.equals(SWITCH)){
			sentencia();
			instruccionesP();
		}
	}

	/*
		sentencia → parte_izquierda = bool ;| if( bool ) sentencia
					| if( bool ) sentencia else sentencia | while( bool ) sentencia
					| do sentencia while( bool ) | break ; | bloque | return exp ;| return;
					| switch( bool) {casos} | print exp;| scan parte_izquieda

	*/
	public void sentencia() throws IOException
	{
		switch(tokenActual.clase)
		{ 	
			case PRINT:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(NEGACION)||tokenActual.equals(RESTA)){
					exp();
				}
				else
					error("Error Sintáctico: Se esperaba un ! o un -");
			break;
			case SCAN:
				tokenActual = lexer.yylex();
				parte_izquierda();
			break;
			case IF:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(PARENTA))
				{
					tokenActual = lexer.yylex();
					bool();
					if(tokenActual.equals(PARENTC))
					{
						tokenActual = lexer.yylex();
						sentencia();
						if(tokenActual.equals(ELSE))
						{
							tokenActual = lexer.yylex();
							sentencia();
						}
					}
					else
					{
						error("Error Sintáctico: Se esperaba un )");
					}
				}
				else
				{
					error("Error Sintáctico: Se esperaba un (");
				}
			break;
			case WHILE:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(PARENTA))
				{
					tokenActual = lexer.yylex();
					bool();
					if(tokenActual.equals(PARENTC))
					{
						tokenActual = lexer.yylex();
						sentencia();
					}
					else
					{
						error("Error Sintáctico: Se esperaba un )");
					}
				}
				else
				{
					error("Error Sintáctico: Se esperaba un (");
				}
			break;
			case DO:
				tokenActual = lexer.yylex();
				sentencia();
				if(tokenActual.equals(WHILE))
				{
					tokenActual = lexer.yylex();
					if(tokenActual.equals(PARENTA))
					{
						tokenActual = lexer.yylex();
						bool();
						if(tokenActual.equals(PARENTC))
						{
							tokenActual = lexer.yylex();
						}
						else
						{
							error("Error Sintáctico: Se esperaba un )");
						}
					}
					else
					{
						error("Error Sintáctico: Se esperaba un (");
					}
				}
				else
				{
					error("Error Sintáctico: Se esperaba un WHILE");
				}
			break;
			case BREAK:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(PUNTOYC))
				{
					tokenActual = lexer.yylex();
				}
				else
				{
					error("Error Sintáctico: Se esperaba un ;");
				}
			break;
			case SWITCH:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(PARENTA))
				{
					tokenActual = lexer.yylex();
					bool();
					if(tokenActual.equals(PARENTC))
					{
						tokenActual = lexer.yylex();
						if(tokenActual.equals(LLAVEA))
						{
							tokenActual = lexer.yylex();
							casos();
							if(tokenActual.equals(LLAVEC))
							{
								tokenActual = lexer.yylex();
							}
							else
							{
								error("Error Sintáctico: Se esperaba una }");
							}
						}
						else
						{
							error("Error Sintáctico: Se esperaba una {");
						}
					}
					else
					{
						error("Error Sintáctico: Se esperaba )");
					}
				}
				else
				{
					error("Error Sintáctico: Se esperaba un (");
				}
			break;
			case LLAVEA:
				bloque();
			break;
			case RETURN:
				tokenActual = lexer.yylex();
				if(tokenActual.equals(PUNTOYC)){
					tokenActual = lexer.yylex();
				}
				else{
					exp();
					if(tokenActual.equals(PUNTOYC)){
						tokenActual = lexer.yylex();
					}
					else
						error("Error Sintáctico: Se esperaba un ;");
				}
				break;

			case ID:
				parte_izquierda();
				if(tokenActual.equals(ASIG)){
					tokenActual = lexer.yylex();
					bool();
					if(tokenActual.equals(PUNTOYC)){
						tokenActual = lexer.yylex();
					}
					else
						error("Error Sintáctico: Se esperaba un ; ");
				}
				else
					error("Error Sintáctico: Se esperaba un =");
				break;
			default:
				break;

		}
	}

	// Casos → caso casos | predeterminado|epsilon
	public void casos() throws IOException
	{
		switch(tokenActual.clase)
		{
			case CASE:
				caso();
				casos();
				break;
			case DEFAULT:
				predeterminado();
				break;
			default:
				break;
		}
	}

	// caso → case numero: instrucciones
	public void caso() throws IOException
	{
		if(tokenActual.equals(CASE))
		{
			tokenActual = lexer.yylex();
			if(tokenActual.equals(ENTEROS))
			{
				tokenActual = lexer.yylex();
				if(tokenActual.equals(DOSPUNTOS))
				{
					tokenActual = lexer.yylex();
					instrucciones();
				}
				else
				{
					error("Error Sintáctico: Se esperaba un : ");
				}
			}
			else
			{
				error("Error Sintáctico: Se esperaba un Entero");
			}
		}
		else{
			error("Error Sintáctico: Se esperaba un CASE");
		}
	}
	 	// predeterminado → default: instrucciones
	void predeterminado() throws IOException{
		if(tokenActual.equals(DEFAULT)){
			tokenActual = lexer.yylex();
			if(tokenActual.equals(DOSPUNTOS)){
				tokenActual = lexer.yylex();
				instrucciones();
			}
		}
	}
	/*
	parte_izquierda → id localizacion | id  
		Factorización
		parte_izquierda→id parte_izquierda’ 
		parte_izquierda’→localizacion |  ε

	*/
	void parte_izquierda() throws IOException{
		if (tokenActual.equals(ID)){
			tokenActual = lexer.yylex();
			parte_izquierdaP();
		}
		else 
			error("Error Sintáctico: Se esperaba un Identificador");
	}

	void parte_izquierdaP() throws IOException{
		if(tokenActual.equals(CORCHA))
			localizacion();
	}
	/* bool bool → bool ||  comb | comb
		ELIMINACION recursividad izq
		bool →comb bool’
		bool‘→|| comb bool’
	*/

	// bool →comb bool’
	 
	void bool() throws IOException{
		comb();
		boolP();
	}

	//bool‘→|| comb bool’|epsilon
	void boolP() throws IOException{
		if(tokenActual.equals(DISYUNCION)){
			tokenActual = lexer.yylex();
			comb();
			boolP();
		}
	}

	/* comb → comb && igualdad| igualdad
		recursividad
		comb →igualdad comb’
		comb’ →&& igualdad comb’
	*/

	// comb →igualdad comb’
	void comb() throws IOException{
		igualdad();
		combP();
	}

	//comb’ →&& igualdad comb’|epsilon
	void combP() throws IOException{
		if(tokenActual.equals(AND)){
			tokenActual = lexer.yylex();
			igualdad();
			combP();
		}
	}

	/* Igualdad →rel igualdad’
		igualdad’→==rel igualdad’ | !=  rel igualdad’| ε
	*/
	void igualdad() throws IOException{
		rel();
		igualdadP();
	}

	void igualdadP() throws IOException{
		switch(tokenActual.clase)
		{
			case IGUALQUE:
				rel();
				igualdadP();
				break;
			case DIFERENTE:
				rel();
				igualdadP();
				break;
			default:
				break;
		}
		 
	}



	/* rel→exp<exp| exp<=exp| exp>=exp| exp>exp| exp
		Factorización izquierda
		rel→exp rel’
		rel’→<exp|<=exp|>=exp|>exp|epsilon
	*/

	//rel→exp rel’
	void rel() throws IOException{
		exp();
		relP();
	}

	//rel’→<exp|<=exp|>=exp|>exp|epsilon
	void relP() throws IOException{
		if(tokenActual.equals(MENOR) || tokenActual.equals(MENORIGUAL) || tokenActual.equals(MAYORIGUAL) || tokenActual.equals(MAYOR)){
			switch(tokenActual.clase)
			{
				case MENOR:
					tokenActual = lexer.yylex();
					exp();
					break;
				case MENORIGUAL:
					tokenActual = lexer.yylex();
					exp();
					break;
				case MAYORIGUAL:
					tokenActual = lexer.yylex();
					exp();
					break;
				case MAYOR:
					tokenActual = lexer.yylex();
					exp();
					break;
				default:
					error("Error Sintáctico: Se esperaba un < , <=, >= o >");	
					break;
			}
		}
	}

	/* 
	exp→exp+term|exp-term|term
		Recursividad
			exp → term exp’  
			exp’ → +term exp’ | -term exp’ |ε

	*/

	void exp() throws IOException{
		term();
		expP();
	}

	//exp’→+term|-term
	void expP() throws IOException{
		switch(tokenActual.clase)
		{
			case SUMA:
				tokenActual = lexer.yylex();
				term();
				expP();
				break;
			case RESTA:
				tokenActual = lexer.yylex();
				term();
				expP();
				break;
			default:
				break;
		}
	}
	/*
	term→term*unario|term/unario|term%unario|unario
		Recursividad
		term→unario term’
		term’ →*unario term’| /unario term’|%unario term’ |ε

	*/
	//term→unario term’
	public void term()throws IOException{
		unario();
		termP();
	}

	//term’ →*unario term’| /unario term’|%unario term’ |ε
	public void termP()throws IOException{
		if(tokenActual.equals(MUL)){
			tokenActual = lexer.yylex();
			unario();
			termP();
		}else if(tokenActual.equals(DIV)){
			tokenActual = lexer.yylex();
			unario();
			termP();
		}else if(tokenActual.equals(MODULO)){
			tokenActual = lexer.yylex();
			unario();
			termP();
		}
	}

	// unario → !unario|-unario|factor
	public void unario()throws IOException{
		switch(tokenActual.clase){
			case NEGACION:
				tokenActual=lexer.yylex();
				unario();
				break;
			case RESTA:
				tokenActual=lexer.yylex();
				unario();
				break;
			default:
				if(tokenActual.equals(PARENTA) || tokenActual.equals(ID) || tokenActual.equals(ENTEROS) || tokenActual.equals(CADENA) || tokenActual.equals(TRUE) || tokenActual.equals(FALSE) )
					factor();
				else
					error("Error Sintáctico: Simbolo invalido");
				break;
		}
	}

	//factor → (bool)|localizaciobn|numero|cadena|true|false|id(parametros)
	public void factor()throws IOException{
		switch(tokenActual.clase){
			case PARENTA:
				tokenActual=lexer.yylex();
				bool();
				if(tokenActual.equals(PARENTC))
					tokenActual=lexer.yylex();
				else
					error("Error Sintáctico: Se esperaba )");
				break;
			case ENTEROS:
				tokenActual=lexer.yylex();
				break;
			case CADENA:
				tokenActual=lexer.yylex();
				break;
			case TRUE:
				tokenActual=lexer.yylex();
				break;
			case FALSE:
				tokenActual=lexer.yylex();
				break;
			case ID:
				tokenActual=lexer.yylex();
				if(tokenActual.equals(PARENTA)){
					tokenActual=lexer.yylex();
					parametros();
					if(tokenActual.equals(PARENTC))
						tokenActual=lexer.yylex();
					else
						error("Error Sintáctico: Se esperaba un )");
				}else
					localizacion();
				
				break;
			default:
				break;
		}
	}

	//parametros → lista_param|epsilon
	public void parametros()throws IOException{
		switch(tokenActual.clase){
			case NEGACION:
				lista_param();
				break;
			case RESTA:
				lista_param();
				break;
			default:
				break;
		}
	}

	//lista_param → bool lista_param'
	public void lista_param()throws IOException{
		bool();
		lista_paramP();
	}

	//lista_param’ → ,bool lista_param’|epsilon
	public void lista_paramP()throws IOException{
		if(tokenActual.equals(COMA)){
			tokenActual=lexer.yylex();
			bool();
			lista_paramP();
		}
	}
	
	//localizacion→id localizacion’

	public void localizacion()throws IOException{
		if(tokenActual.equals(ID)){
			tokenActual=lexer.yylex();
			localizacionP();
		}
	}

	//localizacion’→ (bool) localizacion’|epsilon
	void localizacionP()throws IOException{
		if(tokenActual.equals(PARENTA)){
			tokenActual=lexer.yylex();
			bool();
			if(tokenActual.equals(PARENTC)){
				tokenActual=lexer.yylex();
				localizacionP();
			}else{
				error("Error Sintáctico: Se esperaba )");
			}
		}
	}
}
