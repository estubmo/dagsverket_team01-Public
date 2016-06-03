/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author Eirik
 */
public class MenuBar extends JMenuBar implements ActionListener{
    private MainWindow mainWindow;

    public MenuBar(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        InitUI();
    }

    private void InitUI() {
        //Fil (Meny 1)
        JMenu file = new JMenu("Fil");
        file.setMnemonic(KeyEvent.VK_F);
        
        //Bytt Bruker
        JMenuItem changeUserMenuItem = new JMenuItem("Bytt Bruker");
        changeUserMenuItem.setMnemonic(KeyEvent.VK_B);
        changeUserMenuItem.setToolTipText("Bytt Bruker");
        changeUserMenuItem.addActionListener(this);
        file.add(changeUserMenuItem);
   
/*        //Change Password
        JMenuItem changePassMenuItem = new JMenuItem("Endre Passord");
        changePassMenuItem.setMnemonic(KeyEvent.VK_E);
        changePassMenuItem.setToolTipText("Glemt Passord?");
        changePassMenuItem.addActionListener(this);
        file.add(changePassMenuItem);
*/
        file.addSeparator();

        JMenuItem settingsItem = new JMenuItem("Innstillinger");
        settingsItem.setMnemonic(KeyEvent.VK_I);
        settingsItem.setToolTipText("Åpne innstillinger");
        settingsItem.addActionListener(this);
        file.add(settingsItem);

        file.addSeparator();
        
        //Avslutt
        JMenuItem eMenuItem = new JMenuItem("Avslutt");
        eMenuItem.setMnemonic(KeyEvent.VK_A);
        eMenuItem.setToolTipText("Avslutt applikasjon");
        eMenuItem.addActionListener(this);
        eMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        file.add(eMenuItem);

        add(file);
        
        //Verktøy (Meny 2)
        JMenu tools = new JMenu("Verktøy");
        tools.setMnemonic(KeyEvent.VK_V);
        
               
        
        //regEmployer
        JMenuItem toolItem = new JMenuItem("Registrer ny Kunde");
        toolItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.visRegKunde();
            }
        });
        tools.add(toolItem);
        
        //regWorker
        JMenuItem workerItem= new JMenuItem("Registrer ny Arbeider");
        workerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.visRegWorker();
            }
        });
        tools.add(workerItem);
        
        //regSupervisor
        JMenuItem superItem = new JMenuItem("Registrer ny Arbeidsleder");
        
        //regCar
        //regEquipment
        add(tools);

        //Vis (Meny 3)
       /* JMenu vis = new JMenu("Vis");
        //vis.setMnemonic(KeyEvent.VK_V);

        JMenuItem visItem = new JMenuItem("Oppdrag");
        visItem.addActionListener(this);
        visItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        vis.add(visItem);

        visItem = new JMenuItem("Produkter");
        visItem.addActionListener(this);
        visItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        vis.add(visItem);

        add(vis);*/

        
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String ac = e.getActionCommand();
        if (ac.equals("Bytt Bruker")){
            mainWindow.loggAv();
        } else if (ac.equals("Avslutt")){
            System.exit(0);
        } else if (ac.equals("Oppdrag")){
            mainWindow.visOppgaver();
       /* } else if (ac.equals("Produkter")){
            mainWindow.visProdukter();*/
        } else if (ac.equals("Innstillinger")){
            mainWindow.visInnstillinger();
        }

    }
}
