import java.awt.Color;
import java.util.ArrayList;

public class Tachefin extends Tache{
    Tachefin(){setnom("Fin");setduree(0);setpos(1300, 450);this.Listearc = new ArrayList<>(); setColor(Color.RED);}

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