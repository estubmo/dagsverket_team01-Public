/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.gui;

import dagsverket.database.Database;
import dagsverket.system.Car;
import dagsverket.system.Worker;

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
public class RegWorker extends JDialog implements ActionListener, TableModel, ListSelectionListener {

    private JTable table;
    private ArrayList<Worker> workers;
    private Database database;
    private JMenuItem deleteItem;
    private JPopupMenu popupMenu;

    public RegWorker(Database database, Frame owner){
        super(owner, "Registrer Arbeider", true);
        this.database = database;



        workers = database.getWorkers();

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

        knapp = new JButton("-");
        knapp.setFont(font);
        knapp.setMinimumSize(dimension);
        knapp.setMaximumSize(dimension);
        knapp.addActionListener(this);
        tb.add(knapp);



        tb.setFloatable(false);
       // tb.setRollover(false);


        add(tb, BorderLayout.SOUTH);

        add(scrollPane, SwingConstants.CENTER);

        pack();
        setLocationRelativeTo(owner);
    }

    public void addWorker(){
        database.insertWorker(new Worker(-1, "Fornavn", "Etternavn", true));
        workers = database.getWorkers();
        table.updateUI();
    }

    public void removeWorker(){
        int i = table.getSelectedRow();
        if (i  >= 0 && i < workers.size()){
            if (database.deleteWorker(workers.get(i))){
                workers.remove(i);
                table.updateUI();
            }

        }
    }


    public JTable getTable(){
        return table;
    }










   //<editor-fold>  //Listeners
    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equals("Slett arbeider") || ac.equals("-")){
            removeWorker();
        } else if (ac.equals("Arbeider") || ac.equals("+")){
            addWorker();
        }

    }

    @Override
    public int getRowCount() {
        return workers.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Fornavn";
            case 1:
                return "Etternavn";
            case 2:
                return "Kan arbeide ute?";
            default:
                return "";
        }
    }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0){
                return String.class;
            } else if (columnIndex == 1){
                return String.class ;
            } else if (columnIndex == 2){
                return Boolean.class;
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
        Worker w = workers.get(rowIndex);

        switch (columnIndex){
            case 0:
                return w.getFirstname();
            case 1:
                return new String(w.getSurname());
            case 2:
                return new Boolean(w.getCanWorkOutside());
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Worker w = workers.get(rowIndex);
        switch (columnIndex){
            case 0:
                w.setFirstname(((String) aValue));
                break;
            case 1:
                w.setSurname(((String) aValue));
                break;
            case 2:
                w.setCanWorkOutside(((Boolean) aValue));
                break;
            default:
        }
        database.updateWorker(w);
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
        new RegWorker(Database.DATABASE, null).setVisible(true);


    }
}
