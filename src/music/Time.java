package music;

import java.util.ArrayList;

//use List to store times and fix node heads lineup requirements

public class Time {
    public int x;

    private Time(int x, List list) {
        this.x = x;
        list.add(this);
    }

    //----------------List of Time-----------------//
    public static class List extends ArrayList<Time> {
        public Sys sys;

        public List(Sys sys) {
            this.sys = sys;
        }

        public Time getTime(int x) { //find the closest time to x, if too far, create new one
            Time timeRes = null;
            int dis = UC.SNAP_TIME;
            for (Time time : this) {
                int d = Math.abs(x - time.x);
                if (d < dis) {
                    dis = d;
                    timeRes = time;
                }
            }
            return (timeRes==null)? new Time(x, this): timeRes;
        }
    }
}
