import java.util.*;
public class TabladeTipos{
	ArrayList <Tipo> tabla;
	public TabladeTipos(){
		tabla=new ArrayList<Tipo>();
	}
	public boolean setSimbol(Tipo tipo){
		if(!buscar(tipo.id)){
			tabla.add(tipo);
			return true;
		}
		return false;
	}
	public boolean buscar(int id){
		for(Tipo tipo : tabla){
			if(tipo.id == id)
				return true;
		}
		return false;
	}

	public int index(int id){
		int i=0;
		for(Tipo tipo: tabla){
			if(tipo.id == id)
				return i;
			i++;
		}
		return -1;
	}

	public String getTipo(int id){
		if(buscar(id)){
			Tipo tipo= tabla.get(index(id));
			return tipo.name;
		}
		return null;
	}
	public int getTam(int id){
		if(buscar(id)){
			Tipo tipo= tabla.get(index(id));
			return tipo.tam;
		}
		return -1;
	}

	public void impTabla(){
		System.out.println("id"+"\t"+"Tipo"+"\t"+"Tam"+"\t"+"#e"+"\t"+"TipoBase");
		for(Tipo s: tabla){
			System.out.println(s.id+"\t"+s.name+"\t"+s.tam+"\t"+s.numElem+"\t"+s.tipoBase);
		}
		System.out.println("\n");
	}
}