package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.gui.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ArneChristian on 26.03.14.
 */
public class MainWindow extends JFrame {

    private Database database;
    private TaskView taskView;
    private Login login;
    private ProductView productView;
    private JPanel centerPanel;


    //private RegKunde regKundeWindow;
    //
    private String username;


    public MainWindow(Database database, Login login){

        this.database = database;
        this.login = login;
        username = login.getUsername();

        this.setJMenuBar(new MenuBar(this));


        setLayout(new BorderLayout());
        this.add(new ToolBar(this), BorderLayout.PAGE_START);
        centerPanel = new JPanel(new CardLayout());


        


        taskView = new TaskView(database, this);
        centerPanel.add(taskView, "taskView");

       /* productView = new ProductView(database);
        centerPanel.add(productView, "productView");
*/

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setFocusable(false);

        add(scrollPane, BorderLayout.CENTER);


        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize())); //Brukes om vi vil ha vinduet ikke-maksimert, men så stort som skjermen
        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setVisible(true);

    }

    public void loggAv(){

        if (login == null){
            login = new Login(database);
        }

        login.clearPassword();
        login.setVisible(true);
        dispose();
    }

  /*  public void visProdukter(){

        ((CardLayout)centerPanel.getLayout()).show(centerPanel, "productView");

    }*/

    public void visOppgaver(){
        ((CardLayout)centerPanel.getLayout()).show(centerPanel, "taskView");
    }

    public void visRegKunde(){
        new REDKunde(database, this).setVisible(true);
        //new RegKunde(database, null, this).setVisible(true);
    }

    public void visRegTask(){

    }

    public void visRegWorker(){
        new RegWorker(database, this).setVisible(true);
        
    }
    
    public void visRegSupervisor(){
        new RegSupervisor(database, this).setVisible(true);
        System.out.println("$#");
        taskView.getTaskOverview().oppdater();
    }
    
    public void visRegCar(){
        new RegCar(database, this).setVisible(true);
    }

    public void visInnstillinger(){
        new Settings(database, this).setVisible(true);
    }
    
    public void visRegProduct(){
        new ProductPanel(database, this).setVisible(true);
    }

    public void addTask(){
        taskView.addTask();
    }

    public void deleteTask(){
        taskView.deleteTask();
    }

    public void oppdater(){
        taskView.oppdaterListe();
    }






    public static void main(String[] args) {
        new MainWindow(Database.DATABASE, new Login(Database.DATABASE));

        }
    }


    

