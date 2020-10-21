package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import reaction.Ink;
import music.UC;
import reaction.Shape;


import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList= new Ink.List();
    public static Shape.Prototype.List pList= new Shape.Prototype.List();
//    static { inkList.add(new Ink());}


    public PaintInk() {
        super("Paint Ink", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        inkList.show(g);
        Ink.BUFFER.show(g);
        if(inkList.size()>1){
            int last=inkList.size()-1;
            int dist=inkList.get(last).norm.dist(inkList.get(last-1).norm);
            g.setColor(dist>UC.NO_MATCH_DIST?Color.RED: Color.BLACK);
            g.drawString("Dist: "+ dist, 600,60);
        }
        pList.show(g);

    }

    @Override
    public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseReleased(MouseEvent me) {
        Ink ink=new Ink();
        Shape.Prototype proto;
        inkList.add(ink);
        if(pList.bestDist(ink.norm)<UC.NO_MATCH_DIST){
            proto=pList.bestMatch;
            pList.bestMatch.blend(ink.norm);
        }else{
            proto=new Shape.Prototype(ink.norm);
            pList.add(proto);

        }
        ink.norm=proto;
        repaint(); }
}
