/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Connexion;
import models.LocalConfig;
import models.Observable;
import models.Observer;
import models.Tache;
import models.User;
import models.Utils;

/**
 *
 * @author aicha
 */
public class TaskDetailsView extends javax.swing.JFrame implements Observable {

    private final User user;
    private Connexion connect;
    private boolean edit;
    public Tache task;
    private int projectId;
    private String projectName;
    private final LocalConfig config = LocalConfig.getConfig();
    private Observer obs;
    public int line;

    public TaskDetailsView(Connexion c, int t, boolean a, int p, String n, int pos) throws IOException, ClassNotFoundException, SQLException, IllegalArgumentException, IllegalAccessException {

        this.connect = c;
        if (this.connect == null) {
            this.connect = Connexion.makeConnexion();
        }
        this.projectId = p;
        this.projectName = n;
        this.edit = a;
        this.line = pos;
        task = new Tache(t);
        task.find(connect);
        this.user = config.user;

        initComponents();
        if (a) {
            add_project.setText("Valider");
            desc.setContentType("text/plain");
            desc.setText(task.getDescription());
            comment.setContentType("text/plain");
            comment.setText(task.getCommentaires());
        }
        jLabel7.setVisible(false);
        jLabel10.setVisible(true);
    }

    public void setObserver(Observer o) {
        this.obs = o;
    }

    private void reset(boolean add) {
        try {
            if (add) {
                task = new Tache(0);
            }
            comment.setText(task.getCommentaires());
            desc.setText(task.getDescription());
            duree_p.setText("" + task.getDuree_prevue());
            duree_r.setText("" + task.getDuree_reel());
        }
        catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(TaskDetailsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        username = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        add_project = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        duree_p = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        comment = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        duree_r = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        username.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        username.setText(this.user.getPrenom() + " " + this.user.getNom());
        username.setToolTipText("");

        add_project.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        add_project.setText("Modifier");
        add_project.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_projectActionPerformed(evt);
            }
        });

        jLabel1.setText("Projet");

        jLabel2.setText(projectName);

        jLabel3.setText("Tache");

        jLabel4.setText("Durée prévue");

        jLabel5.setText("heures");

        jLabel6.setText("Commentaires");

        duree_p.setEditable(edit);
        duree_p.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        duree_p.setText("" + task.getDuree_prevue());

        jLabel7.setForeground(java.awt.Color.red);
        jLabel7.setText("Veuillez entrer un numbre entier ou à virgule");

        comment.setEditable(edit);
        comment.setContentType("text/html"); // NOI18N
        comment.setText(task.getCommentaires());
        jScrollPane2.setViewportView(comment);

        desc.setEditable(edit);
        desc.setContentType("text/html"); // NOI18N
        desc.setText(task.getDescription());
        jScrollPane3.setViewportView(desc);

        jLabel8.setText("Durée réelle");
        jLabel8.setToolTipText("");

        duree_r.setEditable(edit);
        duree_r.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        duree_r.setText("" + task.getDuree_reel());

        jLabel9.setText("heures");

        jLabel10.setForeground(java.awt.Color.red);
        jLabel10.setText("Veuillez entrer un numbre entier ou à virgule");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(duree_r, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(duree_p, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3))
                .addGap(42, 42, 42))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(duree_p, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(duree_r, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                .addGap(61, 61, 61))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(add_project))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(add_project)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Menu");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        jMenuItem1.setText("Editer le profil");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jMenuItem2.setText("Déconnexion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2logoutActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2logoutActionPerformed
        try {
            user.reset();
            LocalConfig conf = LocalConfig.getConfig();
            conf.user = user;
            conf.saveConfig();
            new ConnexionView(connect).setVisible(true);
            obs.update(this, 0);
            this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2logoutActionPerformed

    private void add_projectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_projectActionPerformed
        try {
            if (!edit) {
                edit = true;
                comment.setEditable(edit);
                comment.setContentType("text/plain");
                comment.setText(task.getCommentaires());
                desc.setEditable(edit);
                desc.setContentType("text/plain");
                desc.setText(task.getDescription());
                duree_p.setEditable(edit);
                add_project.setText("Valider");
            }
            else if (task.getId() == 0) {
                if (!desc.getText().isEmpty() && Utils.isFloat(duree_p.getText().replace(',', '.'))) {
                    updateTask(true);
                    reset(true);
                }
                else if (Utils.isFloat(duree_p.getText())) {
                    jLabel7.setVisible(true);
                }
                else if (Utils.isFloat(duree_r.getText())) {
                    jLabel10.setVisible(true);
                }
            }
            else {
                if (!desc.getText().isEmpty() && !desc.getText().equals(task.getDescription()) && Utils.isFloat(duree_p.getText().replace(',', '.'))) {
                    updateTask(false);
                }
                else {
                    reset(false);
                }
                edit = false;
                comment.setEditable(edit);
                comment.setContentType("text/html");
                comment.setText(task.getCommentaires());
                desc.setEditable(edit);
                desc.setContentType("text/html");
                desc.setText(task.getDescription());
                duree_p.setEditable(edit);
                add_project.setText("Modifier");
            }
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(TaskDetailsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_add_projectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_project;
    private javax.swing.JTextPane comment;
    private javax.swing.JTextPane desc;
    private javax.swing.JTextField duree_p;
    private javax.swing.JTextField duree_r;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

    private void updateTask(boolean add) {
        try {
            task.setDescription(desc.getText());
            task.setCommentaires(comment.getText());
            task.setDuree_prevue(Float.parseFloat(duree_p.getText().replace(',', '.')));
            task.setDuree_reel(Float.parseFloat(duree_r.getText().replace(',', '.')));
            task.setProjet(projectId);
            task.setUser(user.getId());
            task.save(connect);
            obs.update(this, add);
        }
        catch (IllegalAccessException | IllegalArgumentException | SQLException ex) {
            Logger.getLogger(TaskDetailsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
