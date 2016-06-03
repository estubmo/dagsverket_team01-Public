/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.gui;

/**
 *
 * @author Kai
 */

import dagsverket.database.Database;
import dagsverket.system.Employer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Created by ArneChristian on 28.03.14.
 */
public class REDKunde extends JDialog implements ActionListener, TableModel, ListSelectionListener {

    private JTable table;
    private ArrayList<Employer> emps;
    private Database database;
    private JMenuItem deleteItem;
    private JPopupMenu popupMenu;
    private JButton editKnapp, deleteKnapp;

    public REDKunde(Database database, Frame owner){
        super(owner, "Registrer Kunde", true);
        this.database = database;



        emps = database.getEmployers();

        setLayout(new BorderLayout());




        table = new JTable(this);
        //table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFocusable(false);






        //font size
        Font font = new Font("Lucinda Grande", Font.PLAIN, 14);
        table.setFont(font);
        table.setRowHeight(18);

        Dimension dimension = new Dimension(40, 40);


        JScrollPane scrollPane = new JScrollPane(table);

        JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
        JButton knapp = new JButton("+");
        knapp.setFont(font);
        knapp.setMinimumSize(dimension);
        knapp.setMaximumSize(dimension);
        knapp.addActionListener(this);
        tb.add(knapp);

        deleteKnapp = new JButton("-");
        deleteKnapp.setFont(font);
        deleteKnapp.setMinimumSize(dimension);
        deleteKnapp.setMaximumSize(dimension);
        deleteKnapp.addActionListener(this);
        tb.add(deleteKnapp);

        editKnapp = new JButton("rediger");
        editKnapp.setFont(font);
        editKnapp.setMinimumSize(dimension);
        //editKnapp.setMaximumSize(dimension);
        editKnapp.addActionListener(this);
        tb.add(editKnapp);


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < getRowCount()){
                    editKnapp.setEnabled(true);
                    deleteKnapp.setEnabled(true);
                } else {
                    editKnapp.setEnabled(false);
                    deleteKnapp.setEnabled(false);
                }
            }
        });


        tb.setFloatable(false);
       // tb.setRollover(false);


        add(tb, BorderLayout.SOUTH);

        add(scrollPane, SwingConstants.CENTER);

        pack();
        setLocationRelativeTo(owner);
    }

   public void addKunde(){
       new RegKunde(database,null, this).setVisible(true);
       emps = database.getEmployers();
       table.updateUI();
      /*  database.insertEmployer2(new Employer(-1, "Bedriftsnavn"));
        emps = database.getEmployers();
        table.updateUI();*/
    }

   public void removeKunde(){
        int i = table.getSelectedRow();
        if (i  >= 0 && i < emps.size()){
            if (database.deleteEmployer(emps.get(i))){
                emps.remove(i);
                table.updateUI();
            }

        }
    }

    public void editKunde(){
        Employer emp = emps.get(table.convertRowIndexToModel(table.getSelectedRow()));

        new RegKunde(database,emp, this).setVisible(true);
        emps = database.getEmployers();
        table.updateUI();
    }


    public JTable getTable(){
        return table;
    }










   //<editor-fold>  //Listeners
    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equals("Slett arbeider") || ac.equals("-")){
            removeKunde();
        } else if (ac.equals("Kunde") || ac.equals("+")){
            addKunde();
        } else if (ac.equals("rediger")){
            editKunde();
        }

    }

    @Override
    public int getRowCount() {
        return emps.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Bedrift navn";
            default:
                return "";
        }
    }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0){
                return String.class;
            } else{
                return null;
            }
        }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employer emp = emps.get(rowIndex);

        switch (columnIndex){
            case 0:
                return emp.getEmpName();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Employer emp = emps.get(rowIndex);
        switch (columnIndex){
            case 0:
                emp.setEmpName(((String) aValue));
                break;
            default:
        }
        database.updateEmployer(emp);
    }


    

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    //</editor-fold>   ///


    public static void main(String[] args) {
        new REDKunde(Database.DATABASE, null).setVisible(true);


    }
}

