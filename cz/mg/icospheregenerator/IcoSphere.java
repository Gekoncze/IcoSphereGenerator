package cz.mg.icospheregenerator;

import java.util.ArrayList;


public class IcoSphere {
    public static Model create(int subdivisions) {
        Model model = new Model();

        double w = (1.0 + Math.sqrt(5.0)) / 2.0;
        double h = 1;

        // first plane
        model.vertices.add(new Vertex(w, h, 0)); // 0
        model.vertices.add(new Vertex(w, -h, 0)); // 1
        model.vertices.add(new Vertex(-w, -h, 0)); // 2
        model.vertices.add(new Vertex(-w, h, 0)); // 3

        // second plane
        model.vertices.add(new Vertex(0, w, h)); // 4
        model.vertices.add(new Vertex(0, w, -h)); // 5
        model.vertices.add(new Vertex(0, -w, -h)); // 6
        model.vertices.add(new Vertex(0, -w, h)); // 7

        // third plane
        model.vertices.add(new Vertex(h, 0, w)); // 8
        model.vertices.add(new Vertex(h, 0, -w)); // 9
        model.vertices.add(new Vertex(-h, 0, -w)); // 10
        model.vertices.add(new Vertex(-h, 0, w)); // 11

        for (Vertex v : model.vertices) normalize(v);
        
        model.triangles.add(new Triangle(4, 8, 11));
        model.triangles.add(new Triangle(5, 10, 9));
        model.triangles.add(new Triangle(11, 8, 7));
        model.triangles.add(new Triangle(10, 6, 9));
        model.triangles.add(new Triangle(0, 1, 8));
        model.triangles.add(new Triangle(0, 9, 1));
        model.triangles.add(new Triangle(3, 11, 2));
        model.triangles.add(new Triangle(3, 2, 10));
        model.triangles.add(new Triangle(5, 0, 4));
        model.triangles.add(new Triangle(3, 5, 4));
        model.triangles.add(new Triangle(1, 6, 7));
        model.triangles.add(new Triangle(6, 2, 7));
        model.triangles.add(new Triangle(4, 0, 8));
        model.triangles.add(new Triangle(5, 9, 0));
        model.triangles.add(new Triangle(9, 6, 1));
        model.triangles.add(new Triangle(8, 1, 7));
        model.triangles.add(new Triangle(11, 7, 2));
        model.triangles.add(new Triangle(3, 4, 11));
        model.triangles.add(new Triangle(5, 3, 10));
        model.triangles.add(new Triangle(10, 2, 6));

        for (int s = 0; s < subdivisions; s++) {
            HashMap2i1i middles = new HashMap2i1i(model.triangles.size() * 3);
            ArrayList<Triangle> newTriangles = new ArrayList<>();
            for (int t = 0; t < model.triangles.size(); t++) {
                Triangle oldTriangle = model.triangles.get(t);
                int v1 = oldTriangle.v1;
                int v2 = oldTriangle.v2;
                int v3 = oldTriangle.v3;
                //       v1
                //       /\
                //      /  \
                //   m3/____\m1
                //    /\    /\
                //   /  \  /  \
                //  /____\/____\
                //v3     m2     v2 
                int m1 = getMiddle(middles, v1, v2, model);
                int m2 = getMiddle(middles, v2, v3, model);
                int m3 = getMiddle(middles, v3, v1, model);
                newTriangles.add(new Triangle(v1, m1, m3));
                newTriangles.add(new Triangle(m1, v2, m2));
                newTriangles.add(new Triangle(m3, m2, v3));
                newTriangles.add(new Triangle(m1, m2, m3));
            }
            model.triangles = newTriangles;
        }
        return model;
    }

    private static int getMiddle(HashMap2i1i middles, int i1, int i2, Model model) {
        if (middles.get(i1, i2) >= 0) return middles.get(i1, i2);
        if (middles.get(i2, i1) >= 0) return middles.get(i2, i1);
        Vertex v1 = model.vertices.get(i1);
        Vertex v2 = model.vertices.get(i2);
        double x = (v1.x + v2.x) / 2;
        double y = (v1.y + v2.y) / 2;
        double z = (v1.z + v2.z) / 2;
        int i = model.vertices.size();
        Vertex v = new Vertex(x, y, z);
        normalize(v);
        model.vertices.add(v);
        middles.put(i1, i2, i);
        return i;
    }

    private static void normalize(Vertex v) {
        double length = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        v.x /= length;
        v.y /= length;
        v.z /= length;
    }

    private static class HashMap2i1i {

        public Container[] data = null;

        private class Container {

            public int i1, i2;
            public int value;
            public Container nextItem;

            public Container(int i1, int i2, int value, Container nextItem) {
                this.i1 = i1;
                this.i2 = i2;
                this.value = value;
                this.nextItem = nextItem;
            }
        }

        public HashMap2i1i(int size) {
            data = new Container[size];
        }

        public void put(int i1, int i2, int value) {
            int index = getIndex(i1, i2);
            data[index] = new Container(i1, i2, value, data[index]);
        }

        public int get(int i1, int i2) {
            Container current = data[getIndex(i1, i2)];
            while (current != null) {
                if (current.i1 == i1 && current.i2 == i2) {
                    return current.value;
                } else {
                    current = current.nextItem;
                }
            }
            return -1;
        }

        private int getIndex(int i1, int i2) {
            int hash1 = Integer.hashCode(i1);
            int hash2 = Integer.hashCode(i2);
            hash2 = (hash2 << 16) | (hash2 >> 16);
            int index = (hash1 ^ hash2) % data.length;
            if (index < 0) index *= -1;
            return index;
        }
    }
}
