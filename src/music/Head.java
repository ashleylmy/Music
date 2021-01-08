package music;

import reaction.Mass;

import java.awt.*;

//head note. All components drawing will extends Mass
public class Head extends Mass {

    public Staff staff;
    public int line; //lines increment starting from 0
    public Time time;


    public Head(Staff staff, int x, int y) { //pass the staff and x, y coordinates from the gesture
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x); //convert x to Time
        this.line=staff.lineOfY(y);
        System.out.println("Line: "+ line);
    }

    public void show(Graphics g){
        int h= staff.H();
        Glyph.HEAD_Q.showAt(g, h, time.x, staff.yTop()+line*h);
    }
}
