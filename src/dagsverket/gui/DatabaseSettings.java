package dagsverket.gui;

import dagsverket.database.Database;
import dagsverket.system.DatabaseInfo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

/**
 * Created by ArneChristian on 08.04.14.
 */
public class DatabaseSettings extends JPanel {

    private DatabaseInfo databaseInfo;
    private JTextField url,username;
    private JPasswordField password;
    private Settings settings;

    private DatabaseInfo dbInfo;

    public DatabaseSettings(Settings settings){
        this.settings = settings;
        initGUI();

        readDBinfo();

    }

    private void readDBinfo(){


        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Resources/dbInfo.ser")));
            dbInfo = (DatabaseInfo) ois.readObject();


            ois.close();

        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        if (dbInfo == null) return;

        url.setText(dbInfo.getUrl());
        username.setText(dbInfo.getUser());
        password.setText(dbInfo.getPassword());
    }

    public void initGUI(){
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        KeyListener kl = new KeyListener();


        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets.set(12, 0, 0, 0);

        JLabel label = new JLabel("Databasetilkobling");
        add(label, c);


        c.gridy = 1;
        label = new JLabel("URL:");
        add(label, c);



        url = new JTextField(12);
        url.addKeyListener(kl);
        c.gridwidth = 3;

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;

        add(url, c);

        c.gridy = 2;
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.insets.set(4, 0, 0, 0);
        label = new JLabel("Brukernavn:");
        add(label, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;

        username = new JTextField();
        username.addKeyListener(kl);
        add(username, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        label = new JLabel("Passord:");
        add(label, c);

        c.gridx = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;

        password = new JPasswordField();
        password.addKeyListener(kl);
        add(password, c);


        c.gridheight = 1;
        c.gridwidth = 4;
        c.weighty = 0;
        c.weightx = 1;
        c.gridy = 3;
        c.gridx = 0;

        add(new JSeparator(JSeparator.HORIZONTAL), c);





        c.gridheight = 1;
        c.gridwidth = 1;
        c.weighty = 1;
        c.weightx = 0;
        c.gridy = 4;
        c.gridx = 4;

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(12, -1));
        add(spacer, c);

    }

    public void apply(){
        dbInfo = new DatabaseInfo();
        dbInfo.setUrl(url.getText());
        dbInfo.setUser(username.getText());
        dbInfo.setPassword(new String(password.getPassword()));

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Resources/dbInfo.ser")));

            oos.writeObject(dbInfo);

            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            settings.setEnableButtons(true);
        }
    }
}
