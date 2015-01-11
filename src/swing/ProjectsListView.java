/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import models.Connexion;
import models.LocalConfig;
import models.Observable;
import models.Observer;
import models.Projet;
import models.User;
import models.Utils;

/**
 *
 * @author aicha
 */
public class ProjectsListView extends javax.swing.JFrame implements Observer{
    
    private Connexion connect;
    private User user;
    private Object[][] project_values;
    private static final String[] header = {"Intitulé", "Status", "Détails", "Planning"};
    private final DefaultTableModel dataList;
    private Observable obs;
    
    public ProjectsListView(Connexion connect) throws IOException, ClassNotFoundException, SQLException {
        
        this.connect = connect;
        if (this.connect == null) {
            this.connect = Connexion.makeConnexion();
        }
        
        this.user = LocalConfig.getConfig().user;
        
        dataList = new DefaultTableModel(project_values, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        
        
        initComponents();
        setLocation(0, 0);
        if (LocalConfig.getConfig().fullFrame) {
            setExtendedState(MAXIMIZED_BOTH);
        }
        
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                try {
                    LocalConfig conf = LocalConfig.getConfig();
                    conf.fullFrame = e.getNewState() == Frame.MAXIMIZED_BOTH;
                    conf.saveConfig();
                }
                catch (IOException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(PlanningView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        initProjetsListe();
        
    }
    
    public ProjectsListView(Connexion connect,int[] periode) throws IOException, ClassNotFoundException, SQLException {
        this(connect);
        //System.out.println("[" + periode[0] + "," + periode[1] + "]");
        annee_combo.setSelectedItem(periode[0]);
        mois_combo.setSelectedIndex(periode[1]);
        loadProjects();
    }
    
    private void initProjetsListe() {

        /* Initialisation de la liste des projets */
        projects_list.setRowHeight(30);        
        projects_list.getTableHeader().setPreferredSize(new Dimension(90, 30));
        
        projects_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int rn = target.getSelectedRow();
                int cn = target.getSelectedColumn();
                
                if (cn == 3) {
                    openPlanning((Integer) dataList.getValueAt(rn, cn));
                }
                else if (cn == 2) {
                    viewProject((Integer) dataList.getValueAt(rn, cn));
                }
            }
        });
        
        TableColumn column;
        for (int i = 0; i < header.length; i++) {
            column = projects_list.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(571); //titre column is bigger
            }
            else {
                column.setPreferredWidth(70);
                column.setMaxWidth(70);
            }
        }
        
        projects_list.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                JLabel stat = new JLabel(arg1.toString());
                stat.setHorizontalAlignment(SwingConstants.CENTER);
                return stat;
            }
        });
        
        projects_list.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                JLabel stat = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/img/state" + arg1 + ".png"))); // NOI18N
                stat.setHorizontalAlignment(SwingConstants.CENTER);
                stat.setToolTipText(Projet.getStateString((Integer) arg1));
                return stat;
            }
        });
        
        projects_list.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                JLabel det = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/img/view.png"))); // NOI18N
                det.setHorizontalAlignment(SwingConstants.CENTER);
                det.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                return det;
            }
        });
        
        projects_list.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, final Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                JLabel pl = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/img/pl.png"))); // NOI18N
                pl.setHorizontalAlignment(SwingConstants.CENTER);
                pl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                return pl;
            }
        });
        
        loadProjects();
    }
    
    public void openPlanning(int id) {
        //System.out.println("openning planning : " + id);
        try {
            
            new PlanningView(connect,id).setVisible(true);
            //System.out.println(full);
            this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void viewProject(int id) {
        //System.out.println("details du projet : " + id);
        try {
            
            new ProjetDetailsView(connect, user, id).setVisible(true);
            this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadProjects() {
        try {
            int a = (Integer) annee_combo.getSelectedItem();
            int m = mois_combo.getSelectedIndex();
            int n = 0;
            int[] tab = Utils.getInterval(a, m);
            HashMap<Integer, Projet> liste = Projet.extractForPeriode(tab[0], tab[1], connect);
            project_values = new Object[liste.size()][];
            JLabel stat, det, pl;
            Object[] obj;
            
            while (dataList.getRowCount() > 0) {
                dataList.removeRow(0);
            }

            //System.out.println("size = " + dataList.getRowCount());
            for (Integer i : liste.keySet()) {
                Projet p = liste.get(i);
                
                obj = new Object[4];
                obj[0] = p.getDesignation();
                obj[1] = p.getStateInt();
                obj[2] = i;
                obj[3] = i;
                
                dataList.addRow(obj);
                //dataList.set
                n++;
            }

            //dataList.setDataVector(project_values, projects_list.get);
            dataList.fireTableDataChanged();
        }
        catch (ClassNotFoundException | IOException | SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
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
        list_container = new javax.swing.JScrollPane();
        projects_list = new javax.swing.JTable();
        mois_combo = new javax.swing.JComboBox();
        annee_combo = new javax.swing.JComboBox();
        add_project = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        username.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        username.setForeground(java.awt.Color.black);
        username.setText(this.user.getPrenom() + " " + this.user.getNom());
        username.setToolTipText("");

        projects_list.setModel(dataList);
        projects_list.setToolTipText("");
        projects_list.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        projects_list.getTableHeader().setReorderingAllowed(false);
        list_container.setViewportView(projects_list);

        mois_combo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre" }));
        mois_combo.setSelectedIndex(Utils.getCurrentMonth());

        annee_combo.setModel(new javax.swing.DefaultComboBoxModel(new Integer[] { 2013, 2014, 2015 }));
        annee_combo.setSelectedItem(Utils.getCurrentYear());

        add_project.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        add_project.setText("Ajouter");
        add_project.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_projectActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lister.png"))); // NOI18N
        jButton1.setText("Lister");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(list_container, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(mois_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(annee_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(add_project))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mois_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(annee_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add_project)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(list_container, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
        );

        jMenu1.setText("Menu");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        jMenuItem1.setText("Editer le profil");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jMenuItem2.setText("Déconnexion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
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

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        try {
            user.reset();
            LocalConfig conf = LocalConfig.getConfig();
            conf.user = user;
            conf.saveConfig();
            new ConnexionView(connect).setVisible(true);
            this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_logoutActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadProjects();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void add_projectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_projectActionPerformed
        try {
            System.out.println("Ajout d'un projet");
            if(this.obs == null){
                System.out.println("ouverture fenetre");
                ProjetAddView dt = new ProjetAddView(connect, 0);
                this.obs = dt;
                dt.setObserver(this);
                dt.setVisible(true);
            }
        }
        catch (IOException | ClassNotFoundException | SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_add_projectActionPerformed

    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            new ProfilView(connect).setVisible(true);
            this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_project;
    private javax.swing.JComboBox annee_combo;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane list_container;
    private javax.swing.JComboBox mois_combo;
    private javax.swing.JTable projects_list;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable obs, Object o) {
        //TODO mise a jour de la liste des projets
    }

    @Override
    public void reset() {
        this.obs = null;
    }
}
