package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;
import reaction.Gesture;
import reaction.Ink;
import reaction.Layer;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Music extends Window {
    public Layer BACK = new Layer("BACK"), FORE = new Layer("FORE");

    public Music() {
        super("Music", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);
    }

    public void mousePressed(MouseEvent me) { Gesture.AREA.dn(me.getX(), me.getY()); repaint(); }
    public void mouseDragged(MouseEvent me) { Gesture.AREA.drag(me.getX(), me.getY()); repaint(); }
    public void mouseReleased(MouseEvent me) { Gesture.AREA.up(me.getX(), me.getY()); repaint(); }
}
