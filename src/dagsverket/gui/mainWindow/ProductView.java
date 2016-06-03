package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.ProductPanel;
import dagsverket.system.Product;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by ArneChristian on 29.03.14.
 */public class ProductView extends JPanel implements ActionListener{

    private Database database;
    private ProductPanel productPanel;
    private JPopupMenu popupMenu;
    private JMenuItem deleteItem;

    public ProductView(Database database){
        this.database = database;
      //  productPanel = new ProductPanel(database);

        createPopupMenu();

        ///////
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Products", JLabel.CENTER);
        add(label, BorderLayout.NORTH);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        ///////

        add(productPanel, BorderLayout.CENTER);
    }



    private void createPopupMenu(){
        ///POPUPMENU
        popupMenu = new JPopupMenu();

        deleteItem = new JMenuItem("Slett vare");
        deleteItem.addActionListener(this);
        popupMenu.add(deleteItem);

        JMenuItem item = new JMenuItem("Legg til vare");
        item.addActionListener(this);
        popupMenu.add(item);

        popupMenu.addSeparator();

        item = new JMenuItem("Oppdater");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_R);
        popupMenu.add(item);

        popupMenu.addSeparator();

        item = new JMenuItem("Neste måned");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_RIGHT);
        popupMenu.add(item);

        item = new JMenuItem("Forrige måned");
        item.addActionListener(this);
        popupMenu.add(item);
        //table.setComponentPopupMenu(popupMenu);
        productPanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) rightClick(e);
            }

        });
    }

    private void rightClick(MouseEvent e){
        JTable table = productPanel.getTable();
        int r = table.rowAtPoint(e.getPoint());
        if (e.getComponent() instanceof JTable && r >= 0 && r < table.getRowCount()) {
            deleteItem.setEnabled(true);
            table.setRowSelectionInterval(r, r);

        } else {
            table.clearSelection();
            deleteItem.setEnabled(false);
        }
        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();

        if (ac.equals("Legg til vare")){
            productPanel.addProduct();
        } else if (ac.equals("Slett vare")){
            productPanel.removeProduct();
        }
    }
}
