package music;

import reaction.Mass;

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
    }

    public int yTop(){ return sys.yTop()+sys.page.sysFmt.staffOffsets.get(iStaff);}

    //--------staff format
    public static class Fmt {
        public int nlines = 5;
        public int H = 8; //half of the spacing between two lines

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
