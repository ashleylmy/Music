package reaction;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;

public class ShapeTrainer extends Window {
    public static String UNKNOWN ="<-unknown";
    public static String ILLEGAL="<-illegal";
    public static String KNOWN = "<-known";
    public static String curName ="";
    public static String curState= ILLEGAL;

    public ShapeTrainer( ) {
        super("Shape Trainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBack(g);
        g.setColor(Color.BLACK);
        g.drawString(curName,600,30);
        g.drawString(curState, 700,30);
    }

    public void setState(){
        curState=(curName.equals("") || curName.equals("DOT"))?ILLEGAL: UNKNOWN;
    }
    public void keyTyped(KeyEvent key){
        char c= key.getKeyChar();
        System.out.println("typed: "+ c);
        curName=(c==' '|| c==0x0D || c==0x0A)? "": curName+c;
        setState();
        repaint();
    }
}
