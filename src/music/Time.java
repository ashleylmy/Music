package music;

import java.util.ArrayList;

//use List to store times and fix node heads lineup requirements

public class Time {
    public int x;
    public Head.List heads=new Head.List();

    private Time(int x, List list) {
        this.x = x;
        list.add(this);
    }

    public void unStemHead(int y1, int y2){
        for(Head head:heads){
            int y =head.y(); //y for this head
            if(y>y1 && y<y2){head.unStem();}
        }
    }
    public void removeHead(Head h){
        heads.remove(h);
        if(heads.size()==0){
            h.staff.sys.times.remove(this);
        }
    }

    //moved this function to Stem class.
/*    public void stemHead(Staff staff, boolean up, int y1, int y2){
        Stem stem= new Stem(staff, up);
        for(Head head:heads){
            int y =head.y(); //y for this head
            if(y>y1 && y<y2){head.joinStem(stem);}
        }
        if(stem.heads.size()>0){
            stem.setWrongSize();
        }
        stem.staff.sys.stems.addStem(stem);
    }
*/

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
