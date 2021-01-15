package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Stem extends Duration implements Comparable<Stem> {
    public Staff staff;
    public Head.List heads;
    boolean isUp = true;
    public Beam beam = null;

    public Stem(Staff staff, Head.List heads, boolean up) {
        super();
        this.staff = staff;
        this.isUp = up;
        for (Head h : heads) {
            h.unStem();
            h.stem = this;
        }
        this.heads = heads;
        staff.sys.stems.addStem(this);
        setWrongSize();

        //increment flags
        addReaction(new Reaction("E-E") {
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.incFlag();
            }
        });

        //decrease flag
        addReaction(new Reaction("W-W") {
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.decFlag();
            }
        });
    }

    // factory method gets a stem IF there are heads that match the y values at the given time
    public static Stem getStem(Staff staff, Time time, int y1, int y2, boolean up) {
        Head.List heads = new Head.List();
        for (Head h : time.heads) {
            int yH = h.y();
            if (yH > y1 && yH < y2) {
                heads.add(h);
            }
        }
        if (heads.size() == 0) {
            return null;
        } // no stem created if no heads
        Beam b = internalStem(staff.sys, time.x, y1, y2); // possibly this is internal stem in beamed group
        Stem res = new Stem(staff, heads, up); // create the stem
        if (b != null) {
            b.addStem(res);
            res.nFlag = 1;
        } // if it was internal, then join the beam and gets one flag.
        return res;
    }

    // returns non-null IF we find a beam crossed by line
    private static Beam internalStem(Sys sys, int x, int y1, int y2) {
        for (Stem s : sys.stems) {
            if (s.beam != null && s.x() < x && s.yLo() < y2 && s.yHi() > y1) {
                int bx = s.beam.first().x(), by = s.beam.first().yBeamEnd();
                int ex = s.beam.last().x(), ey = s.beam.last().yBeamEnd();
                if (Beam.verticalLineCrossesSegment(x, y1, y2, bx, by, ex, ey)) {
                    return s.beam;
                }
            }
        }
        return null;
    }

    private int bidLineCrossStem(Gesture g) {
        int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), xs = Stem.this.heads.get(0).time.x; //xs is the stem's x
        if (x1 > xs || x2 < xs) {
            return UC.NO_BID;
        }
        int y1 = Stem.this.yLo(), y2 = Stem.this.yHi();
        if (y < y1 || y > y2) {
            return UC.NO_BID;
        }
        return Math.abs(y - (y1 + y2) / 2) + 55;//biased to lose bid to a beam
    }

    @Override
    public void show(Graphics g) {
        //make sure there are heads drew before
        if (nFlag >= -1 && heads.size() > 0) {
            int x = x(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.setColor(this.beam == null ? Color.BLACK : Color.ORANGE);
            g.drawLine(x, yH, x, yB);

            if (nFlag > 0 && beam == null) {
                if (nFlag == 1) {
                    (isUp ? Glyph.FLAG1D : Glyph.FLAG1U).showAt(g, h, x, yB);
                }
                if (nFlag == 2) {
                    (isUp ? Glyph.FLAG2D : Glyph.FLAG2U).showAt(g, h, x, yB);
                }
                if (nFlag == 3) {
                    (isUp ? Glyph.FLAG3D : Glyph.FLAG3U).showAt(g, h, x, yB);
                }
                if (nFlag == 4) {
                    (isUp ? Glyph.FLAG4D : Glyph.FLAG4U).showAt(g, h, x, yB);
                }
            }
        }
    }


    public Head firstHead() {
        return heads.get(isUp ? heads.size() - 1 : 0);
    }

    public Head lastHead() {
        return heads.get(isUp ? 0 : heads.size() - 1);
    }

    public int yFirstHead() {
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }

    public int x() {
        Head h = firstHead();
        return h.time.x + (isUp ? h.w() : 0);
    }

    public int yBeamEnd() {
        if (this.beam != null && beam.stems.size() > 1 && beam.first() != this && beam.last() != this) {
            Stem b = beam.first(), e = beam.last();
            return Beam.yOfX(x(), b.x(), b.yBeamEnd(), e.x(), e.yBeamEnd());
        }
        Head h = lastHead();
        int line = h.line;
        //default extension one octave;
        line += (isUp ? -7 : 7);
        //adjust the end if more than two flags
        int flagIncrement = nFlag > 2 ? 2 * (nFlag - 2) : 0;
        line += (isUp ? -flagIncrement : flagIncrement);
        if ((isUp && line > 4) || (!isUp && line < 4)) {
            line = 4;
        }
        return h.staff.yLine(line);
    }

    public int yLo() {
        return isUp ? yBeamEnd() : yFirstHead();
    }

    public int yHi() {
        return isUp ? yFirstHead() : yBeamEnd();
    }

    public void deleteStem() {
        deleteMass();
    }

    public void setWrongSize() {
        Collections.sort(heads);
        //i is the first head on the stack
        int i, last, next;
        //direction of stems will decide the start of prev head
        if (isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        } else {
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head prevHead = heads.get(i);
        prevHead.wrongSide = false;
        while (i != last) {
            i += next;
            Head nextHead = heads.get(i);
            //if differ from prev head less than one or prev head is in wrongside
            nextHead.wrongSide = prevHead.staff == nextHead.staff && Math.abs(nextHead.line - prevHead.line) <= 1 &&
                    !prevHead.wrongSide;
            prevHead = nextHead;
        }
    }

    @Override
    public int compareTo(Stem stem) {
        return x() - stem.x();
    }

    //------------------------List of Stems------------------//
    public static class List extends ArrayList<Stem> {

        public G.LoHi yRange;

        public void addStem(Stem s) {
            if (yRange == null) {
                yRange = new G.LoHi(s.yLo(), s.yHi());
            }
            yRange.add(s.yLo());
            yRange.add(s.yHi());
            add(s);
        }

        public boolean fastReject(int y1, int y2) {
            return y1 > yRange.hi || y2 < yRange.lo;
        }

        public void sort() {
            Collections.sort(this);
        }

        public ArrayList<Stem> allIntersectors(int x1, int y1, int x2, int y2) {
            Stem.List res = new Stem.List();
            for (Stem s : this) {
                int x = s.x(), y = Beam.yOfX(x, x1, y1, x2, y2);
                if (x > x1 && x < x2 && y > s.yLo() && y < s.yHi()) {
                    System.out.print("x, y "+ "("+ x+ ","+ y+ ")");
                    System.out.print("x1, y1 "+ "("+ x1+ ","+ y1+ ")");
                    System.out.print("x2, y2 "+ "("+ x2+ ","+ y2+ ")");
                    System.out.println("ylo, yhi "+ "("+ s.yLo()+ ","+ s.yHi()+ ")");
                    res.add(s);
                }
            }
            return res;
        }
    }
}
