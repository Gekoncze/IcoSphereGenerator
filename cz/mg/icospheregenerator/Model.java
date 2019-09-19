package cz.mg.icospheregenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Model {
    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Triangle> triangles = new ArrayList<>();

    public Model(){}
    
    public void saveGlp(OutputStream stream) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));){
            for(Vertex v : vertices) bw.write("v3 " + v.x + " " + v.y + " " + v.z + "\n");
            bw.write("t3 0 0 0\n");
            bw.write("c4 255 255 255 255\n");
            for(Triangle t : triangles) bw.write("f3 " + t.v1 + "/0/0 " + t.v2 + "/0/0 " + t.v3 + "/0/0 0\n");
        }
    }
    
    public void saveObj(OutputStream stream) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));){
            for(Vertex v : vertices) bw.write("v " + v.x + " " + v.y + " " + v.z + "\n");
            for(Triangle t : triangles) bw.write("f " + (t.v1+1) + " " + (t.v2+1) + " " + (t.v3+1) + "\n");
        }
    }
}
