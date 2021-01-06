package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;

//The group of 5 lines on which the notes are written are called staffs,There are typically 5 stafflines in a staff thought there are varients usually when writing tablature for specific instruments.
public class Staff extends Mass {

    public Sys sys; //single system lives in a staff
    public int iStaff;  // the index of WHERE it lives in the system
    public Staff.Fmt fmt; // the format used for drawing this staff


    public Staff(Sys sys, int iStaff) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.fmt = sys.page.sysFmt.get(iStaff);

        //add reaction to draw bar line
        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture g) {
                G.LoHi margin = Page.PAGE.xMargin;
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                if (x < margin.lo || x > (margin.hi + UC.BAR_TO_MARGIN_SNAP)) {
                    return UC.NO_BID;
                }
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 30) ? d + UC.BAR_TO_MARGIN_SNAP : UC.NO_BID; //if the distance is small enough, it's a bid, biased to prefer bar cycle gesture
            }

            @Override
            public void act(Gesture g) {
                int rightMargin = Page.PAGE.xMargin.hi;
                int x = g.vs.xM();
                if (x > rightMargin - UC.BAR_TO_MARGIN_SNAP) {
                    x = rightMargin;
                }
                new Bar(Staff.this.sys, x);
            }
        });
    }

    public int sysOff() {
        return sys.page.sysFmt.staffOffsets.get(iStaff);
    }

    public int yTop() {
        return sys.yTop() + sysOff();
    }

    public int yBot() {
        return yTop() + fmt.height();
    }

    //--------staff format
    public static class Fmt {
        public int nlines = 5;
        public int H = 8; //half of the spacing between two lines
        public boolean barContinues = false;

        public void toggleBarContinues() {
            barContinues = !barContinues;
        }

        public int height() {
            return 2 * H * (nlines - 1);
        }

        public void showAt(Graphics g, int y, Page page) {
            g.setColor(Color.GRAY);
            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, h = 2 * H;
            for (int i = 0; i < nlines; i++) {
                g.drawLine(x1, y, x2, y);
                y += h;
            }
        }

    }
}
