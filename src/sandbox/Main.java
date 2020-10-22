package sandbox;

import graphicsLib.Window;
import reaction.ShapeTrainerApp;

public class Main {
    //function signature
    //shortcut psvm enter
    public static void main(String[] args){
        //Window.PANEL= new Paint();
        //Window.PANEL= new Squares();
        //Window.PANEL = new PaintInk();
        //Window.PANEL = new ShapeTrainerApp();
        Window.PANEL=new ReactionTest();
        Window.launch();
    }
}
