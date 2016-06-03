package dagsverket.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import dagsverket.database.Database;
import dagsverket.system.ContactInfo;
import dagsverket.system.Employer;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by ArneChristian on 02.04.14.
 */
public class RegKunde extends JDialog {
    private JPanel panel1;
    private JTable contactTable;
    JButton fjernButton;
    private JButton leggTilButton;
    private JButton registrerButton;
    private JButton avbrytButton;
    private JTextField textFieldKunde;
    private JPanel contactInfoPanel;
    private JButton brukButton;

    private Database database;


    private Employer kunde;


    public RegKunde(Database database, Employer employer, Dialog owner) {
        super(owner, true);
        this.database = database;

        if (employer == null) {
            employer = new Employer(-1, "");
            setTitle("Registrer Kunde");
        }
        kunde = employer;

        $$$setupUI$$$();

        if (kunde.getEmployerNumber() == -1) {
            registrerButton.setText("Registrer");
            panel1.setBorder(BorderFactory.createTitledBorder("Kunde"));
        }
        textFieldKunde.setText(kunde.getEmpName());
        setResizable(false);
        fjernButton.setEnabled(kunde.getContactInfo().size() > 0);


        add(panel1);
        pack();
        setLocationRelativeTo(null);
        leggTilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                brukButton.setEnabled(true);
                kunde.regContactInfo(-1, "", "");
                int row = contactTable.getRowCount() - 1;
                contactTable.setRowSelectionInterval(row, row);
                fjernButton.setEnabled(true);
                contactTable.updateUI();
            }
        });
        fjernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                brukButton.setEnabled(true);
                int row = contactTable.getSelectedRow();
                if (row >= 0 && row < kunde.getContactInfo().size()) {

                    kunde.getContactInfo().remove(row);

                    if (row >= contactTable.getRowCount()) {
                        int sRow = contactTable.getRowCount() - 1;
                        if (sRow < 0) {
                            contactTable.clearSelection();
                            fjernButton.setEnabled(false);
                        } else {
                            contactTable.setRowSelectionInterval(sRow, sRow);
                        }
                    }
                    contactTable.updateUI();
                }
            }
        });

        registrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kunde.setEmpName(textFieldKunde.getText());
                if (kunde.getEmployerNumber() < 0) {
                    regNewEmployer();
                } else {
                    updateEmployer();
                }
                dispose();
            }
        });
        brukButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                brukButton.setEnabled(false);
                kunde.setEmpName(textFieldKunde.getText());
                if (kunde.getEmployerNumber() < 0) {
                    regNewEmployer();
                } else {
                    updateEmployer();
                }

            }
        });

        avbrytButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });

        textFieldKunde.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                brukButton.setEnabled(true);
            }
        });

    }

    private void regNewEmployer() {
        int id = database.insertEmployer(kunde);
        kunde.setEmployerNumber(id);
    }

    private void updateEmployer() {

        database.updateEmployer(kunde);
    }

    private void createUIComponents() {


        contactTable = new JTable(new TableModel(kunde.getContactInfo()));
    }

    public static void main(String[] args) {
        Employer emp = new Employer(1, "Arne Christian");
        emp.regContactInfo("tlf", "41402921");
        new RegKunde(Database.DATABASE, emp, null).setVisible(true);
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
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setInheritsPopupMenu(true);
        panel1.setMinimumSize(new Dimension(470, 186));
        panel1.setPreferredSize(new Dimension(450, 451));
        panel1.setBorder(BorderFactory.createTitledBorder("Endre Kundedata"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setInheritsPopupMenu(true);
        panel1.add(panel2, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setText("Kunde:  ");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldKunde = new JTextField();
        textFieldKunde.setText("");
        panel2.add(textFieldKunde, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        contactInfoPanel = new JPanel();
        contactInfoPanel.setLayout(new GridLayoutManager(2, 8, new Insets(0, 0, 0, 0), -1, -1));
        contactInfoPanel.setInheritsPopupMenu(true);
        contactInfoPanel.setPreferredSize(new Dimension(-1, 358));
        panel1.add(contactInfoPanel, BorderLayout.CENTER);
        contactInfoPanel.setBorder(BorderFactory.createTitledBorder("Kontaktinfo"));
        final JScrollPane scrollPane1 = new JScrollPane();
        contactInfoPanel.add(scrollPane1, new GridConstraints(0, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contactTable.setAutoResizeMode(4);
        contactTable.setFillsViewportHeight(true);
        contactTable.setFocusCycleRoot(true);
        contactTable.setFocusTraversalPolicyProvider(true);
        contactTable.setGridColor(new Color(-10066330));
        contactTable.setIntercellSpacing(new Dimension(1, 1));
        contactTable.setMaximumSize(new Dimension(520, 32));
        contactTable.setMinimumSize(new Dimension(520, 32));
        contactTable.setPreferredScrollableViewportSize(new Dimension(4250, 2000));
        contactTable.setPreferredSize(new Dimension(350, 32));
        contactTable.setRowSelectionAllowed(true);
        contactTable.setShowVerticalLines(true);
        contactTable.setSurrendersFocusOnKeystroke(false);
        scrollPane1.setViewportView(contactTable);
        final Spacer spacer2 = new Spacer();
        contactInfoPanel.add(spacer2, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(12, -1), new Dimension(12, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        contactInfoPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(12, -1), new Dimension(12, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        contactInfoPanel.add(panel3, new GridConstraints(1, 2, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fjernButton = new JButton();
        fjernButton.setText("Fjern");
        panel3.add(fjernButton, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leggTilButton = new JButton();
        leggTilButton.setText("Legg til");
        panel3.add(leggTilButton, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setInheritsPopupMenu(true);
        panel1.add(panel4, BorderLayout.SOUTH);
        final Spacer spacer5 = new Spacer();
        panel4.add(spacer5, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(16, -1), null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel4.add(spacer6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        avbrytButton = new JButton();
        avbrytButton.setInheritsPopupMenu(true);
        avbrytButton.setText("Avbryt");
        avbrytButton.setVerticalAlignment(0);
        avbrytButton.setVerticalTextPosition(0);
        panel4.add(avbrytButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        brukButton = new JButton();
        brukButton.setEnabled(false);
        brukButton.setInheritsPopupMenu(true);
        brukButton.setText("Bruk");
        panel4.add(brukButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel4.add(spacer7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(2, -1), null, 0, false));
        registrerButton = new JButton();
        registrerButton.setInheritsPopupMenu(true);
        registrerButton.setText("Ok");
        registrerButton.setVerticalAlignment(0);
        registrerButton.setVerticalTextPosition(0);
        panel4.add(registrerButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel4.add(spacer8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(2, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    class TableModel implements javax.swing.table.TableModel {

        private ArrayList<ContactInfo> contactInfos;

        public TableModel(ArrayList<ContactInfo> liste) {
            this.contactInfos = liste;
        }

        @Override
        public int getRowCount() {
            return contactInfos.size();
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
                    return "Kontakt";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                    return String.class;

                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return contactInfos.get(rowIndex).getType();
                case 1:
                    return contactInfos.get(rowIndex).getNumber();
                default:
                    return "-";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    contactInfos.get(rowIndex).setType((String) aValue);
                    break;
                case 1:
                    contactInfos.get(rowIndex).setNumber((String) aValue);
                default:

            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
}



