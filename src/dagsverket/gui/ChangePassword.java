/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.gui;

import dagsverket.database.Database;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Kaw
 */
public class ChangePassword extends JPanel{
    
    private Database db;
    
    private JTextFieldWithPlaceholder username = new JTextFieldWithPlaceholder(12, "Brukernavn", (char) 0);
    private JTextFieldWithPlaceholder passOld =  new JTextFieldWithPlaceholder(12, "Password", '•');
    private JTextFieldWithPlaceholder passNew =  new JTextFieldWithPlaceholder(12, "Nytt Passord", '•');
    private JTextFieldWithPlaceholder passNewG =  new JTextFieldWithPlaceholder(12, "Nytt Passord", '•');
    private String gammel;
    private String ny;
    private String nyG;
    
    public ChangePassword(Database db){
        this.db = db;
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
      /*  JLabel ledetekst = new JLabel("Brukernavn:");
        setFocusable(true);
        grabFocus();
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets.set(6, 12, 6, 12);
        add(ledetekst, c);
        c.gridx = 1;
        add(username, c);   */
        
        JLabel ledetekst = new JLabel("Passord:");
        c.gridy = 2;
        c.gridx = 0;
        c.insets.set(6, 12, 6, 12);
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(ledetekst, c);
        c.gridx = 1;
        add(passOld, c);
        
        ledetekst = new JLabel("Nytt Passord:");
        c.gridy = 3;
        c.gridx = 0;
        c.insets.set(6, 12, 6, 12);
        add(ledetekst, c);
        c.gridx = 1;
        add(passNew, c);
        
        ledetekst = new JLabel("Gjenta nytt Passord:");
        c.gridy = 4;
        c.gridx = 0;
        c.insets.set(6, 12, 6, 12);
        add(ledetekst, c);
        c.gridx = 1;
        add(passNewG, c);
        

          
        JButton okKnapp = new JButton("Endre");
        c.gridy = 5;
        c.gridx = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.insets.set(0, 3, 12, 12);
        //okKnapp.setPreferedSize(new Dimension(30,60));
        
        add(okKnapp, c);

        c.weighty = 1;
        c.gridy = 6;
        add(new JPanel(), c);

        c.gridx = 2;
        c.gridy = 2;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 2;
        add(new JPanel(), c);


        
        
        KnappeLytter knappelytter = new KnappeLytter();  
        okKnapp.addActionListener(knappelytter);
     
    }
    
    class KnappeLytter implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JButton okKnapp = (JButton) e.getSource();
            
            if(e.getActionCommand().equals("Endre")){
                gammel = passOld.getText();
                ny = passNew.getText();
                nyG = passNewG.getText();
                if (ny.equals(nyG) && db.changePass(Login.username, gammel, ny)){
                   JOptionPane.showMessageDialog(getParent(), "Passord er endret", "Passord byttet", JOptionPane.PLAIN_MESSAGE, null);
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Feil oppstod.", "Passord ikke byttet", JOptionPane.DEFAULT_OPTION, null);
                }
            }
           
                
       }
    }
               
    public static void main (String[] args){

        JFrame frame = new JFrame();

        ChangePassword passVindu = new ChangePassword(Database.DATABASE);
        frame.setContentPane(passVindu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void changePass(){
        JFrame frame = new JFrame();
        ChangePassword passVindu = new ChangePassword(Database.DATABASE);
        frame.setContentPane(passVindu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}

