package reaction;

import music.I;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//---------------keeps tracking Layers-----------------------//
public class Layer extends ArrayList<I.Show> implements I.Show {
    public String name;
    public static HashMap<String, Layer> byName=new HashMap<>();
    public static Layer ALL = new Layer("ALL");//sigle layer that contains all layers

    public Layer(String name) {
        this.name = name;
        if(!name.equals("ALL")){ALL.add(this);}
        byName.put(name, this);
    }

    public static void nuke() { for(I.Show lay: ALL){((Layer)lay).clear();}}//casting into layers

    @Override
    public void show(Graphics g) {for(I.Show item:this){item.show(g);}}
}
