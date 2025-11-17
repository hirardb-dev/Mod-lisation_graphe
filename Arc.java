public class Arc{
    private Tache pere;
    private Tache fils;
    
    Arc(Tache p, Tache f){this.pere=p; this.fils=f;}

    public Tache getFils() {
        return fils;
    }
    public Tache getPere() {
        return pere;
    }
    public void setFils(Tache fils) {
        this.fils = fils;
    }
    public void setPere(Tache pere) {
        this.pere = pere;
    }
}