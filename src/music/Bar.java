package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;

//  The reaction is to draw from the topline of a staff to the bottomline of that same staff
//  Barlines show up in several different formats and we will just use an int to know what type of barline this is. The formats are these
//
//        single - this is the normal shape - one single thin line.
//        double - this is two thin lines, used for indicating KeySig or TimeSig change
//        fine - this is a fat one with a thin one on the left - marks end of piece (fine - pronounced FeeNay - Italian meaning Finish or End)
//        repeat left - just like a fine, fat, thin on left but also dots on left
//        repeat right - fat line with thin on the right and dots on right
//        repeat double - fat line with thin and dots on both sides
public class Bar extends Mass {
    private static final int FAT = 2, RIGHT = 4, LEFT = 8; //bar type: bits or flags
    public Sys sys; //bar refers to system
    /*
    barType
    0 - normal single thin line
    1 - double thin line, used for key changes
    2 - thin, fat fine line
    4-7 fat, thin, dots repeat right (repeat bar going to the right)
    8-11 dots, thin, fat (repeat bar going to the left)
    12 - dots, thin, fat, thin, dots ( double repeat)
     */
    public int x, barType = 0;

    public Bar(Sys sys, int x) {
        super("BACK");
        this.sys = sys;
        this.x = x;

        addReaction(new Reaction("S-S") {  //cycle an exiting bar
            @Override
            public int bid(Gesture g) {
                int x=g.vs.xM();
                if(Math.abs(x-Bar.this.x)>UC.BAR_TO_MARGIN_SNAP*2){return UC.NO_BID;}
                int y1=g.vs.yL(), y2=g.vs.yH();
                int sysTop=Bar.this.sys.yTop(), sysBot=Bar.this.sys.yBot();
                //if the user draw outside the system, no bid
                if(y1<sysTop-10 || y2>sysBot+10){return UC.NO_BID;}
                G.LoHi margin=Page.PAGE.xMargin;
                if(x< margin.lo-10||x>margin.hi+10){return UC.NO_BID;}
                int d=Math.abs(x-Bar.this.x);
                return (d>UC.BAR_TO_MARGIN_SNAP)? UC.NO_BID: d;
            }

            @Override
            public void act(Gesture g) {
                Bar.this.cycleType();
            }
        });
    }

    public void show(Graphics g) {
        g.setColor(Color.RED);
        int N = sys.staffs.size();
        //sys could be null due to multi-thread, throw null pointer exception
        //        for (int i = 0; i < N; i++) {
        //            Staff staff = sys.staffs.get(i);
        //            g.drawLine(x, staff.yTop(), x, staff.yBot());
        //            if(barType==1){g.drawLine(x-4, staff.yTop(),  x-4, staff.yBot());}
        //        }
        int sysTop=sys.yTop(), y1=0, y2=0;
        boolean justSawBreak=true; //put wing at this break
        for(Staff staff: sys.staffs){
            int staffTop=staff.yTop();
            if(justSawBreak){y1=staffTop;}
            y2=staff.yBot();
            if(!staff.fmt.barContinues){drawLines(g, y1, y2);}
            justSawBreak=!staff.fmt.barContinues;
            if(barType>3){
                drawDots(g, x, staffTop);
            }
        }
    }

    public void drawLines(Graphics g, int y1, int y2){
        int H= sys.page.sysFmt.maxH;
        if(barType==0){thinBar(g, x, y1, y2);}
        if(barType==1){thinBar(g, x, y1, y2); thinBar(g, x-H, y1, y2);}
        if(barType==2){fatBar(g, x-H,y1, y2, H);thinBar(g, x-2*H, y1, y2);}
        if(barType>=4){
            if((barType&LEFT)!=0) {//bit is set
                thinBar(g, x-2*H, y1, y2);
                wings(g, x-2*H, y1, y2, -H, H);
            }
            if((barType&RIGHT)!=0) {//bit is set
                thinBar(g, x+H, y1, y2);
                wings(g, x+H, y1, y2, H, H);
            }
            }

    }

    //fill a fat bar
    public static void fatBar(Graphics g, int x, int y1, int y2, int dx){
        g.fillRect(x,y1, dx, y2-y1);
    }

    //fill a thin bar
    public static void thinBar(Graphics g, int x, int y1, int y2){
        g.drawLine(x, y1, x, y2);
    }

    //fill wing
    public static void wings(Graphics g, int x, int y1, int y2, int dx, int dy){
        g.drawLine(x, y1, x+dx, y1-dy);
        g.drawLine(x, y2, x+dx, y2+dy);
    }

    //interact with staff
    public void drawDots(Graphics g, int x, int top){
        int H= sys.page.sysFmt.maxH;
        if((barType & LEFT)!=0){
            g.fillOval(x-3*H, top+11*H/4, H/2, H/2);
            g.fillOval(x-3*H, top+19*H/4, H/2, H/2);
        }
        if((barType & RIGHT)!=0){
            g.fillOval(x+3*H/2, top+11*H/4, H/2, H/2);
            g.fillOval(x+3*H/2, top+19*H/4, H/2, H/2);
        }
    }

    //if the barType is greater than 2, cycle back to 0.
    public void cycleType() {
        barType++;
        if (barType > 2) barType = 0;
    }

    //^ -- exclusive OR, switch bits back and forth
    public void toggleLeft(){barType = barType^LEFT;}
    public void toggleRight(){barType ^= RIGHT;}
}
