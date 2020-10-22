package music;

import reaction.Gesture;

import java.awt.*;

public interface I {
    //no implement in interface, only define

    interface Hit{ public boolean hit(int x, int y);}
    interface Area extends Hit{
        void dn(int x, int y);
        void drag(int x, int y);
        void up(int x, int y);
    }
    interface Show{ void show(Graphics g);}
    interface Act{ void act(Gesture g);}
    interface React extends Act{int bid(Gesture g);}
}
