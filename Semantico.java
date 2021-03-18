import java.io.IOException;
import java.lang.*;
import java.util.*;
public class Semantico{
	Stack <TabladeTipos> ptt;
	
	public Semantico(){
	}	
	public TabladeSimbolos fondoTS(Stack <TabladeSimbolos> pts){
		Stack <TabladeSimbolos> pts2=new Stack<TabladeSimbolos>();
		while(!pts.empty()){
			pts2.push(pts.pop());
		}
		return pts2.peek();
	}
	//public Stack<TabladeTipos> fondoTT(Stack <TabladeTipos>){

	//}
}