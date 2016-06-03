package dagsverket.gui.mainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ArneChristian on 08.04.14.
 */
public class ToolBar extends JToolBar {

    private JButton editTask, deleteTask;
    private MainWindow mainWindow;



    public ToolBar(MainWindow mainWindow){
        this.mainWindow = mainWindow;

        /* Ikoner: ca 40x40 px */

        TBActionListener tba = new TBActionListener();
        //JButton button = new JButton("Legg til Oppdrag", new ImageIcon("src/ToolBarIcons/calendar_add.png"));
        JButton button = new JButton("Legg til Oppdrag", new ImageIcon(getClass().getResource("/resource/calendar_add.png")));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);

    /*    editTask = new JButton("Rediger Oppdrag");
        editTask.addActionListener(tba);
        add(editTask);   */

        deleteTask = new JButton("Slett Oppdrag", new ImageIcon(getClass().getResource("/resource/calendar_delete.png")));
        //deleteTask = new JButton("Slett Oppdrag",  new ImageIcon(ToolBar.class.getResource("/toolBarIcons/calendar_delete.png")));
        deleteTask.addActionListener(tba);
        deleteTask.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteTask.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteTask.setBorderPainted(false);
        deleteTask.setContentAreaFilled(false);
        add(deleteTask);

        addSeparator();

        button = new JButton(" Kunde ", new ImageIcon(getClass().getResource("/resource/employer.png")));
        //button = new JButton(" Kunde ",  new ImageIcon(ToolBar.class.getResource("/toolBarIcons/employer.png")));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);
        
        //button = new JButton("Arbeider", new ImageIcon("src/ToolBarIcons/worker.png"));
        button = new JButton("Arbeider",  new ImageIcon(getClass().getResource("/resource/worker.png")));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);
        
        //button = new JButton("Arbeidsleder", new ImageIcon("src/ToolBarIcons/supervisor.png"));
        button = new JButton("Arbeidsleder", new ImageIcon(getClass().getResource("/resource/supervisor.png")));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);

        /*button = new JButton("Bil", new ImageIcon("src/ToolBarIcons/car.png"));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);*/
        
        button = new JButton("Produkt", new ImageIcon(getClass().getResource("/resource/product.png")));
        //button = new JButton("Produkt", new ImageIcon("/toolBarIcons/product.png"));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);
        
        addSeparator();

        button = new JButton("Oppdater", new ImageIcon(getClass().getResource("/resource/refresh.png")));
        //button = new JButton("Oppdater", new ImageIcon("/toolBarIcons/refresh.png"));
        button.addActionListener(tba);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        add(button);




    }



    private class TBActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String ac = e.getActionCommand();
            if (ac.equals("Legg til Oppdrag")){
                mainWindow.addTask();
            } else if (ac.equals("Rediger Oppdrag")){

            } else if (ac.equals("Slett Oppdrag")){
                mainWindow.deleteTask();
            } else if (ac.equals(" Kunde ")){
                mainWindow.visRegKunde();
            } else if (ac.equals("Arbeider")){
                mainWindow.visRegWorker();
                } else if (ac.equals("Arbeidsleder")){
                mainWindow.visRegSupervisor();
                } else if (ac.equals("Bil")){
                    mainWindow.visRegCar();
                }else if(ac.equals("Produkt")){
                    mainWindow.visRegProduct();
            } else if (ac.equals("Oppdater")){
                mainWindow.oppdater();
            }

        }
    }
}
