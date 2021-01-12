package music;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;

public class Stem extends Duration {
    public Staff staff;
    public Head.List heads = new Head.List();
    boolean isUp = true;

    public Stem(Staff staff, boolean up) {
        super();
        this.staff = staff;
        this.isUp = up;
    }

    @Override
    public void show(Graphics g) {
        //make sure there are heads drew before
        if (nFlag >= -1 && heads.size() > 0) {
            int x = x(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
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
}
