import java.util.*;
public class Simbolo{
	String id;
	int dir;
	int tipo;
	String var;
	ArrayList <Integer> params=new ArrayList<Integer>();

	public Simbolo(String id, int dir, int tipo,ArrayList <Integer> args,String var){
		this.id = id;
		this.tipo = tipo;
		this.dir = dir;
		this.params=args;
		this.var = var;
	}

	public Simbolo(String id, int dir, int tipo,String var){
		this.id = id;
		this.tipo = tipo;
		this.dir = dir;
		this.var = var;
	}
	public String impParam(){
		String array = "{ ";
		for(Integer i : params){
			//System.out.println(i);
			array=array+(Integer.toString(i)+" ");
		}
		array+="}";
		return array;
	}

}