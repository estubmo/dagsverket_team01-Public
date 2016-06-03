package dagsverket.gui.mainWindow;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.toedter.calendar.JDateChooserCellEditor;
import dagsverket.database.Database;
import dagsverket.system.ContactInfo;
import dagsverket.system.Supervisor;
import dagsverket.system.Task;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by ArneChristian on 02.04.14.
 */
public class TaskOverview extends JPanel {


    private JPanel panel1;
    private JTable contactTable;
    private JPanel infoField;
    private TaskInfoPanel taskInfoPanel;


    private Database database;
    private TaskView taskView;
    private Task task;

    private ArrayList<Supervisor> supervisors = new ArrayList<Supervisor>();


    private JMenuItem addContact, deleteContact;


    public TaskOverview(Database database, TaskView taskView) {
        this.database = database;
        this.taskView = taskView;
        setMinimumSize(new Dimension(320, 500));
        setMaximumSize(new Dimension(-1, -1));
        setPreferredSize(new Dimension(325, 500));
        setInheritsPopupMenu(true);
        $$$setupUI$$$();


        taskInfoPanel = new TaskInfoPanel(database, taskView);
        infoField.add(taskInfoPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel1, BorderLayout.CENTER);


    }

    public void oppdater() {
        taskInfoPanel.update();
    }

    public void setTask(Task task) {

        taskInfoPanel.setTask(task);

        if (contactTable.isEditing()) {
            contactTable.getCellEditor().stopCellEditing();
        }
        contactTable.clearSelection();

        this.task = task;

        if (task != null) {
            contactTable.setEnabled(true);
        } else {
            contactTable.setEnabled(false);
        }


        contactTable.setPreferredSize(new Dimension(300, contactTable.getRowCount() * contactTable.getRowHeight()));
        contactTable.updateUI();


    }


