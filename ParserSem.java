import java.io.IOException;
import java.lang.*;
import java.util.*;
public class ParserSem{
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
	int id = 0;
	int idS = 0;
	ArrayList<Integer> listaDir = new ArrayList<Integer>(); //contador de direcciones
	//lista de retorno
	TabladeSimbolos TSFondo;
	TabladeTipos TTFondo;
	Stack <TabladeSimbolos> pilaTS; 
	public Stack <TabladeTipos> pilaTT; 
	Semantico sem=new Semantico();
	ArrayList<Integer> listaRetorno;
	public ParserSem(Yylex lexer)throws IOException{
		this.lexer = lexer;
		TSFondo = new TabladeSimbolos();
		TTFondo = new TabladeTipos();
		TTFondo.setSimbol(new Tipo(id,"int",4,0,-1));
		id++;
		TTFondo.setSimbol(new Tipo(id,"float",4,0,-1));
		id++;
		TTFondo.setSimbol(new Tipo(id,"char",1,0,-1));
		id++;
		TTFondo.setSimbol(new Tipo(id,"double",8,0,-1));
		id++;
		pilaTS = new Stack<TabladeSimbolos>();
		pilaTT = new Stack<TabladeTipos>();
		//pilaTT.peek().impTabla();
	}

	public void init()throws IOException{
		this.tokenActual = lexer.yylex();
		programa();
		if (this.acp == 0 && tokenActual.equals(0))
			System.out.println("Cadena aceptada");
		else
			System.out.println("Error en la linea: "+tokenActual.linea+" Error sintactico "+tokenActual.valor);
	}

	void error(String msj)throws IOException{
		System.out.println("Error en la linea: "+tokenActual.linea+" "+msj+"\n Error generado por:"+tokenActual.valor);
		//System.out.println(msj);
		//System.out.println();

		this.acp = -1;
	}
	
	void programa()throws IOException{
		pilaTS.push(TSFondo);
		pilaTT.push(TTFondo);
		listaDir.add(0);
		
		declaraciones();
		funciones();
		TTFondo.impTabla();
		TSFondo.impTabla();
	}
	//declaraciones → tipo lista_var; declaraciones | epsilon
	void declaraciones()throws IOException{
		if(tokenActual.equals(INT)|| tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)|| tokenActual.equals(DOUBLE)|| tokenActual.equals(VOID)){
			int listVarTipo = tipo();
			lista_var(listVarTipo);
			if(tokenActual.equals(PUNTOYC)){
				tokenActual = lexer.yylex();
				declaraciones();
			}else{
				error("Error Sintáctico: Se esperaba ;");
			}
		}
	}

	//basico → int|float|char|double|void
	int basico()throws IOException{
 		if(tokenActual.equals(INT)|| tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)|| tokenActual.equals(DOUBLE)|| tokenActual.equals(VOID)){		
			switch(tokenActual.clase){
				case INT:
					tokenActual = lexer.yylex();
					return 0;
				case FLOAT:
					tokenActual = lexer.yylex();
					return 1;
				case CHAR:
					tokenActual = lexer.yylex();
					return 2;
				case DOUBLE:
					tokenActual = lexer.yylex();
					return 3;
				case VOID:
					tokenActual = lexer.yylex();
					return 4;
				default:
					error("Error Semántico: No existe ese tipo de dato");
					return -1;
			}
 		}
 		else
 		{
 			error("Error Sintáctico: No se reconoce el tipo de dato");
 		}
 		return -1;
 	}
 	//tipo → basico compuesto
	int tipo()throws IOException{
		int compuestoBase = basico();
		return compuesto(compuestoBase);
	}
	//compuesto → (numero) compuesto | epsilon
	int compuesto(int compuestoBase)throws IOException{
 		if(tokenActual.equals(CORCHA)){
 			tokenActual = lexer.yylex();
 			if(tokenActual.equals(ENTEROS)){
 				int aux=Integer.parseInt(tokenActual.valor);
 				tokenActual = lexer.yylex();
 				if(tokenActual.equals(CORCHC)){
 					tokenActual = lexer.yylex();
 					int tam=pilaTT.peek().getTam(compuestoBase);
 					pilaTT.peek().setSimbol(new Tipo (id,"array",tam*aux,aux,compuestoBase));
 					//dir = dir + pilaTT.peek().getTam(id);
 					id++;
 					return compuesto(id-1);
 				}
 				else
 					error("Error Sintáctico: Se esperaba un ]");
 			}
 			else
 				error("Error Sintáctico: Se esperaba un entero ");
 		}
 		return compuestoBase;
 	}
 	/*lista_var → lista_var,id|id
		Recursividad izquierda:
		lista_var→ id lista_var’
		lista_var’→, id lista_var’|epsilon
 	*/
 	 void lista_var(int listaVar)throws IOException{
 		if(tokenActual.equals(ID)){
 			int listaVarP = listaVar;
 			if (!pilaTS.peek().buscar(tokenActual.valor)){
 				pilaTS.peek().setSimbol(new Simbolo(tokenActual.valor,listaDir.get(0),listaVarP,null,"var"));
 				//dir = dir + pilaTT.peek().getTam(listaVarP);
 				listaDir.set(0,listaDir.get(0)+ pilaTT.peek().getTam(listaVarP));
 				tokenActual = lexer.yylex();
 			}
 			else {
 				error("Error Semántico: el id ya está declarado");
 			}
  			lista_varP(listaVarP);
 		}
 		else
 			error("Error Sintáctico:Se esperaba un identificador");
 	}
