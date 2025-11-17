import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.*;

public class Fenetre extends JFrame implements MouseListener, MouseMotionListener{

    private boolean modeAjoutTache = false;
    private boolean modeModifier = false;
    private boolean modeSuppression = false;
    private boolean modeAjoutArc = false;
    private JSlider js;
    private int select=-1;
    private ArrayList<Tache> t = new ArrayList<>();
    private Display d;
    private ArrayList<Arc> arcs = new ArrayList<>();
    private Tache arcdebut = null;


    public Fenetre(int UI, Graphe gr){
        super("Graphe");
        t=gr.getListetache();
        arcs=gr.getListearc();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.init();
        this.d = new Display();
        this.d.addMouseListener(this);
        this.d.addMouseMotionListener(this);
        
        this.d.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK),"Init");
        this.d.getActionMap().put("Init",new InitAction());

        this.getContentPane().add(d, BorderLayout.CENTER);
        this.js = new JSlider(2,50);
        js.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){repaint();}
        });
        JToolBar jtb = new JToolBar();
        jtb.add(js);
        this.getContentPane().add(jtb, BorderLayout.PAGE_START);
        //this.pack();

        // Création d'un panneau vertical pour les boutons à droite
        JPanel paneaudroit = new JPanel();
        paneaudroit.setLayout(new GridLayout(3, 1, 5, 10));

        // Création des boutons
        JButton AjoutTache = new JButton("Ajout Tâche");
        AjoutTache.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeAjoutTache = true;
            }
        });        
        JButton Suppression = new JButton("Suppression");
        Suppression.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeSuppression = true;
            }
        });        
        JButton AjoutArc = new JButton("Ajout Arc");
        AjoutArc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeAjoutArc = true;
                arcdebut = null; // réinitialise la sélection
            }
        });        
        JButton Modifier = new JButton("Modifier");
        Modifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modeModifier = true;
            }
        });
        JButton Enregistrer = new JButton("Enregistrer");
        Enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enregistrerGraphe();
            }
        });
        JButton Charger = new JButton("Charger");
        Charger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerGraphe();
                repaint();
            }
        });

        // Ajout des boutons au panneau
        paneaudroit.add(AjoutTache);
        paneaudroit.add(Suppression);
        paneaudroit.add(AjoutArc);
        paneaudroit.add(Modifier);
        paneaudroit.add(Enregistrer);
        paneaudroit.add(Charger);


        // Ajout du panneau à la fenêtre, sur le côté gauche
        this.getContentPane().add(paneaudroit, BorderLayout.EAST);

        //Ouverture en plein écran
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
    }

    private void init(){
    t.add(new Tachedebut());
    t.add(new Tachefin());
    arcs.clear();
	}
	
    private void pressed(MouseEvent e){
        if (modeAjoutTache) {
            // Créer une nouvelle tâche à l'endroit cliqué
            t.add(new Tacheinter("T" + t.size(), 1, e.getX(), e.getY(), Color.BLACK));

            modeAjoutTache = false; // sortir du mode ajout
            repaint();
            //return;
        }

        this.select = -1;
        for (int i = 0; i < t.size(); i++) {
            int dx = e.getX() - t.get(i).getx();
            int dy = e.getY() - t.get(i).gety();
            if (Math.sqrt(dx * dx + dy * dy) <= js.getValue()) {
                this.select = i;
                break;
            }
        }
    
        if (modeAjoutArc) {
            for (int i = 0; i < t.size(); i++) {
                int dx = e.getX() - t.get(i).getx();
                int dy = e.getY() - t.get(i).gety();
                if (Math.sqrt(dx * dx + dy * dy) <= js.getValue()) {
                    if (arcdebut == null) {
                        arcdebut = t.get(i); // première tâche sélectionnée
                    } else {
                        Tache arcEnd = t.get(i); // deuxième tâche
                        arcs.add(new Arc(arcdebut, arcEnd));
                        arcdebut = null;
                        modeAjoutArc = false;
                        repaint();
                    }
                    break;
                }
            }
            //return;
        }
        
        if (modeSuppression) {
            // Suppression d'une tâche si cliquée
            for (int i = 0; i < t.size(); i++) {
                int dx = e.getX() - t.get(i).getx();
                int dy = e.getY() - t.get(i).gety();
                if (Math.sqrt(dx * dx + dy * dy) <= js.getValue()) {
                    Tache toRemove = t.get(i);
                    t.remove(i);
        
                    // Supprimer tous les arcs liés à cette tâche (père ou fils)
                    arcs.removeIf(arc -> arc.getPere() == toRemove || arc.getFils() == toRemove);
        
                    modeSuppression = false;
                    repaint();
                    //return;
                }
            }
        
            // Sinon, vérifier si clic proche d'un arc
            for (int i = 0; i < arcs.size(); i++) {
                Arc arc = arcs.get(i);
                int x1 = arc.getPere().getx();
                int y1 = arc.getPere().gety();
                int x2 = arc.getFils().getx();
                int y2 = arc.getFils().gety();
        
                double distance = distancearc(e.getX(), e.getY(), x1, y1, x2, y2);
                if (distance < 10) { // seuil de proximité
                    arcs.remove(i);
                    modeSuppression = false;
                    repaint();
                    //return;
                }
            }
        
            // Si rien supprimé
            modeSuppression = false;
        }        

        // Si une tâche est sélectionnée en mode modification
        if (modeModifier && select != -1) {
            Tache selected = t.get(select);
            String nouveauNom = JOptionPane.showInputDialog(this, "Nom de la tâche :", selected.getnom());
            if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
                selected.setnom(nouveauNom.trim());
            }
    
            String dureeStr = JOptionPane.showInputDialog(this, "Durée de la tâche :", selected.getduree());
            try {
                int nouvelleDuree = Integer.parseInt(dureeStr);
                if (nouvelleDuree >= 0) {
                    selected.setduree(nouvelleDuree);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Durée invalide. Modification ignorée.");
            }
    
            modeModifier = false; // quitter le mode
            repaint();
        }

        if(e.getButton()==MouseEvent.BUTTON1){
            for (int i = 0; i < t.size(); i++) {
            int dx = e.getX() - t.get(i).x;
            int dy = e.getY() - t.get(i).y;
            if (Math.sqrt(dx*dx + dy*dy) <= js.getValue()) {
                this.select = i;
            }
}
        }
    }
 
    private void released(){
        this.select = -1;
    }

    private void dragged(MouseEvent e){
        if (this.select != -1) {
            Tache selected = t.get(this.select);
            selected.setx(e.getX());
            selected.sety(e.getY());
            this.repaint();
        }
        
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {pressed(e);}
    public void mouseReleased(MouseEvent e) {released();}
    public void mouseDragged(MouseEvent e) {dragged(e);}

    class InitAction extends AbstractAction{
        public InitAction(){
            super("InitAction");
        }

        public void actionPerformed(ActionEvent e){
            init();
            repaint();
        }

    }


    class Display extends JPanel{
        public Display(){
            super();
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            // prise en compte de la valeur de la barre d'outil
            int d = js.getValue();
            for (int i = 0; i < t.size(); i++) {
                g.setColor(t.get(i).col);
                g.fillOval(t.get(i).getx() - d / 2, t.get(i).gety() - d / 2, d, d);
            
                g.setColor(Color.BLACK);
                g.drawString(t.get(i).getnom(), t.get(i).getx() - d / 2, t.get(i).gety() - d / 2 - 5);
            }
            
            for (Arc arc : arcs) {
                Tache pere = arc.getPere();
                Tache fils = arc.getFils();
            
                int x1 = pere.getx();
                int y1 = pere.gety();
                int x2 = fils.getx();
                int y2 = fils.gety();
            
                dessinarc(g, x1, y1, x2, y2);
            
                // Calcul de la position du texte (milieu de l'arc)
                int textX = (x1 + x2) / 2;
                int textY = (y1 + y2) / 2 - 5;//-5 pour surélever le texte
            
                g.setColor(Color.BLUE); // Couleur du texte
                g.drawString(String.valueOf(pere.getduree()), textX, textY);
            }
            
        }
    }
    private void dessinarc(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(x1, y1, x2, y2);
    
        // Dessiner une petite flèche au bout
        double phi = Math.toRadians(20);
        int barb = 10;
    
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        double x, y;
    
        for(int j = 0; j < 2; j++) {
            double rho = theta + (j == 0 ? phi : -phi);
            x = x2 - barb * Math.cos(rho);
            y = y2 - barb * Math.sin(rho);
            g.drawLine(x2, y2, (int)x, (int)y);
        }
    
        g.dispose();
    }
    private double distancearc(int px, int py, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0) {
            dx = px - x1;
            dy = py - y1;
            return Math.sqrt(dx * dx + dy * dy);
        }
    
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
    
        dx = projX - px;
        dy = projY - py;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    private void enregistrerGraphe() {
        StringBuilder sb = new StringBuilder();
    
        for (Tache task : t) {
            sb.append("TACHE;");
            sb.append(task.getClass().getSimpleName()).append(";");
            sb.append(task.getnom()).append(";");
            sb.append(task.getduree()).append(";");
            sb.append(task.getx()).append(";");
            sb.append(task.gety()).append("\n");
        }
    
        for (Arc arc : arcs) {
            int i1 = t.indexOf(arc.getPere());
            int i2 = t.indexOf(arc.getFils());
            sb.append("ARC;").append(i1).append(";").append(i2).append("\n");
        }
    
        JTextArea area = new JTextArea(sb.toString());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
    
        JOptionPane.showMessageDialog(this, scroll, "Données du graphe", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void chargerGraphe() {
        JTextArea inputArea = new JTextArea(20, 40);
        JScrollPane scroll = new JScrollPane(inputArea);
        int option = JOptionPane.showConfirmDialog(this, scroll, "Données du graphe", JOptionPane.OK_CANCEL_OPTION);
    
        if (option != JOptionPane.OK_OPTION) return;
    
        String[] lines = inputArea.getText().split("\n");
        t.clear();
        arcs.clear();
    
        ArrayList<String> arcLines = new ArrayList<>();
    
        for (String line : lines) {
            if (line.startsWith("TACHE")) {
                String[] parts = line.split(";");
                String type = parts[1];
                String nom = parts[2];
                int duree = Integer.parseInt(parts[3]);
                int x = Integer.parseInt(parts[4]);
                int y = Integer.parseInt(parts[5]);
    
                if (type.equals("Tachedebut")) {
                    Tachedebut td = new Tachedebut();
                    td.setx(x); td.sety(y);
                    t.add(td);
                } else if (type.equals("Tachefin")) {
                    Tachefin tf = new Tachefin();
                    tf.setx(x); tf.sety(y);
                    t.add(tf);
                } else {
                    Tacheinter ti = new Tacheinter(nom, duree, x, y, Color.GRAY);
                    t.add(ti);
                }
            } else if (line.startsWith("ARC")) {
                arcLines.add(line);
            }
        }
    
        for (String arcLine : arcLines) {
            String[] parts = arcLine.split(";");
            int i1 = Integer.parseInt(parts[1]);
            int i2 = Integer.parseInt(parts[2]);
            arcs.add(new Arc(t.get(i1), t.get(i2)));
        }
    }    
}