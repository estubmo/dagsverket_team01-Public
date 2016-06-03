package dagsverket.gui;

//import com.sun.codemodel.internal.JOp;
import dagsverket.database.Database;
import dagsverket.gui.mainWindow.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * Created by ArneChristian on 27.03.14.
 */
public class Login extends JFrame implements KeyListener, ActionListener{
    
    private Database database;
    private JTextFieldWithPlaceholder brukernavn;
    private JTextFieldWithPlaceholder password;
    private JButton loginKnapp;
    public static String username;



    int posX=0,posY=0;


    public Login(Database database){
        super("Login");
        this.database = database;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initComponents();
        pack();
        setLocationRelativeTo(null);


        this.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                posX=e.getX();
                posY=e.getY();
            }
        });

        this.addMouseMotionListener(new MouseAdapter()
        {
            public void mouseDragged(MouseEvent evt)
            {
                //sets frame position when mouse dragged
                setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);

            }
        });

    }




    public void initComponents(){



        JPanel login = new JPanel();


        login.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setUndecorated(true);

        rootPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        brukernavn = new JTextFieldWithPlaceholder(12, "Brukernavn", (char) 0);
        brukernavn.addKeyListener(this);
        login.setFocusable(true);
        login.grabFocus();

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 5;
        c.anchor = GridBagConstraints.CENTER;
        c.insets.set(12, 12, 3, 12);
        login.add(brukernavn, c);




        password = new JTextFieldWithPlaceholder(12, "Passord", '•');
        password.addKeyListener(this);
        c.gridy = 2;
        c.insets.set(3, 12, 12, 12);
        login.add(password, c);

        loginKnapp = new JButton("Logg inn");
        loginKnapp.addActionListener(this);
        c.gridy = 3;
        c.gridx = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets.set(0, 0, 12, 12);
        login.add(loginKnapp, c);


        JButton button = new JButton("Avslutt");
        button.addActionListener(this);
        c.gridx = 1;
        c.insets.set(0, 12, 12, 0);
        login.add(button, c);
        login.add(button, c);


        rootPane.setLayout(new GridBagLayout());
        
        //JLabel icon = new JLabel(new ImageIcon("src/ToolBarIcons/stavne-dagsverket.png"), JLabel.CENTER);
        JLabel icon = new JLabel(new ImageIcon(getClass().getResource("/resource/stavne-dagsverket.png")), JLabel.CENTER);
       
        
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.insets.set(12, 12, 0, 12);
        c.anchor = GridBagConstraints.CENTER;

        rootPane.add(icon, c);




        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridy = 1;
        c.gridx = 0;
        c.insets.set(0, 0, 0, 0);

        rootPane.add(login, c);





    }

    public void clearPassword(){
        password.clear();
    }




    public static void main(String[] args) {


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }

        new Login(Database.DATABASE).setVisible(true);

    }

    private void login(){
        String brkNavn = brukernavn.getText();
        String pass = new String(password.getPassword());

        if (database.checkUP(brkNavn, pass)){
            password.setText("");
            password.clear();
            loginKnapp.requestFocus();
            username = brkNavn;
            setVisible(false);
            new MainWindow(database, this);

        } else {
            brukernavn.requestFocus();
            brukernavn.setSelectionStart(0);
            brukernavn.setSelectionEnd(brukernavn.getText().length());
            JOptionPane.showMessageDialog(this, "Feil brukernavn eller passord", "Feil med innlogging", JOptionPane.PLAIN_MESSAGE, null);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Logg inn")){
            login();

        } else if (e.getActionCommand().equals("Avslutt")){
            System.exit(0);

        }

    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if (e.getSource().equals(brukernavn)){
                password.requestFocus();
            } else if (e.getSource().equals(password)){
                loginKnapp.requestFocus();
                loginKnapp.doClick();
            }
        }

    }

    public String getUsername(){
        return brukernavn.getText();
    }











}





