package music;

import reaction.Mass;

import java.awt.*;
import java.util.ArrayList;

//head note. All components drawing will extends Mass
public class Head extends Mass {

    public Staff staff;
    public int line; //lines increment starting from 0
    public Time time;
    public Glyph forcedGlyph=null;
    public Stem stem=null; //first creat note head, stem is null
    public boolean wrongSide=false;



    public Head(Staff staff, int x, int y) { //pass the staff and x, y coordinates from the gesture
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x); //convert x to Time
        time.heads.add(this);
        this.line=staff.lineOfY(y);
        System.out.println("Line: "+ line);
    }

    public void show(Graphics g){
        int h= staff.H();
        (forcedGlyph !=null? forcedGlyph: normalGlyph()).showAt(g, h, time.x, staff.yTop()+line*h);
    }

    //TODO figure out what to return
    public Glyph normalGlyph(){
        return Glyph.HEAD_Q;
    }

    public int y(){
        return staff.yLine(line);
    }

    //TODO if the head is on the wrong side, need to change
    public int x(){
        return time.x;
    }

    //delete head
    //this is a stub
    public void delete(){
        time.heads.remove(this);
    }

    //get the note head width
    public int w(){
        return 24* staff.H()/10;
    }


    //--------------------List of heads--------------------//
    public static class List extends ArrayList<Head>{
    }
}
