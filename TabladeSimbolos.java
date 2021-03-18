import java.util.*;
public class TabladeSimbolos{
	ArrayList <Simbolo> tabla;

	public TabladeSimbolos(){
		tabla = new ArrayList <Simbolo>();
	}
	public boolean setSimbol(Simbolo s){
		if(!buscar(s.id)){
			tabla.add(s);
			return true;
		}
		return false;
	}
	public boolean buscar(String id){
		for(Simbolo sim : tabla){
			if(sim.id.equals(id))
				return true;
		}
		return false;
	}

	public int index(String id){
		int i=0;
		for(Simbolo sim : tabla){
			if(sim.id.equals(id))
				return i;
			i++;
		}
		return -1;
	}

	public int getTipo(String id){
		if(buscar(id)){
			Simbolo s = tabla.get(index(id));
			return s.tipo;
		}
		return -1;
	}
	public String getVar(String id){
		if(buscar(id)){
			Simbolo s = tabla.get(index(id));
			return s.var;
		}
		return null;
	}
	public ArrayList<Integer> getArgs(String id){
		if(buscar(id)){
			Simbolo s = tabla.get(index(id));
			return s.params;
		}
		return null;
	}
	public void impTabla(){
		System.out.println("id"+"\t"+"dir"+"\t"+"tipo"+"\t"+"Var"+"\t"+"Params");
		for(Simbolo s: tabla){
			if(s.params==null)	
				System.out.println(s.id+"\t"+s.dir+"\t"+s.tipo+"\t"+s.var+"\t"+"---");
			else{
				System.out.println(s.id+"\t"+s.dir+"\t"+s.tipo+"\t"+s.var+"\t"+s.impParam());
			}
		}
		System.out.println("\n");
	}

}