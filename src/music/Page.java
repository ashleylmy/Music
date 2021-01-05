package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

//each page will consiste of as many Systems as will comfortably fit on a page
public class Page extends Mass {

    public static Page PAGE;
    public G.LoHi xMargin, yMargin;
    public Sys.Fmt sysFmt = new Sys.Fmt();
    public int sysGap; //Size of space between two systems, gets set when 2nd sys added
    public ArrayList<Sys> sysList = new ArrayList<>();
    public static Reaction R1;


    public Page(int y) {
        super("BACK");
        PAGE = this;
        int mm = 50;
        xMargin = new G.LoHi(mm, UC.WINDOW_WIDTH - mm);
        yMargin = new G.LoHi(y, UC.WINDOW_HEIGHT - mm);



        //disable initial reaction. add new staff and system
        // TODO: create two reactions
        Reaction.initialReactions.get(0).disable();
        addNewStaffFmtToSysFmt(y);
        addNewSys();

        //add new staff to the system
        addReaction( R1=new Reaction("E-W") {
            @Override
            public int bid(Gesture g) {
                return (g.vs.yM() < PAGE.allSysBot()) ? UC.NO_BID : 0;
            }

            @Override
            public void act(Gesture g) {
                addNewStaffFmtToSysFmt(g.vs.yM());
            }
        });

        // add a new system
        addReaction(new Reaction("W-W") {
            @Override
            public int bid(Gesture g) {
                return (g.vs.yM() < PAGE.allSysBot()) ? UC.NO_BID : 0;
            }

            @Override
            public void act(Gesture g) {
                if (PAGE.sysList.size() == 1) {
                    PAGE.sysGap = g.vs.yM() - PAGE.allSysBot();
                    R1.disable();
                }
                addNewSys();
            }
        });
    }

    public void show(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, yMargin.lo, 30, yMargin.lo);
        for (int i = 0; i < sysList.size(); i++) {
            sysFmt.showAt(g, sysTop(i), this);
        }
    }

    public void addNewStaffFmtToSysFmt(int y) {
        int yOff = y - yMargin.lo;
        int iStaff = sysFmt.size();
        sysFmt.addNew(yOff);
        //upgrade all system to match sys format
        for (Sys sys : sysList) {
            sys.addNewStaff(iStaff);
        }
    }

    //add new system in the system list
    public void addNewSys() {
        Sys sys = new Sys(this, sysList.size());
        sysList.add(sys);
        for (int i = 0; i < sysFmt.size(); i++) {
            sys.addNewStaff(i);
        }
    }

    // helper function to find the top margin
    public int sysTop(int iSys) {
        return yMargin.lo + iSys * (sysFmt.height() + sysGap);
    }

    public int allSysBot() {
        int n = sysList.size();
        return yMargin.lo + n * sysFmt.height() + (n - 1) * sysGap;
    }
}