    private void createUIComponents() {
        /***************TEST*DATA*******************
         Calendar regCal = GregorianCalendar.getInstance();

         String dato = String.format("%04d%02d%02d", regCal.get(Calendar.YEAR), regCal.get(Calendar.MONTH) + 1, 1);
         String regdato = String.format("%04d%02d%02d", regCal.get(Calendar.YEAR), regCal.get(Calendar.MONTH) + 1, regCal.get(Calendar.DATE));
         Task t = new Task(1, "Nytt Oppdrag", new Employer(4, "EMPLOYER"), "Address", "Post", "0000", true, "Description", "Befaring", 100, dato, regdato, false);
         t.setUsername(Login.username);

         task = t;
         /***********************TEST*DATA*END**************/
        // infoTable = new JTable(new InfoTableModel(t));
        contactTable = new JTable(new contactTableModel());
        contactTable.setGridColor(Color.BLACK);
        contactTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);


        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) rightClick(e);

            }
        };

        addMouseListener(mouseAdapter);
        //contactTable.getParent().addMouseListener(mouseAdapter);
        contactTable.addMouseListener(mouseAdapter);
        contactTable.getTableHeader().addMouseListener(mouseAdapter);
        contactTable.getTableHeader().setReorderingAllowed(false);
        contactTable.setEnabled(false);


    }

    private void rightClick(MouseEvent e) {
        int r = contactTable.rowAtPoint(e.getPoint());
        if (contactTable.getSelectedRowCount() == 1) {


            if (e.getComponent() instanceof JTable && r >= 0 && r < contactTable.getRowCount()) {

                contactTable.setRowSelectionInterval(r, r);

            } else {
                contactTable.clearSelection();

            }
        } else {
            int[] rows = contactTable.getSelectedRows();
            boolean clickOnSelected = false;
            for (int row : rows) {
                if (row == r) {
                    clickOnSelected = true;
                }
            }
            if (!clickOnSelected && e.getComponent() instanceof JTable && r >= 0 && r < contactTable.getRowCount()) {

                contactTable.setRowSelectionInterval(r, r);

            } else {
                contactTable.clearSelection();

            }


        }
        taskView.getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());

    }

    public void update() {
        supervisors = database.getSupervisors();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setFocusable(true);
        panel1.setInheritsPopupMenu(true);
        panel1.setPreferredSize(new Dimension(-1, -1));
        panel1.setRequestFocusEnabled(true);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setFocusable(true);
        panel2.setInheritsPopupMenu(true);
        panel2.setRequestFocusEnabled(true);
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 350), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setFocusable(true);
        panel3.setInheritsPopupMenu(true);
        panel3.setRequestFocusEnabled(true);
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setFocusable(true);
        label1.setRequestFocusEnabled(true);
        label1.setText("Oppdraginfo");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setFocusable(true);
        panel4.setInheritsPopupMenu(true);
        panel4.setRequestFocusEnabled(true);
        panel2.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setFocusable(true);
        label2.setRequestFocusEnabled(true);
        label2.setText("Kontaktinfo");
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setFocusable(true);
        scrollPane1.setInheritsPopupMenu(true);
        scrollPane1.setRequestFocusEnabled(true);
        scrollPane1.setWheelScrollingEnabled(true);
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contactTable.setAutoResizeMode(4);
        contactTable.setFillsViewportHeight(true);
        contactTable.setFocusable(true);
        contactTable.setGridColor(new Color(-16777216));
        contactTable.setInheritsPopupMenu(true);
        contactTable.setPreferredScrollableViewportSize(new Dimension(300, 200));
        contactTable.setPreferredSize(new Dimension(300, 200));
        contactTable.setRequestFocusEnabled(true);
        contactTable.setRowSelectionAllowed(true);
        contactTable.setShowVerticalLines(true);
        scrollPane1.setViewportView(contactTable);
        infoField = new JPanel();
        infoField.setLayout(new BorderLayout(0, 0));
        panel2.add(infoField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    class contactTableModel implements TableModel {


        @Override
        public int getRowCount() {
            if (task != null) {
                return task.getEmployer().getContactInfo().size() + 1;
            } else {
                return 1;
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Navn";
                case 1:
                    return "Nummer";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {

            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            if (task == null || rowIndex == task.getEmployer().getContactInfo().size()) {
                return "";
            }

            ContactInfo contactInfo = task.getEmployer().getContactInfo().get(rowIndex);


            switch (columnIndex) {
                case 0:
                    return contactInfo.getType();
                case 1:
                    return contactInfo.getNumber();
                default:
                    return new Object();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (task == null) {
                return;
            }

            if (rowIndex == task.getEmployer().getContactInfo().size()) {
                if (((String) aValue).isEmpty()) {
                    return;
                }
                switch (columnIndex) {
                    case 0:
                        task.getEmployer().regContactInfo(-1, (String) aValue, "");
                        break;
                    case 1:
                        task.getEmployer().regContactInfo(-1, "", (String) aValue);
                        break;
                }

                contactTable.setPreferredSize(new Dimension(300, contactTable.getRowCount() * contactTable.getRowHeight()));
                database.updateEmployer(task.getEmployer());
                contactTable.updateUI();
                return;
            }

            ContactInfo contactInfo = task.getEmployer().getContactInfo().get(rowIndex);


            switch (columnIndex) {
                case 0:
                    contactInfo.setType((String) aValue);
                    break;
                case 1:
                    contactInfo.setNumber((String) aValue);
                    break;
                default:

            }


            if (contactInfo.getType().isEmpty() && contactInfo.getNumber().isEmpty()) {
                task.getEmployer().getContactInfo().remove(rowIndex);

                if (rowIndex == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            contactTable.setRowSelectionInterval(0, 0);
                        }
                    });
                } else {
                    final int row = rowIndex;
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            contactTable.setRowSelectionInterval(row, row);
                        }
                    });

                }
            }


            database.updateEmployer(task.getEmployer());
            contactTable.updateUI();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }


}
