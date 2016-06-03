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
import dagsverket.system.Supervisor;
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
public class RegSupervisor extends JDialog implements ActionListener, TableModel, ListSelectionListener {

    private JTable table;
    private ArrayList<Supervisor> supervisors;
    private Database database;
    private JMenuItem deleteItem;
    private JPopupMenu popupMenu;





    public RegSupervisor(Database database, Frame owner){
        super(owner,"Registrer Arbeidsleder", true);
        this.database = database;



        supervisors = database.getSupervisors();

        setLayout(new BorderLayout());




        table = new JTable(this);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
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

    public void addSupervisor(){

        database.insertSupervisor(new Supervisor(-1, "Fornavn", "Etternavn", "Tlf","Epost"));
        supervisors = database.getSupervisors();
        table.updateUI();
    }

    public void removeSupervisor(){
        int i = table.getSelectedRow();
        if (i  >= 0 && i < supervisors.size()){
            if (database.deleteSupervisor(supervisors.get(i))){
                supervisors.remove(i);
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
        if (ac.equals("Slett arbeidsleder") || ac.equals("-")){
            removeSupervisor();
        } else if (ac.equals("Arbeidsleder") || ac.equals("+")){
            addSupervisor();
        }

    }

    @Override
    public int getRowCount() {
        return supervisors.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Fornavn";
            case 1:
                return "Etternavn";
            case 2:
                return "Tlf";
            case 3:
                return "Epost";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supervisor sup = supervisors.get(rowIndex);

        switch (columnIndex){
            case 0:
                return sup.getFirstname();
            case 1:
                return sup.getSurname();
            case 2:
                return sup.getPhoneNumber();
            case 3:
                return sup.getEmail();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Supervisor sup = supervisors.get(rowIndex);

        switch (columnIndex){
            case 0:
                sup.setFirstname(((String) aValue));
                break;
            case 1:
                sup.setSurname(((String) aValue));
                break;
            case 2:
                sup.setPhoneNumber(((String)aValue));
                break;
            case 3:
                sup.setEmail(((String)aValue));
            default:
        }


        database.updateSupervisor(sup);

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
        new RegSupervisor(Database.DATABASE,null).setVisible(true);


    }
}