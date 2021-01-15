package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

//The staffs (one for each voice) are typically grouped vertically into a system,each System contains as many staffs as it needs to match the instrumentation of the piece
public class Sys extends Mass {

    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;
    public Sys.Fmt fmt;
    public Time.List times;
    public Stem.List stems = new Stem.List();


    public Sys(Page page, int iSys) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        times = new Time.List(this); // the system has a time list

        addReaction(new Reaction("E-E") { // Beam Stems.
            public int bid(Gesture g) {
                int x1 = g.vs.xL(), y1 = g.vs.yL(), x2 = g.vs.xH(), y2 = g.vs.yH(); // collect the gesture numbers
                if (stems.fastReject(y1, y2)) {
                    return UC.NO_BID;
                } // reject if gesture does not overlap the list of stems
                ArrayList<Stem> tempStems = stems.allIntersectors(x1, y1, x2, y2);// possible overlap: find intersecting stems
                System.out.println("Crossed "+tempStems.size()+" stems");
                if (tempStems.size() < 2) {
                    return UC.NO_BID;
                } // crossing a single stem is a Stem reaction, not a Sys reaction
                Beam b = tempStems.get(0).beam; //check if all crossed stems are owned by the same beam (including null!)
                for (Stem s : tempStems) {
                    if (s.beam != b) {
                        return UC.NO_BID;
                    }
                } // different owners is reject
                if (b == null && tempStems.size() != 2) {
                    return UC.NO_BID;
                } // only new Beam if exactly 2 null beamed stems
                // .. also, only new if both stems currenly have zero nFlag
                if (b == null && (tempStems.get(0).nFlag != 0 || tempStems.get(1).nFlag != 0)) {
                    return UC.NO_BID;
                }
                return 50; // this is either a create new Beam or flags a set of beams
                // NOW go bias single stem E-E reactions to over 50 so they DON'T win a multiple stem crossing gesture
            }

            public void act(Gesture g) {
                int x1 = g.vs.xL(), y1 = g.vs.yL(), x2 = g.vs.xH(), y2 = g.vs.yH();
                ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
                Beam b = temp.get(0).beam;
                if (b == null) {
                    new Beam(temp.get(0), temp.get(1));
                } else {
                    for (Stem s : temp) {
                        s.incFlag();
                    }
                }
            }
        });
    }

    public void addNewStaff(int iStaff) {
        staffs.add(new Staff(this, iStaff));
    }

    public int yTop() {
        return page.sysTop(iSys);
    }

    public int yBot() {
        return staffs.get(staffs.size() - 1).yBot();
    }

    // get time
    public Time getTime(int x) {
        return times.getTime(x);
    }

    //--------------------------------Sys format-------------------------------//
    public static class Fmt extends ArrayList<Staff.Fmt> {
        public ArrayList<Integer> staffOffsets = new ArrayList<>();
        public int maxH = 8;

        public void addNew(int yOff) {
            add(new Staff.Fmt());
            staffOffsets.add(yOff);
        }

        public int height() {
            int last = size() - 1;
            return get(last).height() + staffOffsets.get(last);
        }

        public void showAt(Graphics g, int y, Page page) {
            for (int i = 0; i < size(); i++) {
                get(i).showAt(g, y + staffOffsets.get(i), page);
            }
            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, y2 = y + height();
            g.drawLine(x1, y, x1, y2);
            g.drawLine(x2, y, x2, y2);
        }

    }

}
