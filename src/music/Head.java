package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

//head note. All components drawing will extends Mass
public class Head extends Mass implements Comparable<Head>{

    public Staff staff;
    public int line; //lines increment starting from 0
    public Time time;
    public Glyph forcedGlyph = null;
    public Stem stem = null; //first creat note head, stem is null
    public boolean wrongSide = false;


    public Head(Staff staff, int x, int y) { //pass the staff and x, y coordinates from the gesture
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x); //convert x to Time
        time.heads.add(this);
        this.line = staff.lineOfY(y);
        System.out.println("Line: " + line);

        //stem or unstem heads
        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                int w = Head.this.w(), yH = Head.this.y();
                if (y1 > yH || y2 < yH) {
                    return UC.NO_BID;
                }
                int hL = Head.this.time.x, hR = hL + w; //head's left and right
                if (x < hL - w || x > hR + w) {
                    return UC.NO_BID;
                }//stem too far away from head
                //decide which side the stem is on and up/down
                if (x < (hL + w / 2)) {
                    return hL - x;
                }
                if (x >( hR - w / 2)) {
                    return x - hR;
                }
                return UC.NO_BID;
            }

            @Override
            public void act(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean up = x >( t.x + w / 2);
                if (Head.this.stem == null) {
                    //t.stemHead(staff, up, y1, y2);
                    Stem.getStem(staff, time, y1, y2, up);
                }else{
                    t.unStemHead(y1, y2);
                }
            }
        });

        //augmentation dot
        addReaction(new Reaction("DOT") {
            @Override
            public int bid(Gesture g) {
                int xH= Head.this.x(), yH=Head.this.y(), h=Head.this.staff.H(), w=Head.this.w();
                int x=g.vs.xM(), y=g.vs.yM();
                if(x<xH || x>xH+2*w || y<yH-h || y>yH+h) {return UC.NO_BID;}
                return Math.abs(xH+w-x)+Math.abs(yH-y);
            }

            @Override
            public void act(Gesture g) {
                if(Head.this.stem!=null)
                Head.this.stem.cycleDot();
            }
        });
    }

    public void show(Graphics g) {
        g.setColor(wrongSide ? Color.RED: Color.BLUE);
        int h = staff.H();
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, h, x(), staff.yTop() + line * h);
        if(stem!=null){
            //draw dots
            int off = UC.REST_AUG_DOT_OFFSET, sp=UC.AUG_DOT_SPACING;
            for(int i=0; i<stem.nDot; i++){
                g.fillOval(time.x+off+i*sp, y()-3*h/2, 2*h/3, 2*h/3);
            }
        }
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this);
            if (stem.heads.size() == 0) {
                //if the head is the last
                stem.deleteStem();
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null) {
            unStem();
        } // if the head is in stem, unstem first
        s.heads.add(this);
        stem = s;
    }


    public Glyph normalGlyph() {
        if(stem==null){return Glyph.HEAD_Q;}
        if(stem.nFlag == -1 ){return Glyph.HEAD_HALF;}
        if(stem.nFlag == -2 ){return Glyph.HEAD_W;}
        return Glyph.HEAD_Q;
    }

    public int y() {
        return staff.yLine(line);
    }

    //Adjust x
    public int x() {
        int res=time.x;
        if(wrongSide){res+=(stem!=null &&stem.isUp)? w(): -w();}
        return res;
    }

    //delete head
    //this is a stub
    public void deleteMass() {
        time.heads.remove(this);
    }

    //get the note head width
    public int w() {
        return 24 * staff.H() / 10;
    }

    //set rules for sorting the heads
    @Override
    public int compareTo(Head head) {
        return (staff.iStaff!=head.staff.iStaff)? staff.iStaff-head.staff.iStaff: line-head.line;
    }


    //--------------------List of heads--------------------//
    public static class List extends ArrayList<Head> {
    }
}
