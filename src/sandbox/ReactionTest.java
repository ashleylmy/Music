package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;
import reaction.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ReactionTest extends Window {
    static {
        new Layer("Back");
        new Layer("Fore");
    }

    public ReactionTest() {
        super("Reaction Test", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
        Reaction.initialReactions.addReaction(new Reaction("SW-SW") {
            @Override
            public int bid(Gesture g) {
                return 0;
            }

            @Override
            public void act(Gesture g) {
                new Box(g.vs);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        g.setColor(Color.BLUE);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        Gesture.AREA.dn(me.getX(), me.getY());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        Gesture.AREA.drag(me.getX(), me.getY());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        Gesture.AREA.up(me.getX(), me.getY());
        repaint();
    }

    public static class Box extends Mass {
        public G.VS vs;
        public Color c = G.rndColor();

        public Box(G.VS vs) {
            super("Back");
            this.vs = vs;
        }

        public void show(Graphics g) {
            vs.fill(g, c);
        }

    }
}