//lista_var'→  , id lista_var'|epsilon
 	void lista_varP(int listaVarP)throws IOException{
 		if(tokenActual.equals(COMA)){
 			tokenActual = lexer.yylex();
 			if(tokenActual.equals(ID)){
 				if (!pilaTS.peek().buscar(tokenActual.valor)){
 					pilaTS.peek().setSimbol(new Simbolo(tokenActual.valor,listaDir.get(0),listaVarP,null,"var"));
 					listaDir.set(0,listaDir.get(0)+ pilaTT.peek().getTam(listaVarP));
 					tokenActual = lexer.yylex();
 				}else{
 					error("Error Semántico: el id ya está declarado");
 				}
 				lista_varP(listaVarP);
 			}
 			else
 				error("Error Sintáctico: Se esperaba un identificador");
 		}
 		
 	}
 	// funciones → func tipo id (argumentos) bloque funciones | epsilon
 	void funciones()throws IOException{
 		listaRetorno= new ArrayList<Integer>();
 		listaDir.add(0);
 		pilaTS.push(new TabladeSimbolos());
		pilaTT.push(new TabladeTipos());
 		if(tokenActual.equals(FUNC)){
 			tokenActual = lexer.yylex();
 			int functipo=tipo();
 			if(tokenActual.equals(ID)){
 				String auxid=tokenActual.valor;
 				tokenActual = lexer.yylex();
 				if(!TSFondo.buscar(auxid)){
	 				if(tokenActual.equals(PARENTA)){
	 					tokenActual = lexer.yylex();
	 					ArrayList<Integer> argumentosLista=new ArrayList<Integer>();
	 					argumentosLista=argumentos();
	 					//gencode
	 					//nueva etiq
	 					if(tokenActual.equals(PARENTC)){
							tokenActual = lexer.yylex();
	 						bloque();
	 						TSFondo.setSimbol(new Simbolo(auxid,listaDir.get(0),functipo,argumentosLista,"func"));
	 						//gencode()
	 						System.out.println("TablaTT func "+auxid);
	 						pilaTT.peek().impTabla();
	 						System.out.println("TablaTS func "+auxid);
	 						pilaTS.peek().impTabla();
	 						funciones();
	 					}
	 					else
	 						error("Error Sintáctico: Se esperaba un )");
	 				}
	 				else
	 					error("Error Sintáctico: Se esperaba un (");
	 			}
	 			else
	 				error("Error semantico :El id ya esta declarado");
 			}
 			else 
 				error("Error Sintáctico: Se esperaba un identificador");
 		}
			
 	}
 	// argumentos → lista_args|epsilon
	public ArrayList<Integer>  argumentos() throws IOException
	{
		if(tokenActual.equals(INT)||tokenActual.equals(FLOAT)||tokenActual.equals(CHAR)||tokenActual.equals(DOUBLE)||tokenActual.equals(VOID))
			return lista_args();
		return null;
	}

	/*
		lista_args → lista_args, tipo id| tipo id
		ELIMINACION de recursividad
		lista_args → tipo id lista_args’
		lista_args’ → , tipo id lista_args’| epsilon
	*/
	// lista_args → tipo id lista_args’
	public ArrayList<Integer>  lista_args() throws IOException
	{
		ArrayList<Integer> listaArgsP=new ArrayList<Integer>();
		int auxtipo=tipo();
		if(tokenActual.equals(ID))
		{
			tokenActual = lexer.yylex();
			listaArgsP.add(auxtipo);
			return lista_argsP(listaArgsP);
		}
		else
		{
			error("Error Sintáctico: se esperaba identificador");
		}
		return listaArgsP;
	}
	// lista_args’ → , tipo id lista_args’| epsilon
	public ArrayList<Integer> lista_argsP(ArrayList<Integer> listaArgsP) throws IOException
	{
		if(tokenActual.equals(COMA))
		{
			tokenActual = lexer.yylex();
			int auxtipo=tipo();
			if(tokenActual.equals(ID))
			{
				tokenActual = lexer.yylex();
				listaArgsP.add(auxtipo);
				return lista_argsP(listaArgsP);
			}
			else
			{
				error("Error Sintáctico: Se esperaba identificador");
			}
		}
		return listaArgsP;
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
			localizacion("");
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
		ArrayList<String> auxarray=new ArrayList<String>();
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
				if(tokenActual.equals(PARENTA) || tokenActual.equals(ID) || tokenActual.equals(ENTEROS) || tokenActual.equals(CADENA) || tokenActual.equals(TRUE) || tokenActual.equals(FALSE) ){
					auxarray=factor();
					//String auxtipo=auxarray.get(0);
					//factor();
					//int unarioTipo=Integer.parseInt(auxarray.get(0));
				}
				else{
					error("Error Sintáctico: Simbolo invalido");
				}
				break;
		}
	}

	//factor → (bool)|localizaciobn|numero|cadena|true|false|id(parametros)
	public ArrayList<String> factor()throws IOException{
		ArrayList<String> factipodir=new ArrayList<String>();
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
				factipodir.add(0,"0");
				factipodir.add(1,tokenActual.valor);
				tokenActual=lexer.yylex();
				break;
			case CADENA:
				factipodir.add(0,tokenActual.valor);
				factipodir.add(1,"dir");
				tokenActual=lexer.yylex();
				break;
			case TRUE:
				factipodir.add(0,"0");
				factipodir.add(1,"true");
				tokenActual=lexer.yylex();
				break;
			case FALSE:
				factipodir.add(0,"0");
				factipodir.add(1,"false");
				tokenActual=lexer.yylex();
				break;
			case ID:
				String factorPBase=tokenActual.valor;
				tokenActual=lexer.yylex();
				factipodir=factorP(factorPBase);
			default:
				break;
		}
		return factipodir;
	}

	public ArrayList<String>  factorP(String factorPBase) throws IOException{
		ArrayList<String> facPtipodir=new ArrayList<String>();
			if(tokenActual.equals(PARENTA)){ 
				if(TSFondo.buscar(factorPBase)){
					if(TSFondo.getVar(factorPBase)=="func"){
						//if(TSFondo.getArgs(factorPBase)){
							tokenActual=lexer.yylex();
							parametros();
							if(tokenActual.equals(PARENTC)){
								tokenActual=lexer.yylex();
							}
						//}else{

						//}
					}else{
						error("Error Semantico: id no es funcion");
					}
				}else{
					error("Error Semantico: El id no esta declarado");
				}
			}else if(tokenActual.equals(CORCHA)){
				facPtipodir.add(0,localizacion(factorPBase));
				facPtipodir.add(1,"nueva etiqueta");
			}
			return facPtipodir;
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

	public String localizacion(String factorPBase)throws IOException{
		if(tokenActual.equals(CORCHA)){
			tokenActual=lexer.yylex();
			bool();
			if(tokenActual.equals(CORCHC)){
				tokenActual=lexer.yylex();
			}
			localizacionP();
		}
		return "";
	}

	//localizacion’→ (bool) localizacion’|epsilon
	void localizacionP()throws IOException{
		if(tokenActual.equals(CORCHA)){
			tokenActual=lexer.yylex();
			bool();
			if(tokenActual.equals(CORCHC)){
				tokenActual=lexer.yylex();
				localizacionP();
			}else{
				error("Error Sintáctico: Se esperaba )");
			}
		}
	}
}
