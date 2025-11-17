import java.util.ArrayList;

public class Graphe {
    private ArrayList<Tache> Listetache;
    private ArrayList<Arc> Listearc;

    Graphe(){
        this.Listetache=new ArrayList<Tache>();
        this.Listearc=new ArrayList<Arc>();
    }

    public ArrayList<Arc> getListearc() {
        return Listearc;
    }

    public ArrayList<Tache> getListetache() {
        return Listetache;
    }

    public static void main(String[] args) {
        Graphe graphe= new Graphe();
        new Fenetre(1,graphe);
    }
}