package sandbox;

import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Paint extends Window {
    public static int clicks=0;
    public static Path thePath=new Path();
    public static Path.Pic thePic=new Path.Pic();
    public Paint(){
        super("Paint", 1000, 800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        g.setColor(G.rndColor());
        g.fillRect(100, 100, 200, 300);//x axis->, y axis down
        g.setColor(Color.BLACK);
        g.drawLine(100,500, 500, 100);
        int x=400, y=200; //local var, live in this function
        String str="Dude "+clicks;
        g.drawString(str, x, y);
        g.drawOval(x,y,2,2);//coordinates of string on the window: left-bottom of the string
        FontMetrics fm=g.getFontMetrics();
        int a=fm.getAscent(), d=fm.getDescent();// ascent: how high about the baseline the characters reach
                                                // descent: how far below the baseline(e.g. letter 'g', 'y')
        int w=fm.stringWidth(str);
        g.drawRect(x, y-a, w, a+d);
        g.setColor(Color.BLACK);
        thePic.draw(g);
        g.setColor(Color.red);
        thePath.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        clicks++;
        thePath=new Path();
        thePath.add(me.getPoint());
        thePic.add(thePath);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        thePath.add(me.getPoint());
        repaint();
    }

    //-------------------------------Path---------------------------
    //nest class
    //calling outside from Paint, must be Paint.Path
    public static class Path extends ArrayList<Point>{
        public void draw(Graphics g){
            for(int i=1; i<size(); i++){
                Point p=get(i-1), n=get(i);
                g.drawLine(p.x, p.y, n.x, n.y); //draw from previous point to next point
            }
        }
        //-------------------------------List---------------------------
        public static class Pic extends ArrayList<Path>{
            public void draw(Graphics g){
                for (Path p:this){
                    p.draw(g);
                }
            }
        }
    }
}
