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
    public static String recognized ="";
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
        g.drawString(recognized, 700,40);
    }

    @Override
    public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseReleased(MouseEvent me) {
        Ink ink=new Ink();
        Shape s = Shape.recognize(ink);
        recognized="recognized: "+ ((s!=null)?s.name:"unrecognized");
        Shape.Prototype proto;
        inkList.add(ink);
        if(pList.bestDist(ink.norm)<UC.NO_MATCH_DIST){
            proto=pList.bestMatch;
            proto.blend(ink.norm);
        }else{
            proto=new Shape.Prototype(ink.norm);
            pList.add(proto);

        }
        ink.norm=proto;
        repaint(); }
}
