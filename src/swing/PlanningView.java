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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import models.Connexion;
import models.LocalConfig;
import models.Observer;
import models.Observable;
import models.Planning;
import models.Tache;
import models.User;
import models.Utils;

/**
 *
 * @author aicha
 */
public class PlanningView extends javax.swing.JFrame implements Observer {

    private Connexion connect;
    private User user;
    private Planning taskList;
    private Object[][] project_values;
    private static final String[] header = {"Tache", "Status", "Avancement" , "Détails"};
    private final DefaultTableModel dataList;
    private int filtrer = -1;
    private HashSet<Integer> myTask;
    private final LocalConfig config = LocalConfig.getConfig();
    private JTextField pourcent;
    private int currentRow;

    public PlanningView(Connexion connect, int projet) throws IOException, ClassNotFoundException, SQLException, IllegalArgumentException, IllegalAccessException {
        myTask = new HashSet<>();
        this.connect = connect;
        if (this.connect == null) {
            this.connect = Connexion.makeConnexion();
        }

        this.user = config.user;

        this.taskList = new Planning(connect, projet);

        dataList = new DefaultTableModel(project_values, header){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return column == 2;
            }
        };

        initComponents();
        setLocation(0, 0);
        initUserList();

        if (config.fullFrame) {
            setExtendedState(MAXIMIZED_BOTH);
        }

        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                config.fullFrame = e.getNewState() == Frame.MAXIMIZED_BOTH;
                config.saveConfig();
            }
        });
        
        pourcent = new JTextField();
        initPlanningListe();
        
    }

    private void initPlanningListe() {

        /* Initialisation de la liste des projets */
        planning.setRowHeight(30);
        planning.getTableHeader().setPreferredSize(new Dimension(90, 30));
        planning.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int rn = target.getSelectedRow();
                int cn = target.getSelectedColumn();

                //System.out.println("rn : " + rn + " / cn : " + cn);
                if (cn == 3) {
                    int[] tab = (int[]) dataList.getValueAt(rn, cn);
                    viewTask(tab[0], rn);
                }
                else if (cn == 2) {
                    System.out.println("editing pourcentage");
                    System.out.println(pourcent.getText());
                    currentRow = rn;
                }
            }
        });

        TableColumn column;
        for (int i = 0; i < header.length; i++) {
            column = planning.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(601);
            }
            else {
                column.setPreferredWidth(90);
                column.setMaxWidth(90);
            }
        }

        /*planning.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
         Object[] tab = (Object[]) arg1;
         JLabel stat = new JLabel("<html><head></head><body>" + tab[0].toString() + "</body></html>");
         stat.setHorizontalAlignment(SwingConstants.CENTER);
         stat.setOpaque(true);
         stat.setBackground(config.usercolors.get((Integer) tab[1]));
         return stat;
         }
         });*/
        planning.getColumnModel().getColumn(0).setCellRenderer(new MultilineTableCellRenderer());
        planning.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(pourcent));
        if(planning.getColumnModel().getColumn(2).getCellEditor().stopCellEditing()){
            int[] tab = (int[]) dataList.getValueAt(currentRow, 3);
        }

        planning.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                int[] tab = (int[]) arg1;
                JLabel stat = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/img/" + ((tab[0] == 0) ? "non_" : "") + "vue.png"))); // NOI18N
                stat.setHorizontalAlignment(SwingConstants.CENTER);
                stat.setToolTipText("Tache " + ((tab[0] == 0) ? "non " : "") + "terminée");
                stat.setCursor(new Cursor(Cursor.HAND_CURSOR));
                stat.setOpaque(true);
                stat.setBackground(config.usercolors.get(tab[2]));

                return stat;
            }
        });
        
        /*planning.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                int av = (int) arg1;
                JTextField det = new JTextField("" + av); // NOI18N
                det.setHorizontalAlignment(SwingConstants.CENTER);
                //det.setOpaque(true);
                //det.setBackground(config.usercolors.get());
                //det.setEditable(true);
                return det;
            }
        });*/

        planning.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
                int[] tab = (int[]) arg1;
                JLabel det = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/img/view.png"))); // NOI18N
                det.setHorizontalAlignment(SwingConstants.CENTER);
                det.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                det.setOpaque(true);
                det.setBackground(config.usercolors.get(tab[1]));
                return det;
            }
        });

        loadPlanning();
    }

    public void viewTask(int id, int rn) {
        System.out.println("details du projet line " + rn + " : " + id);

        try {
            TaskDetailsView dt = new TaskDetailsView(connect, id, false, taskList.projet.getId(), taskList.projet.getDesignation(), rn);
            dt.setObserver(this);
            dt.setVisible(true);
            //this.dispose();
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PlanningView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtrerList() {

    }

    /*public void checkTask(int id) {
        try {
            Tache t = new Tache(id);
            t.find(connect);
            if (!t.isV_user()) {
                t.setV_user(true);
                t.save(connect);
            }
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PlanningView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    private void loadPlanning() {
        try {
            int n = 0;
            int[] tab = new int[config.userview.size()];
            for (Integer i : config.userview.keySet()) {
                if (config.userview.get(i)) {
                    tab[n++] = i;
                }
            }

            HashMap<Integer, Tache> liste = Tache.getForUser(connect, taskList.projet.getId(), tab);
            project_values = new Object[liste.size()][];
            JLabel stat, det, pl;
            Object[] obj;

            while (dataList.getRowCount() > 0) {
                dataList.removeRow(0);
            }

            //System.out.println("size = " + dataList.getRowCount());
            for (Integer i : liste.keySet()) {
                Tache t = liste.get(i);

                if (t.getUser() == this.user.getId()) {
                    //System.out.println(i);
                    myTask.add(i);
                }

                obj = new Object[4];
                obj[0] = new Object[]{t.getDescription(), config.usercolors.get(t.getUser())};
                obj[1] = new int[]{((t.isValide()) ? 1 : 0), i, t.getUser()};
                obj[2] = t.getAvancement();//new int[]{i, t.getAvancement(),t.getUser()};
                obj[3] = new int[]{i, t.getUser()};

                dataList.addRow(obj);
                //dataList.set
                //n++;
            }

            //dataList.setDataVector(project_values, projects_list.get);
            dataList.fireTableDataChanged();
        }
        catch (ClassNotFoundException | IOException | SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProjectsListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initUserList() {
        JCheckBoxMenuItem item;
        for (int i : config.usernames.keySet()) {
            item = new JCheckBoxMenuItem("Taches " + config.usernames.get(i));
            if (config.userview.get(i)) {
                item.setBackground(config.usercolors.get(i));
            }
            item.setSelected(config.userview.get(i));
            item.setPreferredSize(new java.awt.Dimension(139, 31));
            item.addActionListener(new UserItemListener(i));

            jMenu2.add(item);
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
        planning = new javax.swing.JTable();
        add_project = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        username.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        username.setText(this.user.getPrenom() + " " + this.user.getNom());
        username.setToolTipText("");

        planning.setModel(dataList);
        planning.setToolTipText("");
        planning.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        planning.getTableHeader().setReorderingAllowed(false);
        list_container.setViewportView(planning);

        add_project.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        add_project.setText("Ajouter");
        add_project.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_projectActionPerformed(evt);
            }
        });

        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        btn_back.setText("Liste des projets");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(list_container, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btn_back)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(add_project))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add_project)
                    .addComponent(btn_back))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(list_container, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(8, 6, 6));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(taskList.projet.getDesignation());

        jMenu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMenu1.setText("Menu");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        jMenuItem1.setText("Editer le profil");
        jMenu1.add(jMenuItem1);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/xls.png"))); // NOI18N
        jMenuItem3.setText("Exporter tableau excel");
        jMenu1.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pdf.png"))); // NOI18N
        jMenuItem4.setText("Exporter en PDF");
        jMenu1.add(jMenuItem4);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jMenuItem2.setText("Déconnexion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2logoutActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText(" ");
        jMenu3.setEnabled(false);
        jMenu3.setFocusable(false);
        jMenuBar1.add(jMenu3);

        jMenu2.setText("Affichage");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void add_projectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_projectActionPerformed
        try {
            TaskDetailsView dt = new TaskDetailsView(connect, 0, true, taskList.projet.getId(), taskList.projet.getDesignation(), 0);
            dt.setObserver(this);
            dt.setVisible(true);
        }
        catch (IOException | ClassNotFoundException | SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PlanningView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_add_projectActionPerformed

    private void jMenuItem2logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2logoutActionPerformed
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
    }//GEN-LAST:event_jMenuItem2logoutActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        try {
            new ProjectsListView(connect, Utils.parserDate("" + taskList.projet.getDate())).setVisible(true);
            this.dispose();

        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PlanningView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_backActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_project;
    private javax.swing.JButton btn_back;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane list_container;
    private javax.swing.JTable planning;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable arg0, Object arg1) {
        if (Utils.isInteger(arg1.toString())) {
            this.dispose();
        }
        else {
            TaskDetailsView dt = (TaskDetailsView) arg0;
            Tache t = dt.task;
            boolean add = (Boolean) arg1;
            Object[] obj = new Object[4];
            obj[0] = new Object[]{t.getDescription(), config.usercolors.get(t.getUser())};
            obj[1] = new int[]{((t.isValide()) ? 1 : 0), t.getId(), t.getUser()};
            obj[2] = t.getAvancement();//new int[]{t.getId(), t.getAvancement(),t.getUser()};
            obj[3] = new int[]{t.getId(), t.getUser()};

            if (add) {
                dataList.addRow(obj);
                System.out.println(dataList.getRowCount());
                dataList.fireTableRowsUpdated(dataList.getRowCount() - 1, dataList.getRowCount() - 1);
                //dataList.fireTableRowsInserted(dataList.getRowCount() - 1, dataList.getRowCount() - 1);
            }
            else {
                dataList.setValueAt(obj[0], dt.line, 0);
                dataList.setValueAt(obj[1], dt.line, 1);
                dataList.setValueAt(obj[2], dt.line, 2);
                dataList.setValueAt(obj[3], dt.line, 3);
                dataList.fireTableRowsUpdated(dt.line, dt.line);
            }
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class UserItemListener implements ActionListener {

        private final int user;

        public UserItemListener(int u) {
            this.user = u;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
            if (item.isSelected()) {
                item.setOpaque(true);
                item.setBackground(config.usercolors.get(user));
            }
            else {
                item.setOpaque(false);
            }
            config.userview.put(user, item.isSelected());
            config.saveConfig();
            loadPlanning();
        }
    }
}
