package dagsverket.gui;

import dagsverket.database.Database;
import dagsverket.system.Product;

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
public class ProductPanel extends JDialog implements ActionListener, TableModel, ListSelectionListener {

    private JTable table;
    private ArrayList<Product> products;
    private Database database;
    private JMenuItem deleteItem;
    private JPopupMenu popupMenu;





    public ProductPanel(Database database, Frame owner){
        super(owner, "Registrer Produkt",true);
        this.database = database;



        products = database.getProducts();

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

    public void addProduct(){

        database.insertProduct(new Product(-1, "Ny vare", 0, 0));
        products = database.getProducts();
        table.updateUI();
    }

    public void removeProduct(){
        int i = table.getSelectedRow();
        if (i  >= 0 && i < products.size()){
            if (database.deleteProduct(products.get(i))){
                products.remove(i);
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
        if (ac.equals("Slett vare") || ac.equals("-")){
            removeProduct();
        } else if (ac.equals("Legg til vare") || ac.equals("+")){
            addProduct();
        }

    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Navn";
            case 1:
                return "Pris";
            case 2:
                return "Antall";
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
                return Double.class;
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
        Product p = products.get(rowIndex);

        switch (columnIndex){
            case 0:
                return p.getProductName();
            case 1:
                return new Double(p.getCost());
            case 2:
                return new Integer(p.getStock());
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Product p = products.get(rowIndex);

        switch (columnIndex){
            case 0:
                p.setProductName(((String) aValue));
                break;
            case 1:
                p.setCost(((Double)aValue).doubleValue());
                break;
            case 2:
                p.setStock(((Integer)aValue).intValue());
                break;
            default:
        }


        database.updateProduct(p);

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
        new ProductPanel(Database.DATABASE,null).setVisible(true);

    }
}
