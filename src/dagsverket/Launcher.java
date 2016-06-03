package dagsverket;

import dagsverket.database.Database;
import dagsverket.gui.Login;
import dagsverket.system.DatabaseInfo;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

/**
 * Created by ArneChristian on 06.04.14.
 */
public class Launcher {
    public static void main(String[] args) {


        DatabaseInfo dbInfo = read();



        if (dbInfo == null){
            dbInfo = writeObj(); //SETUP
        }



        String driver = "com.mysql.jdbc.Driver";
        final Database database = new Database(driver, dbInfo.getUrl(), dbInfo.getUser(), dbInfo.getPassword());


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login(database).setVisible(true);
            }
        });

        System.out.println(dbInfo);
    }

    private static DatabaseInfo read(){
        DatabaseInfo dbInfo = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Resources/dbInfo.ser"));
            dbInfo = (DatabaseInfo) ois.readObject();


            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dbInfo;
    }


    private static DatabaseInfo writeObj(){
        DatabaseInfo dbInfo = new DatabaseInfo();
        dbInfo.setUrl("jdbc:mysql://mysql.stud.aitel.hist.no:3306/14-ing1-t01");
        dbInfo.setUser("14-ing1-t01");
        dbInfo.setPassword("dataing2016");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Resources/dbInfo.ser")));

            oos.writeObject(dbInfo);

            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dbInfo;
    }
}
