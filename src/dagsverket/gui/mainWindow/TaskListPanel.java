package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.Login;
import dagsverket.gui.SelectEmployer;
import dagsverket.gui.mainWindow.TaskOverview;
import dagsverket.system.Employer;
import dagsverket.system.Task;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by ArneChristian on 26.03.14.
 */
public class TaskListPanel extends JPanel implements  TableModel, ListSelectionListener{

    private JLabel monthLabel;
    private Calendar cal;
    private JTable taskTable;
    private ArrayList<Task> tasks;
    private Database database;
    //private TaskInfoPanel infoPanel;
    private TaskOverview taskOverview;
    private TaskParticipantEquipmentPanel taskParticipantEquipmentPanel;
    private TaskView taskView;
    private TaskDescription taskDescription;

    private ButtonPanel buttonPanel;

    private int modelRow, viewRow;
    private TableModel model;



    public TaskListPanel(Database database, TaskView taskView, TaskOverview taskOverview, TaskParticipantEquipmentPanel taskParticipantEquipmentPanel, TaskDescription taskDescription){
        super();



        this.database = database;
        this.taskView = taskView;
        this.taskOverview = taskOverview;
        this.taskParticipantEquipmentPanel = taskParticipantEquipmentPanel;
        this.taskDescription = taskDescription;
        cal = GregorianCalendar.getInstance();
        cal.set(Calendar.DATE, 1);
        tasks = database.getTasks(cal);
        setLayout(new BorderLayout());



        monthLabel = new JLabel( getMonthName(cal), JLabel.CENTER);
        monthLabel.setFont(monthLabel.getFont().deriveFont(24.0f));
        buttonPanel = new ButtonPanel(monthLabel, taskView);
        add(buttonPanel, BorderLayout.NORTH);




        taskTable = new JTable(this);
        taskTable.setPreferredScrollableViewportSize(new Dimension(500, 300));
        taskTable.setFillsViewportHeight(true);
        taskTable.getSelectionModel().addListSelectionListener(this);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        taskTable.setAutoCreateRowSorter(true);

        taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                viewRow = taskTable.getSelectedRow();
                if (viewRow != -1)
                    modelRow = taskTable.convertRowIndexToModel(viewRow);
                else
                    modelRow = -1;



            }
        });




        //Create the scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, SwingConstants.CENTER);

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));



        setInheritsPopupMenu(true);
        taskTable.setInheritsPopupMenu(false);
        scrollPane.setInheritsPopupMenu(true);
        buttonPanel.setInheritsPopupMenu(true);
        taskTable.getTableHeader().setInheritsPopupMenu(true);

        setFocusable(false);
        taskTable.setFocusable(true);
        scrollPane.setFocusable(false);
        buttonPanel.setFocusable(false);



        taskView.editItem.setEnabled(false);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) rightClick(e);

            }
        };

        addMouseListener(mouseAdapter);
        scrollPane.addMouseListener(mouseAdapter);
        taskTable.addMouseListener(mouseAdapter);
        taskTable.getTableHeader().addMouseListener(mouseAdapter);

    }



    public static String getMonthName(Calendar cal){
        String måned = "";

        switch (cal.get(Calendar.MONTH)){
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

        måned += " " + cal.get(Calendar.YEAR);
        return måned;
    }



    public void nextMonth(){
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        monthLabel.setText(getMonthName(cal));
        oppdaterListe();
    }

    public void previousMonth(){
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        monthLabel.setText(getMonthName(cal));
        oppdaterListe();
    }

    public void goToToday(){
        GregorianCalendar today = (GregorianCalendar)GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, today.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, today.get(Calendar.MONTH));
        monthLabel.setText(getMonthName(cal));
        oppdaterListe();
    }

    public void addTask(){


        //TODO LEGG TIL OPPDRAG
        Calendar regCal = GregorianCalendar.getInstance();

        String dato = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
        String regdato = String.format("%04d%02d%02d", regCal.get(Calendar.YEAR), regCal.get(Calendar.MONTH) + 1, regCal.get(Calendar.DATE));
        Task t = new Task();//1, "Nytt Oppdrag", new SelectEmployer(database, this).getEmployer(), "Address", "Post", "0000", true, "Description", "Befaring", 100, dato, regdato, false);
        t.setStartDate(dato);
        t.setUsername(Login.username);
        t.setEmployer(new SelectEmployer(database, this).getEmployer());
        database.insertTask(t);

        oppdaterListe();

    }

    public void editTask(){

    }

    public void deleteTask(){

        String[] options = {"Ja", "Nei"};

        if (taskTable.getSelectedRowCount() == 1){

            if (modelRow >= 0 && modelRow < tasks.size()){
                Task t = tasks.get(modelRow);
                String text = "Er du sikker at du vil slette \'" + t.getTaskName() + "\'?";

                if (JOptionPane.showOptionDialog(this,text,"Slette",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,new ImageIcon(),options,options[0] ) == 0){
                    database.deleteTask(t);
                    oppdaterListe();
                }
            }
        } else if (taskTable.getSelectedRowCount() > 1){
            int[] selRows = taskTable.getSelectedRows();
            String text = "Er du sikker at du vil slette valgte oppdrag?";
            if (JOptionPane.showOptionDialog(this,text,"Slette",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,new ImageIcon(),options,options[0] ) == 0){
                for (int row : selRows){
                    database.deleteTask(tasks.get(taskTable.convertRowIndexToModel(row)));
                }

                oppdaterListe();
            }
        }
    }

    public void oppdaterListe(){


        taskTable.getRowSorter().setSortKeys(null);



        database.clearEmployers();


        tasks = database.getTasks(cal);



        if (viewRow >= 0 && viewRow < tasks.size()){
            taskTable.setRowSelectionInterval(viewRow, viewRow);
        } else {
            taskTable.clearSelection();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                taskTable.updateUI();
            }
        });


        taskTable.updateUI();
        oppdaterInfoPanel();
    }

    public void oppdaterInfoPanel(){


        if (modelRow >= 0 && modelRow < tasks.size()){
            taskOverview.setTask(tasks.get(modelRow));
            taskParticipantEquipmentPanel.setTask(tasks.get(modelRow));
            taskDescription.setTask(tasks.get(modelRow));
        }
    }

    public void setTasks(ArrayList<Task> newTasks){
        tasks = newTasks;
        taskTable.updateUI();
    }

    public Task getSelectedTask(){



        if (modelRow >= 0 && modelRow < tasks.size()){
            return tasks.get(modelRow);
        } else {
            return null;
        }
    }

    private void rightClick(MouseEvent e){
        int r = -1;
        try {
            r = taskTable.convertRowIndexToModel(taskTable.rowAtPoint(e.getPoint()));
        } catch (Exception e1) {

        }
        if (taskTable.getSelectedRowCount() == 1){


            if (e.getComponent() instanceof JTable && r >= 0 && r < taskTable.getRowCount()) {
                taskView.editItem.setEnabled(true);
                taskView.deleteItem.setEnabled(true);
                taskTable.setRowSelectionInterval(r, r);

            } else {
                taskTable.clearSelection();
                taskView.editItem.setEnabled(false);
                taskView.deleteItem.setEnabled(false);
            }
        } else {
            int[] rows = taskTable.getSelectedRows();
            boolean clickOnSelected = false;
            for (int row : rows){
                if (taskTable.convertRowIndexToModel(row) == r){
                    clickOnSelected = true;
                }
            }
            if (clickOnSelected) {

            } else if (e.getComponent() instanceof JTable && r >= 0 && r < taskTable.getRowCount()) {

                taskTable.setRowSelectionInterval(r, r);

            } else {
                taskTable.clearSelection();

            }


        }
        taskView.getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());

    }


    //<editor-fold> Listeners

    // TableModel
    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Tittel";
            case 1:
                return "Dato";
            case 2:
                return "Arbeidsgiver";
            case 3:
                return "Ute";
            case 4:
                return "Laget av";
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
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (rowIndex >= tasks.size()) return "OOB";
        Task t = tasks.get(rowIndex);
        switch (columnIndex){
            case 0:
                return t.getTaskName();
            case 1:
                return Task.getDato(t);
            case 2:
                return t.getEmployer().getEmpName();
            case 3:
                return t.isIsOutside() ? "Ja" : "Nei";
            case 4:
                return t.getUsername();
            default:
                return null;
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

    //////// ListSelectionListener

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (modelRow < 0){
            taskView.editItem.setEnabled(false);
            taskOverview.setTask(null);
            taskParticipantEquipmentPanel.setTask(null);
            taskDescription.setTask(null);
            return;
        }




        if (modelRow >= 0 && modelRow < tasks.size()){

            //taskView.editItem.setEnabled(true);
            //taskOverview.setTask(tasks.get(row));
            //taskParticipantEquipmentPanel.setTask(tasks.get(row));
            //taskDescription.setTask(tasks.get(row));

        } else {
            taskView.editItem.setEnabled(false);
            taskOverview.setTask(null);
            taskParticipantEquipmentPanel.setTask(null);
            taskDescription.setTask(null);

        }



        oppdaterInfoPanel();
    }


    //</editor-fold>


    private class ButtonPanel extends JPanel{


        public ButtonPanel(JLabel month, ActionListener al){
            super();
            setLayout(new BorderLayout());
            setBackground(new Color(112, 167, 255, 255));



            JButton button = new JButton(new ImageIcon(getClass().getResource("/resource/backIcon.png"))); // <-
            button.setActionCommand("Forrige Måned");
            button.addActionListener(al);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setInheritsPopupMenu(true);
            this.add(button, BorderLayout.WEST);

            this.add(month, SwingConstants.CENTER);

            button = new JButton(new ImageIcon(getClass().getResource("/resource/nextIcon.png"))); // ->
            button.setActionCommand("Neste Måned");
            button.addActionListener(al);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setInheritsPopupMenu(true);
            this.add(button, BorderLayout.EAST);
        }
    }
}
