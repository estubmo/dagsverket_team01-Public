/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.RegWorkerTask;
import dagsverket.system.Equipment;
import dagsverket.system.Task;
import dagsverket.system.Worker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Eirik
 */
public class TaskParticipantEquipmentPanel extends JPanel {

    private JPanel panel;
    private JTable participants, equipment;
    private JButton addParticipant;
    private Database database;
    private TaskView taskView;
    private JLabel participantHeader, equipmentHeader;
    private JScrollPane scrollpane;
    private Dimension minSize;

    private JButton deleteEquipmentButton, removeParticipantButton, addEquipmentButton;

    private JScrollPane participantScroll, equipmentScroll;

    private Task task;

    public TaskParticipantEquipmentPanel(Database database, TaskView taskView) {

        this.database = database;
        this.taskView = taskView;

        minSize = new Dimension(320, 16);

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setLayout(new BorderLayout());

        //Deltagere (Header)
        participantHeader = new JLabel("Deltagere");
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.NORTH;
        c.weightx = 1;
        c.weighty = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(participantHeader, c);

        //Deltagere (Tabell)
        c.fill = GridBagConstraints.HORIZONTAL;
        participants = new JTable(new ParticipantTableModel()) {

        };
        participants.setFillsViewportHeight(true);
        participants.setPreferredScrollableViewportSize(new Dimension(300, 16));
        participants.setGridColor(Color.BLACK);
        participants.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        c.gridy = 1;
        panel.add(new JScrollPane(participants), c);

        //Deltagere (Legg til)
        addParticipant = new JButton("Legg til");
        addParticipant.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createRegWorkerTaskDialog();
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        c.gridwidth = 1;
        panel.add(addParticipant, c);
        removeParticipantButton = new JButton("Fjern");
        removeParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeWorkerTask();
            }

        });
        c.gridx = 1;
        panel.add(removeParticipantButton, c);

        //Utstyr (Header)
        c.fill = GridBagConstraints.NORTH;
        equipmentHeader = new JLabel("Utstyr og Bil");
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridx = 0;
        panel.add(equipmentHeader, c);

        //Utstyr (Tabell)
        equipment = new JTable(new EquipmentTableModel());
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 4;
        c.insets.set(0, 1, 0, 1);
        equipment.setFillsViewportHeight(true);
        equipment.setBorder(BorderFactory.createEtchedBorder());
        equipment.setPreferredScrollableViewportSize(new Dimension(300, 50));
        equipment.setGridColor(Color.BLACK);
        equipment.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        equipment.setEnabled(false);

        participantScroll = new JScrollPane(equipment);
        panel.add(equipment, c);

        //Knapper
        addEquipmentButton = new JButton("Legg til");
        addEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        c.gridwidth = 1;
        c.gridy = 5;
        panel.add(addEquipmentButton, c);

        deleteEquipmentButton = new JButton("Fjern");
        deleteEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEquipment();
            }
        });
        c.gridx = 1;
        panel.add(deleteEquipmentButton, c);

        c.gridy = 6;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(0, 0, 0, 0);
        panel.add(javax.swing.Box.createVerticalGlue(), c);

        scrollpane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setMinimumSize(minSize);
        scrollpane.setPreferredSize(minSize);

        add(scrollpane, BorderLayout.CENTER);
        setTask(null);
    }

    public TaskView getTaskView() {
        return taskView;
    }

    public JTable getParticipants() {
        return participants;
    }

    private void removeWorkerTask() {
        task.removeWorkers(participants.getSelectedRows());
        database.updateWorkerTask(task, task.getWorkerList());
        updateUI();
    }

    public void createRegWorkerTaskDialog() {
        if (task != null) {
            RegWorkerTask reg = new RegWorkerTask(task, this, database);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Oppgave må være valgt for å legge til deltager.",
                    "Feilmelding!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setTask(Task task) {
        this.task = task;
        participants.clearSelection();
        equipment.clearSelection();

        if (task != null) {
            participants.setEnabled(true);
            equipment.setEnabled(true);
            deleteEquipmentButton.setEnabled(true);
            addEquipmentButton.setEnabled(true);
            addParticipant.setEnabled(true);
            removeParticipantButton.setEnabled(true);
        } else {
            participants.setEnabled(false);
            equipment.setEnabled(false);
            deleteEquipmentButton.setEnabled(false);
            addEquipmentButton.setEnabled(false);
            addParticipant.setEnabled(false);
            removeParticipantButton.setEnabled(false);
        }

        participants.updateUI();
        equipment.updateUI();
    }

    private void addEquipment() {
        if (task != null) {
            final int row = task.getEquipment().size();
            task.getEquipment().add(new Equipment(" "));

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    equipment.setRowSelectionInterval(row, row);
                    equipment.setPreferredSize(new Dimension(300, equipment.getRowCount() * equipment.getRowHeight()));
                    equipment.updateUI();
                }
            });
            deleteEquipmentButton.setEnabled(equipment.getRowCount() > 1);
        }
    }

    private void removeEquipment() {
        int row = equipment.getSelectedRow();
        if (row >= 0 && row < task.getEquipment().size()) {
            task.getEquipment().remove(row);
            database.updateTask(task);
            equipment.updateUI();
        }
        deleteEquipmentButton.setEnabled(equipment.getRowCount() > 1);

    }

    @Override
    public void updateUI() {
        if (task != null && participants != null) {
            participants.setPreferredScrollableViewportSize(new Dimension(300, task.getWorkerList().size() * 16));
        } 
        
        super.updateUI();
    }

    class ParticipantTableModel implements TableModel {

        String[] columnNames = {
            "Fornavn",
            "Etternavn"
        };

        @Override
        public int getRowCount() {
            if (task == null) {
                return 0;
            }
            int rowCount = task.getWorkerList().size();
            participants.setPreferredScrollableViewportSize(new Dimension(300, rowCount * 16));
            return rowCount;

        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (task == null) {
                return "";
            }

            Worker w = task.getWorkerList().get(rowIndex);
            if (columnIndex == 0) {
                return w.getFirstname();
            } else if (columnIndex == 1) {
                return w.getSurname();
            } else {
                return "";
            }

        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {

            return false;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }

    }

    class EquipmentTableModel implements TableModel {

        @Override
        public int getRowCount() {
            if (task != null) {
                return task.getEquipment().size() + 1;
            } else {
                return 1;
            }
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return "";
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
            if (task == null) {
                return "";
            } else if (rowIndex == task.getEquipment().size()) {
                return "";

            }

            switch (columnIndex) {
                case 0:
                    return task.getEquipment().get(rowIndex).getTypeName();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (task == null) {
                return;
            }
            String value = (String) aValue;
            if (rowIndex == task.getEquipment().size()) {

                if (!value.isEmpty()) {
                    task.getEquipment().add(new Equipment((String) aValue));

                    equipment.setPreferredSize(new Dimension(300, equipment.getRowCount() * equipment.getRowHeight()));
                    database.updateTask(task);
                    equipment.updateUI();

                }
                return;
            }
            if (value.isEmpty()) {
                task.getEquipment().remove(rowIndex);
                equipment.setPreferredSize(new Dimension(300, equipment.getRowCount() * equipment.getRowHeight()));
            } else {
                task.getEquipment().get(rowIndex).setTypeName((String) aValue);
            }
            database.updateTask(task);
            updateUI();

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

}
