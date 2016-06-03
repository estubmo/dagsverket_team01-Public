/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dagsverket.gui;


import dagsverket.database.Database;
import dagsverket.gui.mainWindow.TaskParticipantEquipmentPanel;
import dagsverket.system.Task;
import dagsverket.system.Worker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Eirik
 */
public class RegWorkerTask extends JDialog {

    private final Task task;
    private JTextFieldWithPlaceholder searchbar;
    private JButton addButton, okButton;
    private ArrayList<Worker> workers;
    private ArrayList<Worker> searchedWorkers;
    private JPanel panel;
    private GridBagConstraints c;
    private JTable workerTable;
    private TaskParticipantEquipmentPanel taskParticipantEquipmentPanel;
    private Database database;

    public RegWorkerTask(Task task, TaskParticipantEquipmentPanel taskParticipantEquipmentPanel, Database database) {
        super(taskParticipantEquipmentPanel.getTaskView().getMainWindow(), "Registrer deltager", true);
        this.taskParticipantEquipmentPanel = taskParticipantEquipmentPanel;
        this.task = task;
        this.database = database;
        workers = database.getWorkers();
        searchedWorkers = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            searchedWorkers.add(workers.get(i));
        }
        initGui();
    }

    private void initGui() {
        setLayout(new BorderLayout());
        c = new GridBagConstraints();

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 2;
        c.weighty = 0;

        searchbar = new JTextFieldWithPlaceholder(12, "Søk etter navn", (char) 0);
        panel.add(searchbar, c);
        searchbar.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (workers.isEmpty()) {
                } else if (searchbar.getText().equals("")) {
                    searchedWorkers.clear();
                    for (int i = 0; i < workers.size(); i++) {
                        searchedWorkers.add(workers.get(i));
                    }
                    workerTable.updateUI();
                } else if (!searchbar.getText().equals("")) {
                    setSearchedWorkersList(searchbar.getText());
                } else {
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;

        workerTable = new JTable(new regWorkerTaskTableModel());
        workerTable.setMinimumSize(new Dimension(-1, -1));
        workerTable.setPreferredScrollableViewportSize(new Dimension(-1, -1));
        workerTable.setFillsViewportHeight(true);
        workerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(workerTable), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        JPanel emptyPanel = new JPanel();
        panel.add(emptyPanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.weighty = 0;
        c.weightx = 0;
        c.insets.set(6, 6, 6, 6);
        addButton = new JButton("Legg til");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addWorkerToTask();
            }

        });
        panel.add(addButton, c);

        c.insets.set(6, 6, 6, 12);
        c.gridx = 2;
        okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }

        });
        panel.add(okButton, c);

        panel.setOpaque(true);
        panel.setPreferredSize(new Dimension(800, 500));
        //setPreferredSize(new Dimension(500,500));
        add(panel, BorderLayout.CENTER);

        pack();
        //   setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setSearchedWorkersList(String search) {
        if (!workers.isEmpty()) {
            searchedWorkers.clear();
            for (int i = 0; i < workers.size(); i++) {
                if (workers.get(i).getFirstname().toUpperCase().contains(search.toUpperCase()) || workers.get(i).getSurname().toUpperCase().contains(search.toUpperCase())) {
                    searchedWorkers.add(workers.get(i));
                }
            }
        }
        workerTable.updateUI();

    }

    public void closeWindow() {
        this.dispose();
    }

    private void addWorkerToTask() {
        if (workerTable.getSelectedRow() != -1) {
            if (database.insertWorkerToTask(searchedWorkers.get(workerTable.getSelectedRow()), task)) {
                task.getWorkerList().add(searchedWorkers.get(workerTable.getSelectedRow()));
            } else {
                JOptionPane.showMessageDialog(null,
                        searchedWorkers.get(workerTable.getSelectedRow()).getFirstname() + " er allerede lagt til i " + task.getTaskName(),
                        "Feilmelding!",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Arbeider må være valgt!",
                    "Feilmelding!",
                    JOptionPane.ERROR_MESSAGE);
        }
        taskParticipantEquipmentPanel.getParticipants().updateUI();
        taskParticipantEquipmentPanel.updateUI();

    }

    public String getMonthName(Calendar cal) {
        String måned = "";

        switch (cal.get(Calendar.MONTH)) {
            case 0:
                måned += "Januar";
                break;
            case 1:
                måned += "Februar";
                break;
            case 2:
                måned += "Mars";
                break;
            case 3:
                måned += "April";
                break;
            case 4:
                måned += "Mai";
                break;
            case 5:
                måned += "Juni";
                break;
            case 6:
                måned += "Juli";
                break;
            case 7:
                måned += "August";
                break;
            case 8:
                måned += "September";
                break;
            case 9:
                måned += "Oktober";
                break;
            case 10:
                måned += "November";
                break;
            case 11:
                måned += "Desember";
                break;
            default:

        }

        return måned;
    }

    private class regWorkerTaskTableModel implements TableModel {

        public regWorkerTaskTableModel() {
        }

        String[] columnNames = {
            "Fornavn",
            "Etternavn",
            "Kan jobbe utendørs",
            "Siste arbeidsdato",
            "Antall oppdrag siste 30 dager",
            "Antall oppdrag neste 30 dager"
        };

        @Override
        public int getRowCount() {
            return searchedWorkers.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 4 || columnIndex == 5) {
                return Integer.class;
            } else {
                return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            Worker w;
            if (workers != null) {
                w = searchedWorkers.get(rowIndex);
            } else {
                return "";
            }

            if (columnIndex == 0) {
                return w.getFirstname();
            } else if (columnIndex == 1) {
                return w.getSurname();
            } else if (columnIndex == 2) {
                return w.getCanWorkOutside() ? "Ja" : "Nei";
            } else if (columnIndex == 3) {
                return w.getLastDateWorked();
            } else if (columnIndex == 4) {
                return w.getJobsLast30Days();
            } else if (columnIndex == 5) {
                return w.getJobsNext30Days();
            } else {
                return "";
            }

        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

}
