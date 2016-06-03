package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.mainWindow.TaskOverview;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by ArneChristian on 28.03.14.
 */
public class TaskView extends JPanel implements ActionListener{

    
    private Database database;
    JMenuItem editItem, deleteItem;
    private TaskOverview taskOverview;
    private TaskListPanel taskListPanel;
    private TaskParticipantEquipmentPanel taskParticipantEquipmentPanel;
    private TaskDescription taskDescription;
    private JFrame mainWindow;
    
    


    public TaskView(Database database, JFrame mainWindow){
        this.database = database;
        this.mainWindow = mainWindow;

        createPopupMenu();

        taskOverview = new TaskOverview(this.database, this);
        taskDescription = new TaskDescription(database);
        taskParticipantEquipmentPanel = new TaskParticipantEquipmentPanel(this.database, this);
        taskListPanel = new TaskListPanel(database,this,this.taskOverview, this.taskParticipantEquipmentPanel, taskDescription);


        setLayout(new BorderLayout());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        middlePanel.add(taskListPanel, BorderLayout.NORTH);



        middlePanel.add(taskDescription, BorderLayout.CENTER);

        add(taskOverview, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(taskParticipantEquipmentPanel, BorderLayout.EAST);
        
        
//



    }
    
    public JFrame getMainWindow(){
        return mainWindow;
    }

    public void createPopupMenu(){
        ///POPUPMENU
        JPopupMenu popupMenu = new JPopupMenu(){
            @Override
            public void show(Component invoker, int x, int y) {
                if (taskListPanel.getSelectedTask() == null){
                    deleteItem.setEnabled(false);
                    editItem.setEnabled(false);
                } else {
                    deleteItem.setEnabled(true);
                    editItem.setEnabled(true);
                }
                super.show(invoker, x, y);
            }
        };

        editItem = new JMenuItem("Rediger Oppdrag");
        editItem.addActionListener(this);
        popupMenu.add(editItem);

        JMenuItem item = new JMenuItem("Legg til Oppdrag");
        item.addActionListener(this);
        popupMenu.add(item);

        deleteItem = new JMenuItem("Slett Oppdrag");
        deleteItem.addActionListener(this);
        popupMenu.add(deleteItem);

        popupMenu.addSeparator();

        item = new JMenuItem("Oppdater Liste");
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic(KeyEvent.VK_R);
        popupMenu.add(item);

        popupMenu.addSeparator();

        item = new JMenuItem("Gå til i dag");
        item.addActionListener(this);
        popupMenu.add(item);

        item = new JMenuItem("Neste Måned");
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_RIGHT);
        popupMenu.add(item);

        item = new JMenuItem("Forrige Måned");
        item.addActionListener(this);
        popupMenu.add(item);

        setComponentPopupMenu(popupMenu);
    }



    @Override
    public void actionPerformed(ActionEvent e) {

        String ac = e.getActionCommand();

        if (ac.equals("Forrige Måned")){
            taskListPanel.previousMonth();

        } else if (ac.equals("Neste Måned")){
            taskListPanel.nextMonth();
        } else if (ac.equals("Gå til i dag")){
            taskListPanel.goToToday();
        } else if (ac.equals("Oppdater Liste")){
            taskListPanel.oppdaterListe();
        } else if (ac.equals("Rediger Oppdrag")){
            taskListPanel.editTask();
        } else if (ac.equals("Legg til Oppdrag")){
            taskListPanel.addTask();
        } else if (ac.equals("Slett Oppdrag")){
            taskListPanel.deleteTask();
        }

    }




    public void oppdaterListe(){
        taskListPanel.oppdaterListe();
        taskOverview.oppdater();
    }

    public void addTask(){
        taskListPanel.addTask();
    }

    public void deleteTask(){
        taskListPanel.deleteTask();
    }


    public TaskOverview getTaskOverview(){
        return taskOverview;
    }




}
