package reaction;

import graphicsLib.G;
import music.I;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static sandbox.PaintInk.pList;

public class Shape implements Serializable {
    public Prototype.List prototypes = new Prototype.List();
    public String name;
    public static String fileName="ShapeDB.DAT";
    public static HashMap<String, Shape> DB = loadShapeDB();
    public static Shape DOT= DB.get("DOT");
    public static Collection<Shape> LIST= DB.values();

    public Shape(String name) {
        this.name = name;
    }

    public static void saveShapeDB(){
        try{
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(DB);
            System.out.println("Saved"+fileName);
            oos.close();
        }catch (Exception e){
            System.out.println("Save DB Fail");
            System.out.println(e);
        }
    }
    private static HashMap<String, Shape> loadShapeDB() {
        HashMap<String, Shape>  res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        try{
            System.out.println("Attempt DB Load...");
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));
            res=(HashMap<String, Shape>)ois.readObject();
            System.out.println("Successful Load "+ res.keySet());
            ois.close();
        }catch (Exception e){
            System.out.println("Load DB failed");
            System.out.println(e);
        }
        return res;
    }

    public static Shape recognize(Ink ink){
        if(ink.vs.size.x<UC.DOT_THRESHOLD && ink.vs.size.y<UC.DOT_THRESHOLD) return DOT;
        Shape bestMatch=null;
        int bestSoFar=UC.NO_MATCH_DIST;
        for(Shape s:LIST){
            int d=s.prototypes.bestDist(ink.norm);
            if(d<bestSoFar){ bestSoFar=d; bestMatch=s;}
        }
        return bestMatch;
    }

    public  static Shape getOrCreate(String name){
        if(!DB.containsKey(name)){DB.put(name, new Shape(name));}
        return DB.get(name);
    }



    //----------------------Nested Prototype Class----------------------------//
    public static class Prototype extends Ink.Norm  implements Serializable {
        int nBlend = 1;

        public Prototype(Ink.Norm norm) {
            super(norm);
        }

        public void blend(Ink.Norm norm) {
            blend(norm, nBlend);
            nBlend++;
        }

        //----------------------Nested List Class----------------------------//
        public static class List extends ArrayList<Prototype> implements Serializable {
            public static Prototype bestMatch;
            private static final int m =UC.PROTOTYPE_LIST_MARGIN, w = UC.PROTOTYPE_LIST_SIZE;
            private static G.VS showBox = new G.VS(m, m, w, w);

            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.NO_MATCH_DIST;
                for (Prototype p : this) {
                    int d = p.dist(norm);
                    if (d < bestSoFar) {
                        bestSoFar = d;
                        bestMatch = p;
                    }
                }
                return bestSoFar;
            }

            public int nHit(int x, int y){ return x/(m+w);}
            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for (int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

            public Prototype blendOrAdd(Ink.Norm norm) {
                int d = bestDist(norm);
                if (d < UC.NO_MATCH_DIST) {
                    bestMatch.blend(norm);
                    return bestMatch;
                }
                Prototype res = new Prototype(norm);
                add(res);
                return res;
            }
        }
    }

        //----------------------------------------Trainer--------------------------------------------//
        public static class Trainer implements I.Area {
            public NameState ns = new NameState();
            public static boolean isTraining = false;

            public void keyTyped(char c) { ns.keyTyped(c);}

            @Override
            public boolean hit(int x, int y) {
                return isTraining;
            }

            @Override
            public void dn(int x, int y) {
                Ink.BUFFER.dn(x, y);
            }

            @Override
            public void drag(int x, int y) {
                Ink.BUFFER.drag(x, y);
            }

            @Override
            public void up(int x, int y) {
                if (ns.illegal()) { ns.shapeBeingTrained = null; return; }
                Ink ink = new Ink();
                Shape.Prototype proto;
                ns.shapeBeingTrained = Shape.getOrCreate(ns.name);
                Prototype.List pList=ns.shapeBeingTrained.prototypes;
                //click stored shape to remove from database
                if(pList.size()>0 && y<UC.PROTOTYPE_LIST_Y_LIM){
                    int iProto=pList.nHit(x, y);
                    if(iProto<pList.size()) pList.remove(iProto);
                }else {
                   pList.blendOrAdd(ink.norm);
                }
                ns.setState();
            }

            public void show(Graphics g) {
                g.setColor(UC.SHAPE_TRAINER_BACKGROUND_COLOR);
                g.fillRect(0, 0, 5000, 5000);
                g.setColor(Color.BLACK);
                Ink.BUFFER.show(g);
                ns.show(g);
            }

            //-------------------------------------Name State----------------------------------------//
            public static class NameState {
                public static String
                        UNKNOWN = "<-unknown", ILLEGAL = "<-illegal", KNOWN = "<-known",
                        INSTRUCTIONS = "type name and draw examples - space clears name - enter saves DB - click saved shape to delete from DB";
                public String name = "";
                public String state = ILLEGAL;
                public Shape shapeBeingTrained;

                public void show(Graphics g) {
                    g.setColor(Color.BLACK);
                    g.drawString(INSTRUCTIONS, 20, 100);
                    if (state == ILLEGAL) g.setColor(Color.RED);
                    g.drawString(name, 600, 30);
                    g.drawString(state, 700, 30);
                    if (shapeBeingTrained != null) {
                        shapeBeingTrained.prototypes.show(g);
                    }
                }

                public void setState() {
                    shapeBeingTrained = null;
                    state = (name.equals("") || name.equals("DOT")) ? ILLEGAL : UNKNOWN;
                    if (state.equals(UNKNOWN)) {
                        if (Shape.DB.containsKey(name)) {
                            state = KNOWN;
                            shapeBeingTrained = Shape.DB.get(name);
                        }
                    }
                }

                public void keyTyped(char c) {
                    System.out.println("typed: " + c);
                    name = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : name + c;
                    setState();
                    if (c == 0x0D || c == 0x0A) {
                        Shape.saveShapeDB();
                    }
                }

                public boolean illegal() {
                    return state.equals(ILLEGAL);
                }
            }
        }
    }

