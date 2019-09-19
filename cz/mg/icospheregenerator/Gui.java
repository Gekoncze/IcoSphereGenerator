package cz.mg.icospheregenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Gui extends javax.swing.JFrame {
    public static final Color EDGE_COLOR = Color.BLACK;
    public static final Color FILL_COLOR = Color.LIGHT_GRAY;
    public static final Color TEXT_COLOR = Color.BLACK;
    public static final Color BACKGROUND_COLOR = Color.WHITE;

    private Model original;
    private Model modified;
    private double rx = 0;
    private double ry = 0;
    private int clickPositionX = 0;
    private int clickPositionY = 0;


    public Gui() {
        initComponents();
        setTitle("Ico Sphere Generator");
        setSize(800, 600);
        original = IcoSphere.create(0);
        modified = IcoSphere.create(0);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                super.mousePressed(event);
                clickPositionX = event.getX();
                clickPositionY = event.getY();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                super.mouseDragged(event);
                ry = event.getX() - clickPositionX;
                rx = event.getY() - clickPositionY;
                repaint();
            }
        });
    }
    
    private void prepareModel() {
        for(int i = 0; i < original.vertices.size(); i++){
            Vertex ov = original.vertices.get(i);
            Vertex mv = modified.vertices.get(i);
            mv.x = ov.x;
            mv.y = ov.y;
            mv.z = ov.z;
            transform(mv);
        }
        
        Collections.sort(modified.triangles, new Comparator<Triangle>() {
            @Override
            public int compare(Triangle t1, Triangle t2) {
                Model model = modified;
                Vertex t1v1 = model.vertices.get(t1.v1);
                Vertex t1v2 = model.vertices.get(t1.v2);
                Vertex t1v3 = model.vertices.get(t1.v3);
                Vertex t2v1 = model.vertices.get(t2.v1);
                Vertex t2v2 = model.vertices.get(t2.v2);
                Vertex t2v3 = model.vertices.get(t2.v3);
                double t1z = (t1v1.z + t1v2.z + t1v3.z) / 3;
                double t2z = (t2v1.z + t2v2.z + t2v3.z) / 3;
                return Double.compare(t1z, t2z);
            }
        });
    }
    
    private void draw(Graphics g){
        prepareModel();
        drawBackground(g);
        drawModel(g, modified);
        g.setColor(TEXT_COLOR);
        g.drawString("Vertex count: " + original.vertices.size(), 8, 16);
        g.drawString("Triangle count: " + original.triangles.size(), 8, 32);
    }
    
    private void drawBackground(Graphics g){
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private void drawModel(Graphics g, Model model){
        g.setColor(Color.black);
        for(Triangle triangle : model.triangles){
            drawTriangle(g, model, triangle);
        }
    }
    
    private int[] xs = new int[3];
    private int[] ys = new int[3];
    private void drawTriangle(Graphics g, Model model, Triangle triangle){
        Vertex v1 = model.vertices.get(triangle.v1);
        Vertex v2 = model.vertices.get(triangle.v2);
        Vertex v3 = model.vertices.get(triangle.v3);
        xs[0] = (int)v1.x;
        xs[1] = (int)v2.x;
        xs[2] = (int)v3.x;
        ys[0] = (int)v1.y;
        ys[1] = (int)v2.y;
        ys[2] = (int)v3.y;
        g.setColor(FILL_COLOR);
        g.fillPolygon(xs, ys, xs.length);
        g.setColor(EDGE_COLOR);
        g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
        g.drawLine((int)v1.x, (int)v1.y, (int)v3.x, (int)v3.y);
        g.drawLine((int)v3.x, (int)v3.y, (int)v2.x, (int)v2.y);
    }
    
    private void transform(Vertex v){
        rotateY(v, ry);
        rotateX(v, rx);
        scale(v, 200, -200, 200);
        move(v, getWidth() / 2, getHeight() / 2, 0);
    }
    
    private void move(Vertex v, double dx, double dy, double dz){
        double x = v.x;
        double y = v.y;
        double z = v.z;
        v.x = x + dx;
        v.y = y + dy;
        v.z = z + dz;
    }
    
    private void scale(Vertex v, double sx, double sy, double sz){
        double x = v.x;
        double y = v.y;
        double z = v.z;
        v.x = x * sx;
        v.y = y * sy;
        v.z = z * sz;
    }
    
    private void rotateX(Vertex v, double angle){
        double x = v.x;
        double y = v.y;
        double z = v.z;
        angle = angle * Math.PI / 180;
        v.x = x;
        v.y = y * Math.cos(angle) - z * Math.sin(angle);
        v.z = y * Math.sin(angle) + z * Math.cos(angle);
    }
    
    private void rotateY(Vertex v, double angle){
        double x = v.x;
        double y = v.y;
        double z = v.z;
        angle = angle * Math.PI / 180;
        v.x = x * Math.cos(angle) + z * Math.sin(angle);
        v.y = y;
        v.z = -1 * x * Math.sin(angle) + z * Math.cos(angle);
    }
    
    private void create(){
        original = IcoSphere.create((int)jSpinner.getValue());
        modified = IcoSphere.create((int)jSpinner.getValue());
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                draw(g);
            }
        };
        jPanel2 = new javax.swing.JPanel();
        jButton = new javax.swing.JButton();
        jSpinner = new javax.swing.JSpinner();
        jButtonSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ico sphere creator");
        setPreferredSize(new java.awt.Dimension(800, 600));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {1.0, 0.0};
        getContentPane().setLayout(layout);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 374;
        gridBagConstraints.ipady = 274;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        getContentPane().add(jPanel1, gridBagConstraints);

        java.awt.GridBagLayout jPanel2Layout = new java.awt.GridBagLayout();
        jPanel2Layout.columnWeights = new double[] {1.0, 0.0};
        jPanel2Layout.rowWeights = new double[] {1.0};
        jPanel2.setLayout(jPanel2Layout);

        jButton.setText("Create");
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        jPanel2.add(jButton, gridBagConstraints);

        jSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 7, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel2.add(jSpinner, gridBagConstraints);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel2.add(jButtonSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
        try {
            create();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try {
            JFileChooser fch = new JFileChooser();
            fch.addChoosableFileFilter(new FileNameExtensionFilter("Obj file", "obj"));
            fch.addChoosableFileFilter(new FileNameExtensionFilter("Glp file (custom)", "glp"));
            fch.showSaveDialog(this);
            File file = fch.getSelectedFile();
            if (file != null) {
                if (file.getAbsolutePath().endsWith(".glp")) {
                    original.saveGlp(new FileOutputStream(fch.getSelectedFile()));
                } else {
                    original.saveObj(new FileOutputStream(fch.getSelectedFile()));
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "" + e.getClass().getSimpleName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner;
    // End of variables declaration//GEN-END:variables
}
