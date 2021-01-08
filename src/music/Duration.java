package music;

import reaction.Mass;

import java.awt.*;


//note heads and rest will have duration

public abstract class Duration extends Mass {
    public int nFlag =0, nDot =0;

    public Duration() {
        super("NOTE");
    }

    public abstract void show(Graphics g);

    public void incFlag(){ if(nFlag<4) nFlag++;} //increment flags

    public void decFlag(){ if(nFlag>-2) nFlag--;} //decrement flags

    public void cycleDot(){
        nDot++;
        if(nDot>3){nDot=0;}
    }
}
