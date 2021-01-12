package reaction;

import music.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show {
    public Layer layer;

    public Mass(String layerName) {
        this.layer = Layer.byName.get(layerName);
        if(layer !=null){
            layer.add(this);
        }else{
            System.out.println( "Bad Layer name: "+ layerName);
        }
    }

    public void deleteMass(){
        clearAll(); //clears all the reactions in this list and in the byShape map

        layer.remove(this);
    }

    /*
       In the collection class, equal will compare two objects and return true if the value is the same even refers to different memory address
       override equal that uses == to compare two objects.
        */
    @Override
    public boolean equals(Object o) {
        return this==o;
    }

    public void show(Graphics g){ }

}
