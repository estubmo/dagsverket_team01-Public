package dagsverket.gui;

import dagsverket.database.Database;
import dagsverket.system.Car;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by ArneChristian on 28.03.14.
 */
public class RegCar extends JDialog implements ActionListener, TableModel, ListSelectionListener {

    private JTable table;
    private ArrayList<Car> cars;
    private Database database;
    private JMenuItem deleteItem;
    private JPopupMenu popupMenu;





    public RegCar(Database database, Frame owner){
        super(owner, "Registrer Bil", true);
        this.database = database;



        cars = database.getCars();

        setLayout(new BorderLayout());




        table = new JTable(this);
        //table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
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

    public void addCar(){

        database.insertCar(new Car(-1, "Skilt", "Farge", 0));
        cars = database.getCars();
        table.updateUI();
    }

    public void removeCar(){
        int i = table.getSelectedRow();
        if (i  >= 0 && i < cars.size()){
            if (database.deleteCar(cars.get(i))){
                cars.remove(i);
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
        if (ac.equals("Slett bil") || ac.equals("-")){
            removeCar();
        } else if (ac.equals("Bil") || ac.equals("+")){
            addCar();
        }
    }

    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Skilt";
            case 1:
                return "Farge";
            case 2:
                return "Antall seter";
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
                return Integer.class;
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
        Car c = cars.get(rowIndex);

        switch (columnIndex){
            case 0:
                return c.getPlateNumber();
            case 1:
                return new String(c.getColour());
            case 2:
                return new Integer(c.getSeats());
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Car c = cars.get(rowIndex);

        switch (columnIndex){
            case 0:
                c.setPlateNumber(((String) aValue));
                break;
            case 1:
                c.setColour(((String) aValue));
                break;
            case 2:
                c.setSeats(((Integer)aValue).intValue());
                break;
            default:
        }


        database.updateCar(c);

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
        new RegCar(Database.DATABASE,null).setVisible(true);


    }
}
