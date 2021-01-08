package music;

import reaction.Mass;

import java.awt.*;
import java.util.ArrayList;

//The staffs (one for each voice) are typically grouped vertically into a system,each System contains as many staffs as it needs to match the instrumentation of the piece
public class Sys extends Mass {

    public ArrayList<Staff> staffs=new ArrayList<>();
    public Page page;
    public int iSys;
    public Sys.Fmt fmt;
    public Time.List times;


    public Sys(Page page, int iSys) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        times=new Time.List(this); // the system has a time list
    }

    public void addNewStaff(int iStaff){
        staffs.add(new Staff(this, iStaff));
    }

    public int yTop(){return page.sysTop(iSys);}
    public int yBot(){return staffs.get(staffs.size()-1).yBot();}

    // get time
    public Time getTime(int x){
        return times.getTime(x);
    }

    //--------------------------------Sys format-------------------------------//
    public static class Fmt extends ArrayList<Staff.Fmt>{
        public ArrayList<Integer> staffOffsets=new ArrayList<>();
        public int maxH=8;

        public void addNew(int yOff){
            add(new Staff.Fmt());
            staffOffsets.add(yOff);
        }

        public int height(){
            int last=size()-1;
            return get(last).height()+staffOffsets.get(last);
        }

        public void showAt(Graphics g, int y, Page page){
            for(int i =0; i<size(); i++){
                get(i).showAt(g, y+ staffOffsets.get(i), page);
            }
            int x1=page.xMargin.lo, x2=page.xMargin.hi, y2=y+height();
            g.drawLine(x1,y, x1, y2);
            g.drawLine(x2, y, x2, y2);
        }

    }

}
