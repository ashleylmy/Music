package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.Ink;
import music.UC;


import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static Ink.List inkList= new Ink.List();
//    static { inkList.add(new Ink());}


    public PaintInk() {
        super("Paint Ink", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        inkList.show(g);
        Ink.BUFFER.show(g);
        g.drawString("Ink Size: "+inkList.size(),500,500);
    }

    @Override
    public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint(); }
    @Override
    public void mouseReleased(MouseEvent me) { inkList.add(new Ink()); repaint(); }
}
