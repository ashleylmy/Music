package music;

import java.awt.*;

public class Rest extends Duration {
    public Staff staff;
    public Time time;
    public int line = 4; // default value for single voice instruments

    public Rest(Staff staff, Time time) {
        this.staff = staff;
        this.time = time;
    }

    @Override
    public void show(Graphics g) {
        int h=staff.H(), y=y();
        //different cases
        if(nFlag==-2){Glyph.REST_W.showAt(g, h, time.x, y);}
        if(nFlag==-1){Glyph.REST_H.showAt(g, h, time.x, y);}
        if(nFlag==0){Glyph.REST_Q.showAt(g, h, time.x, y);}
        if(nFlag==1){Glyph.REST_1F.showAt(g, h, time.x, y);}
        if(nFlag==2){Glyph.REST_2F.showAt(g, h, time.x, y);}
        if(nFlag==3){Glyph.REST_3F.showAt(g, h, time.x, y);}
        if(nFlag==4){Glyph.REST_4F.showAt(g, h, time.x, y);}
    }

    //convert line value to y value
    public int y(){
        return staff.yLine(line);
    }
}
