package reaction;

import graphicsLib.G;
import music.I;
import music.UC;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Ink implements I.Show {
    //singleton Design Pattern, one static buffer holds all the points needed
    public static Buffer BUFFER= new Buffer();
    public static final int K = UC.NORM_SAMPLE_SIZE;
    public Norm norm;
    public G.VS vs;

    public Ink() {
       norm=new Norm();
       vs=BUFFER.bbox.getNewVS();
    }

    @Override
    public void show(Graphics g) {
        g.setColor(UC.INK_COLOR);
        norm.drawAt(g, vs);
    }

    //----------------------Norm----------------------------//
    public static class Norm extends G.PL  implements Serializable {
        public static final int K=UC.NORM_SAMPLE_SIZE, MAX=UC.NORM_COORD_MAX;
        public static final G.VS NORM_BOX=new G.VS(0,0,MAX,MAX);
        public Norm() {
            super(K);
            for (int i = 0; i < K; i++) {
                points[i].set(BUFFER.points[i*(BUFFER.n-1)/(K-1)]);
            }
            G.V.T.set(BUFFER.bbox, NORM_BOX);
            transform();
        }

        public Norm(Norm norm){
            super(K);
            for (int i = 0; i < K; i++) {
                points[i].set(norm.points[i]);
            }
        }
        public void drawAt(Graphics g, G.VS vs){
            G.V.T.set(NORM_BOX, vs);
            for(int i = 1; i<K; i++){
                g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());
            }
        }
        public int dist(Norm norm){
            int result=0;
            for(int i=0; i<K; i++){
                int dx = points[i].x-norm.points[i].x, dy=points[i].y-norm.points[i].y;
                result += dx*dx +dy*dy;
            }
            return result;
        }
        public void blend(Norm norm, int nBlend){
            for( int i =0; i<K; i++){
                points[i].blend(norm.points[i], nBlend);
            }
        }
    }

    //----------------------List----------------------------//
    public static class List extends ArrayList<Ink> implements I.Show{

        @Override
        public void show(Graphics g) { for(Ink ink:this ){ ink.show(g); } }
    }
    //----------------------Buffer(Polyline that collects mouse work)----------------------------//
    public static class Buffer extends G.PL implements I.Show, I.Area{
        public int n; //Number of Points in the buffer;
        public G.BBox bbox= new G.BBox();
        public static final int MAX= UC.INK_BUFFER_MAX;

        //sign of singleton since constructor is private
        private Buffer() {super(MAX);} //can only be constructed in the Ink class


        public void add(int x, int y){ if(n<MAX) {points[n++].set(x, y); bbox.add(x, y);}}

        public void clear(){ n=0;}

        @Override
        //put things in the buffer
        public void dn(int x, int y) { clear(); bbox.set(x, y); add(x, y); }

        @Override
        public void drag(int x, int y) { add(x, y); }

        @Override
        public void up(int x, int y) {}

        @Override
        public void show(Graphics g) { g.setColor(UC.INK_COLOR); drawN(g, n); }

        @Override
        public boolean hit(int x, int y) { return true;}
    }
}
