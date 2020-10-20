package reaction;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLOutput;

public class ShapeTrainer extends Window {
    public static String UNKNOWN ="<-unknown";
    public static String ILLEGAL="<-illegal";
    public static String KNOWN = "<-known";
    public static String curName ="";
    public static String curState= ILLEGAL;
    public static Shape.Prototype.List pList;

    public ShapeTrainer( ) {
        super("Shape Trainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        G.fillBack(g);
        g.setColor(Color.BLACK);
        g.drawString(curName,600,30);
        g.drawString(curState, 700,30);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        if(pList!=null) pList.show(g);
    }

    public void setState(){
        pList=null;
        curState=(curName.equals("") || curName.equals("DOT"))?ILLEGAL: UNKNOWN;
        if(curState.equals(UNKNOWN)){
            if(Shape.DB.containsKey(curName)){
                curState=KNOWN;
                pList=Shape.DB.get(curName).prototypes;
            }
        }
    }
    public void keyTyped(KeyEvent key){
        char c= key.getKeyChar();
        System.out.println("typed: "+ c);
        curName=(c==' '|| c==0x0D || c==0x0A)? "": curName+c;
        setState();
        if(c==0x0D || c==0x0A){Shape.saveShapeDB();}
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY());repaint();}
    @Override
    public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY());repaint();}
    @Override
    public void mouseReleased(MouseEvent me) {
        if(curState!=ILLEGAL){
            Ink ink=new Ink();
            Shape.Prototype proto;
            if(pList==null){
                Shape s=new Shape(curName);
                Shape.DB.put(curName, s);
                pList=s.prototypes;
            }
            if(pList.bestDist(ink.norm)<UC.NO_MATCH_DIST){
                proto=Shape.Prototype.List.bestMatch;
                proto.blend(ink.norm);
            }else{
                proto=new Shape.Prototype();
                pList.add(proto);
            }
            setState();
        }
        repaint();}
}
