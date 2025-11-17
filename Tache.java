import java.util.ArrayList;
import java.awt.Color;

abstract public class Tache {
    protected String nom;
    protected int duree;
    protected int x,y;
    protected Color col;
    protected ArrayList<Arc> Listearc;

    abstract String getnom();
    abstract int getduree();
    abstract int getx();
    abstract int gety();

    abstract void setColor(Color c);
    abstract void setnom(String n);
    abstract void setduree(int d);
    abstract void setx(int x);
    abstract void sety(int y);
    abstract void setpos(int x, int y);
}