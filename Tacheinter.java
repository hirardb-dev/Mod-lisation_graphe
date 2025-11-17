import java.util.ArrayList;
import java.awt.Color;

public class Tacheinter extends Tache{
    Tacheinter(String n,int d, int x, int y, Color c){setnom(n);setduree(d);setpos(x, y); this.Listearc = new ArrayList<>();setColor(Color.GRAY);}

    protected String getnom(){return this.nom;}
    protected int getduree(){return this.duree;}
    protected int getx(){return this.x;}
    protected int gety(){return this.y;}

    protected void setnom(String n){this.nom=n;}
    protected void setduree(int d){this.duree=d;}
    protected void setx(int x){this.x=x;}
    protected void sety(int y){this.y=y;}
    protected void setpos(int x, int y){
        this.x=x;
        this.y=y;
    }
    protected void setColor(Color c){this.col=c;}
}
