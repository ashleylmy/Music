package music;

import reaction.Mass;

public class Beam extends Mass {
    public Stem.List stems = new Stem.List();

    public Beam(Stem firstStem, Stem lastStem) {
        super("NOTE");
        stems.add(firstStem);
        stems.add(lastStem);
    }

    //get the first and last stems. stems are sorted by x value
    public Stem first() {
        return stems.get(0);
    }

    public Stem last() {
        return stems.get(stems.size() - 1);
    }

    public void deleteBeam() {
        for (Stem stem : stems) {
            stem.beam = null;
        }
        deleteMass();
    }

    public void addStem(Stem s) {
        if (s.beam == null) {
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort();
        }
    }

    public static int yOfX(int x, int x1, int y1, int x2, int y2) {
        int dY = y2 - y1, dX = x2 - x1;
        return (x - x1) * dY / dX + y1;
    }

    public static int mx1, my1, mx2, my2; // coordinates for the Master Beam

    public static void setMasterBeam(int x1, int y1, int x2, int y2) {
        mx1 = x1;
        my1 = y1;
        mx2 = x2;
        my2 = y2;
    }

    public static int yOfX(int x) {
        int dy = my2 - my1, dx = mx2 - mx1;
        return (x - mx1) * dy / dx + my1;
    }

    public void setMasterBeam() {
        mx1 = first().x();
        my1 = first().yBeamEnd();
        mx2 = last().x();
        my2 = last().yBeamEnd();
    }

    public static boolean verticalLineCrossesSegment(int x, int y1, int y2, int bx, int by, int ex, int ey) {
        if (x < bx || x > ex) {
            return false;
        }
        int y = yOfX(x, bx, by, ex, ey);
        if (y1 < y2) {
            return (y1 < y && y < y2);
        } else {
            return (y2 < y && y < y1);
        }
    }


}
