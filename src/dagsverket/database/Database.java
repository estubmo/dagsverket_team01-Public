package dagsverket.database;

import dagsverket.system.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//author @Christopher
public class Database {

    public static final Database DATABASE = new Database("com.mysql.jdbc.Driver", "jdbc:mysql://mysql.stud.aitel.hist.no:3306/14-ing1-t01", "14-ing1-t01", "dataing2016");

    private Connection dbCon;
    private HashMap<Integer, Employer> employerHashMap = new HashMap<>();
    private HashMap<Integer, Supervisor> supervisorHashMap = new HashMap<>();
    private HashMap<Integer, Worker> workerHashMap = new HashMap<>();

    public Database(String driver, String dbURL, String user, String password) {
        if (driver == null || dbURL == null || user == null || password == null) {

            throw new NullPointerException("Driver, URL, user or password is null");
        }

        try {
            Class.forName(driver);
            dbCon = DriverManager.getConnection(dbURL, user, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Testet 08.04.2014
    public boolean insertUser(User user) {
        String username = user.getName();
        String sqlCheckIfExists = "SELECT Count(username) as numbUsersFound FROM login where username = '" + username + "';";

        ResultSet res = null;
        Statement stm = null;
        int userExists = 0;
        try {

            stm = dbCon.createStatement();
            res = stm.executeQuery(sqlCheckIfExists);
            while (res.next()) {
                userExists = res.getInt("numbUsersFound");
            }

            if (userExists == 1) {
                throw new SQLException("Brukernavnet er allerede i bruk");
            } else {
                String insertUser = "INSERT INTO login values('" + user.getName() + "', AES_ENCRYPT('" + user.getPassword() + "', MD5('88ea39439e74fa27c09a4fc0bc8ebe6d00978392')));";
                stm.executeUpdate(insertUser);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);

        }
        return false;

    }

    //Testet 08.04.2014
    /*
     public boolean insertEquip(Equipment eq) {
     ArrayList<Equipment> all = new ArrayList<Equipment>();
     String sqlLine = "SELECT * FROM equipment";

     ResultSet res = null;
     Statement line = null;
     try {
     line = dbCon.createStatement();
     res = line.executeQuery(sqlLine);
     while (res.next()) {
     String typeName = res.getString("type_name");
     float cost = res.getFloat("cost");
     int units = res.getInt("unit");

     Equipment equip = new Equipment(typeName, cost, units);
     all.add(equip);
     }
     for (int i = 0; i < all.size(); i++) {
     if (all.get(i).getTypeName().equals(eq.getTypeName())) {
     throw new IllegalArgumentException("Equipment navnet finnes fra før");
     }
     }
     String sqlLine1 = "INSERT INTO equipment(type_name, cost, unit) values('" + eq.getTypeName() + "', '" + eq.getCost() + "', '" + eq.getUnits() + "')";
     line.executeUpdate(sqlLine1);
     return true;
     } catch (SQLException e) {
     all.clear();
     throw new IllegalArgumentException("The all-list is empty (Insert Equip)" + e.getMessage());
     } finally {
     Opprydder.lukkRS(res);
     Opprydder.lukkSTM(line);

     }

     } //*/
    //Testet 08.04.2014 (Feil at den dupliserer employer inf)
    public int insertEmployer(Employer emp) {
        String sqlLine = "INSERT INTO employer VALUES (DEFAULT , '" + emp.getEmpName() + "')";

        ResultSet res = null;
        Statement line = null;

        try {
            dbCon.setAutoCommit(false);
            line = dbCon.createStatement();
            line.executeUpdate(sqlLine);
            res = line.executeQuery("SELECT LAST_INSERT_ID() as 'id'");
            res.next();
            int id = res.getInt("id");
            emp.setEmployerNumber(id);
            //insertContactInfo(emp);

            ArrayList<ContactInfo> contactInfos = emp.getContactInfo();

            for (int i = 0; i < contactInfos.size(); i++) {
                contactInfos.get(i).setId(insertContactInfo(emp.getEmployerNumber(), contactInfos.get(i)));
            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.setAutoCommit(dbCon, true);
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return -1;

    }

    //Testet 08.04.2014
    public boolean updateEmployer(Employer emp) {
        Statement line = null;
        try {

            line = dbCon.createStatement();

            String sqlLine = "UPDATE employer SET business_name = '"
                    + emp.getEmpName() + "' WHERE employer_number = "
                    + emp.getEmployerNumber() + ";";
            line.executeUpdate(sqlLine);

            String delete = "DELETE FROM contactInfo WHERE employer_number = " + emp.getEmployerNumber() + " AND contact_id";

            ArrayList<ContactInfo> contacts = emp.getContactInfo();
            if (contacts.size() > 0) {
                delete += " NOT IN (";

                //   System.out.println(contacts.size());
                for (int i = 0; i < contacts.size(); i++) {

                    //System.out.println("ID:" + contacts.get(i).getId());
                    if (contacts.get(i).getId() >= 0) {
                        updateContactInfo(contacts.get(i));
                    } else {
                        contacts.get(i).setId(insertContactInfo(emp.getEmployerNumber(), contacts.get(i)));
                    }
                    if (i > 0) {
                        delete += ", ";
                    }
                    delete += contacts.get(i).getId();

                }
                delete += ")";
            }

            //System.out.println(delete);
            line.executeUpdate(delete);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    //Testet 08.04.2014
    public boolean deleteContactInfo(ContactInfo contactInfo) {
        String sql = "DELETE FROM contactInfo WHERE contact_id = " + contactInfo.getId();

        Statement stm = null;

        try {
            stm = dbCon.createStatement();
            return stm.executeUpdate(sql) >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(stm);
        }
        return false;
    }

    //Testet 08.04.2014 (Feil at den dupliserer kontakten)
    public int insertContactInfo(int emp, ContactInfo contactInfo) {
        ResultSet res = null;
        Statement stm = null;

        String sql = "INSERT INTO contactInfo values( DEFAULT, " + emp + ",'" + contactInfo.getType() + "','" + contactInfo.getNumber() + "')";

        // System.out.println(sql);
        try {
            dbCon.setAutoCommit(false);
            stm = dbCon.createStatement();
            stm.execute(sql);
            res = stm.executeQuery("SELECT LAST_INSERT_ID() as 'id'");
            res.next();
            return res.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Opprydder.setAutoCommit(dbCon, true);
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return -1;
    }

    public boolean updateContactInfo(ContactInfo contactInfo) {

        String sqlLine = "UPDATE contactInfo SET "
                + "type = '" + contactInfo.getType() + "', "
                + "number = '" + contactInfo.getNumber() + "' "
                + "WHERE contact_id = " + contactInfo.getId();
        //System.out.println(sqlLine);

        Statement stm = null;

        try {
            stm = dbCon.createStatement();
            return stm.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(stm);
        }
        return false;

    }

    public void insertContactInfo(Employer emp) {

        ResultSet res = null;
        Statement line = null;

        try {
            dbCon.setAutoCommit(false);
            line = dbCon.createStatement();
            ArrayList<ContactInfo> contacts = emp.getContactInfo();
            int id = emp.getEmployerNumber();
            for (int i = 0; i < contacts.size(); i++) {
                String name = contacts.get(i).getType();
                String phone = contacts.get(i).getNumber();
                String sqlLine = "INSERT INTO contactInfo VALUES (" + id + ", '" + name + "', '" + phone + "')";
                line.executeUpdate(sqlLine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
    }

    public int insertPerson(Person p) {
        ResultSet res = null;
        Statement line = null;

        String insertPerson = "INSERT INTO person values(DEFAULT, '" + p.getFirstname() + "', '" + p.getSurname() + "')";

        try {
            dbCon.setAutoCommit(false);
            line = dbCon.createStatement();
            line.executeUpdate(insertPerson);
            res = line.executeQuery("SELECT LAST_INSERT_ID() as 'id'");
            res.next();
            int id = res.getInt("id");
            return id;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.setAutoCommit(dbCon, true);
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return -1;
    }

    public int insertTask(Task t) {

        ResultSet res = null;
        Statement line = null;
        try {

            dbCon.setAutoCommit(false);
            line = dbCon.createStatement();

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

            String sDateString = df.format(t.getStartDate().getTime());
            String rDateString = df.format(t.getRegDate().getTime());
            //System.out.println(sDateString);
            //System.out.println(eDateString);

            String sqlLine1 = "INSERT INTO task values('" ///**Unødvendig når alle verdier settes inn**/// (task_name, task_number, employer_number, description, is_outside, cost, startdate, enddate, adress, zipcode, post, username) values('"
                    + t.getTaskName() + "', "
                    + "DEFAULT , "
                    + "NULL, "
                    + t.getEmployer().getEmployerNumber() + ", '"
                    + t.getDescription() + "', "
                    + (t.isIsOutside() ? 1 : 0) + ", '"
                    + t.getBefaring() + "', "
                    + t.getCost() + ", DATE("
                    + sDateString + "), "
                    + (t.getCompleted() ? 1 : 0) + ", '"
                    + t.getAddress() + "', '"
                    + t.getZipcode() + "', '"
                    + t.getPost() + "', '"
                    + t.getUsername() + "', DATE("
                    + rDateString + "), "
                    + (t.isPaid() ? 1 : 0) + ");";

          //  System.out.println(sqlLine1);
            line.execute(sqlLine1);

            res = line.executeQuery("SELECT LAST_INSERT_ID() as 'id'");

            res.next();
            int id = res.getInt("id");

            return id;

        } catch (SQLException e) {
            e.getStackTrace();
            Opprydder.rullTilbake(dbCon);
        } finally {
            Opprydder.setAutoCommit(dbCon, true);
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return -1;
    }

    public boolean insertProduct(Product p) {

        String sqlLine = "INSERT INTO product VALUES (DEFAULT , '" + p.getProductName() + "', " + p.getStock() + ", " + p.getCost() + ")";

        ResultSet res = null;
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return false;
    }
    
        public boolean insertEmployer2(Employer emp){

        String sqlLine = "INSERT INTO employer VALUES (DEFAULT , '" + emp.getEmpName() + "')";

        ResultSet res = null;
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean insertCar(Car c) {
        String sqlLine = "INSERT INTO car VALUES (DEFAULT ,'" + c.getPlateNumber() + "', '" + c.getColour() + "', " + c.getSeats() + ")";

        ResultSet res = null;
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean insertSupervisor(Supervisor sup) {
        int id = insertPerson(sup);
        if (id == -1) {
            return false;
        } else {

            String insertSupervisor = "INSERT INTO supervisor VALUES (" + id + ", '" + sup.getPhoneNumber() + "' , '" + sup.getEmail() + "');";

            Statement line = null;

            try {
                line = dbCon.createStatement();
                line.executeUpdate(insertSupervisor);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Opprydder.lukkSTM(line);
            }
        }
        return false;
    }

    public boolean insertWorker(Worker w) {
        int id = insertPerson(w);
        if (id == -1) {
            return false;
        } else {

            String insertWorker = "INSERT INTO worker VALUES (" + id + ", " + w.getCanWorkOutside() + ")";

            Statement line = null;

            try {
                line = dbCon.createStatement();
                line.executeUpdate(insertWorker);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Opprydder.lukkSTM(line);

            }
        }
        return false;
    }

    public boolean insertWorkerToTask(Worker w, Task t) {

        String insertWorker = "INSERT INTO persontask VALUES (" + w.getPersnr() + ", " + t.getTaskNumber() + ")";

        Statement line = null;

        try {
            line = dbCon.createStatement();
            line.executeUpdate(insertWorker);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);

        }

        return false;
    }

    public boolean deleteSupervisor(Supervisor sup) {
        String sql = "DELETE FROM supervisor WHERE person_number = " + sup.getPersnr();
        String sql1 = "DELETE FROM person WHERE person_number = " + sup.getPersnr();
        Statement line = null;

        try {
            line = dbCon.createStatement();
            if (line.executeUpdate(sql) > 0) {
                if (line.executeUpdate(sql1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteWorker(Worker w) {
        String sqlLine = "DELETE FROM worker WHERE person_number = " + w.getPersnr();
        String sqlLine1 = "DELETE FROM person WHERE person_number = " + w.getPersnr();
        Statement line = null;

        try {
            line = dbCon.createStatement();
            if (line.executeUpdate(sqlLine) > 0) {
                if (line.executeUpdate(sqlLine1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteUser(String name, String password) {
        String SQLsetning = "DELETE FROM login WHERE username = '" + name + "' AND password = AES_ENCRYPT('" + password + "', MD5('88ea39439e74fa27c09a4fc0bc8ebe6d00978392'))";
        Statement line = null;
        try {
            line = dbCon.createStatement();
            if (0 < line.executeUpdate(SQLsetning)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteCar(Car c) {
        String sqlLine = "DELETE FROM car WHERE car_number = '" + c.getCarNumber() + "'";
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return (0 < line.executeUpdate(sqlLine));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteEquip(Equipment eq) {
        String sqlLine = "DELETE FROM person WHERE type_name = '" + eq.getTypeName() + "'";
        Statement line = null;

        try {
            line = dbCon.createStatement();
            if (0 < line.executeUpdate(sqlLine)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deletePerson(Person p) {
        String sqlLine = "DELETE FROM person WHERE person_number = '" + p.getPersnr() + "'";
        Statement line = null;

        try {
            line = dbCon.createStatement();
            if (0 < line.executeUpdate(sqlLine)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteTask(Task t) {
        String sqlLine = "DELETE FROM task WHERE task_number = " + t.getTaskNumber();
        Statement line = null;
        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean deleteProduct(Product p) {
        String sqlLine = "DELETE FROM product WHERE product_number = '" + p.getProductNumber() + "'";
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return (0 < line.executeUpdate(sqlLine));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }
    
        public boolean deleteEmployer(Employer emp) {
        String sqlLine = "DELETE FROM employer WHERE employer_number = '" + emp.getEmployerNumber() + "'";
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return (0 < line.executeUpdate(sqlLine));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }
    
    

    public boolean changePass(String username, String passOld, String passNew) {
        String sqlLine = "SELECT * FROM login";
        ResultSet res = null;
        Statement line = null;

        try {
            //  System.out.println(username + ", " + passOld + ", " + passNew);
            line = dbCon.createStatement();
            res = line.executeQuery(sqlLine);
            while (res.next()) {
                String tmpName = res.getString("username");
                String pass = res.getString("password");

                if (checkUP(username, passOld)) {
                    sqlLine = "UPDATE login SET password = AES_ENCRYPT('" + passNew + "', MD5('88ea39439e74fa27c09a4fc0bc8ebe6d00978392')) WHERE username = '" + username + "'";
                    line.executeUpdate(sqlLine);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean updateSupervisor(Supervisor sup) {
        String sqlLine = "UPDATE person SET first_name = '"
                + sup.getFirstname() + "', last_name = '"
                + sup.getSurname() + "' WHERE person_number = "
                + sup.getPersnr();
        String sqlLine1 = "UPDATE supervisor SET phone_number = '"
                + sup.getPhoneNumber() + "', email_adr = '"
                + sup.getEmail() + "' WHERE person_number = "
                + sup.getPersnr();
        Statement line = null;

        try {
            line = dbCon.createStatement();
            if (line.executeUpdate(sqlLine) > 0) {
                if (line.executeUpdate(sqlLine1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean updateCar(Car c) {

        String sqlLine = "UPDATE car SET plate_number = '"
                + c.getPlateNumber() + "', colour = '"
                + c.getColour() + "', number_of_seats = "
                + c.getSeats() + " WHERE car_number = "
                + c.getCarNumber();
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean updateProduct(Product p) {

        String sqlLine = "UPDATE product SET p_name = '"
                + p.getProductName() + "', stock = "
                + p.getStock() + ", cost = "
                + p.getCost() + " WHERE product_number = "
                + p.getProductNumber();
        Statement line = null;

        try {
            line = dbCon.createStatement();
            return line.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean updateTask(Task t) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String sDateString = df.format(t.getStartDate().getTime());
        String rDateString = df.format(t.getRegDate().getTime());

        String sup;
        if (t.getSupervisor() != null) {
            sup = t.getSupervisor().getPersnr() + "";
        } else {
            sup = "NULL";
        }

        String sqlLine = "UPDATE task SET "
                + "task_name = '" + t.getTaskName() + "', "
                + "employer_number = " + t.getEmployer().getEmployerNumber() + ", "
                + "supervisor_number = " + sup + ", "
                + "description = '" + t.getDescription() + "', "
                + "is_outside = " + (t.isIsOutside() ? 1 : 0) + ", "
                + "befaring = '" + t.getBefaring() + "', "
                + "cost = " + t.getCost() + ", "
                + "startdate = DATE(" + sDateString + "), "
                + "completed = " + (t.getCompleted() ? 1 : 0) + ", "
                + "adress = '" + t.getAddress() + "',"
                + "zipcode = '" + t.getZipcode() + "', "
                + "post = '" + t.getPost() + "', "
                + "username = '" + t.getUsername() + "', "
                + "regDate = DATE(" + rDateString + "), "
                + "paid = " + (t.isPaid() ? 1 : 0) + " "
                + "WHERE task_number = " + t.getTaskNumber();
        ///System.out.println(sqlLine);

        Statement line = null;

        try {
            line = dbCon.createStatement();
            line.executeUpdate(sqlLine);

            String delete = "DELETE FROM equipment WHERE task_number = " + t.getTaskNumber() + " AND equipment_number";

            ArrayList<Equipment> equ = t.getEquipment();
            if (equ.size() > 0) {
                delete += " NOT IN (";

                //   System.out.println(contacts.size());
                for (int i = 0; i < equ.size(); i++) {

                    //System.out.println("ID:" + contacts.get(i).getId());
                    if (equ.get(i).getId() >= 0) {
                        updateEquipment(equ.get(i));
                    } else {
                        equ.get(i).setId(insertEquipment(t.getTaskNumber(), equ.get(i)));
                    }
                    if (i > 0) {
                        delete += ", ";
                    }
                    delete += equ.get(i).getId();

                }
                delete += ")";

                line.execute(delete);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }

    public boolean updateWorker(Worker w) {
        Statement line = null;
        String sqlLine = "UPDATE person SET first_name = '"
                + w.getFirstname() + "', last_name = '"
                + w.getSurname() + "' WHERE person_number = "
                + w.getPersnr();
        String sqlLine1 = "UPDATE worker SET can_work_outside = " + (w.getCanWorkOutside() ? 1 : 0) + " WHERE person_number =" + w.getPersnr();
        //System.out.println(sqlLine1);
        try {
            line = dbCon.createStatement();
            if (line.executeUpdate(sqlLine) > 0) {
                if (line.executeUpdate(sqlLine1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(line);
        }
        return false;
    }
    
     public boolean updateWorkerTask(Task t, ArrayList<Worker> workers){
        Statement stm = null;
        String SQLstring;
        //SELECT * FROM persontask WHERE task_number = 2 AND person_number NOT IN (6, 7, 3);
        try {
            stm = dbCon.createStatement();
            SQLstring = "DELETE FROM persontask WHERE task_number = " + t.getTaskNumber();
            if (workers.size() > 0){
                SQLstring+=" AND person_number NOT IN(";
            
                for (int i = 0; i < workers.size(); i++){
                    if (i > 0){
                        SQLstring+= ",";
                    }
                    SQLstring+= workers.get(i).getPersnr();

                }
                SQLstring+=")";
            } 
            //System.out.println(SQLstring);
            stm.executeUpdate(SQLstring);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(stm);
        }
        
        return false;
    }

    public boolean checkUP(String username, String password) {
        Statement stm = null;
        ResultSet res = null;
        String sql = "SELECT username,password FROM login WHERE username COLLATE latin1_general_cs LIKE '" + username + "' AND password = AES_ENCRYPT('" + password + "', MD5('88ea39439e74fa27c09a4fc0bc8ebe6d00978392'))";

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(sql);

            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return false;

    }

    public ArrayList<Car> getCars() {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM car";
        ArrayList<Car> liste = new ArrayList<Car>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {
                int carnr = res.getInt("car_number");
                String plate = res.getString("plate_number");
                String colour = res.getString("colour");
                int seats = res.getInt("number_of_seats");

                liste.add(new Car(carnr, plate, colour, seats));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public ArrayList<Task> getTasks(Calendar calendar) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Statement stm = null;
        ResultSet res = null;

        try {

            int year = calendar.get(Calendar.YEAR);
            String monthS = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
            String lastDay = "" + calendar.getActualMaximum(Calendar.DATE);
            String sqlSetning = "SELECT * FROM task WHERE startdate BETWEEN DATE(" + year + "" + monthS + "01) AND DATE(" + year + "" + monthS + "" + lastDay + ")";

            stm = dbCon.createStatement();
            res = stm.executeQuery(sqlSetning);

            while (res.next()) {

                int taskNumber = res.getInt("task_number");
                String taskName = res.getString("task_name");
                int employerNumber = res.getInt("employer_number");
                String address = res.getString("adress");
                String poststed = res.getString("post");
                String postnummer = res.getString("zipcode");
                boolean isOutside = res.getInt("is_outside") == 1;
                String description = res.getString("description");
                String befaring = res.getString("befaring");
                double cost = res.getDouble("cost");
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                String startDate = df.format(res.getDate("startdate"));
                String regDate = df.format(res.getDate("regDate"));
                boolean completed = res.getInt("completed") == 1;
                String username = res.getString("username");
                int supervisorNumber = res.getInt("supervisor_number");


                Integer empNumInteger = new Integer(employerNumber);
                if (!employerHashMap.containsKey(empNumInteger)) {
                    employerHashMap.put(empNumInteger, getEmployer(employerNumber));
                }
                
                

                Task task = new Task(taskNumber, taskName, employerHashMap.get(empNumInteger), address, poststed, postnummer, isOutside, description, befaring, cost, startDate, regDate, completed);
                task.setUsername(username);
                task.setEquipment(getEquipment(taskNumber));
                task.setWorker(getWorkerTask(task));

                if (supervisorNumber > 0){
                    Integer supNumInteger = new Integer(supervisorNumber);
                    if (!supervisorHashMap.containsKey(supNumInteger)) {
                       supervisorHashMap.put(supNumInteger, getSupevisor(supNumInteger));
                    }
                    task.setSupervisor(supervisorHashMap.get(supNumInteger));
                }
                

                tasks.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }

        return tasks;

    }

    public Employer getEmployer(int employerNumber) {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM employer WHERE employer_number = " + employerNumber;

        try {

            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            if (res.next()) {

                String empName = res.getString("business_name");
                Employer employer = new Employer(employerNumber, empName);

                ArrayList<ContactInfo> liste = getContactInfo(employerNumber);

                for (ContactInfo c : liste) {
                    employer.regContactInfo(c);
                }
                return employer;

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }

        return new Employer(1, "Arbeidgiver");

    }

    public ArrayList<Worker> getWorkers() {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT DISTINCT workers.person_number, first_name, last_name, can_work_outside, jobslast30daysview.jobslast30days, jobsnext30daysview.jobsnext30days, lastdateworkedview.lastdateworked FROM workers LEFT JOIN jobslast30daysview ON(workers.person_number = jobslast30daysview.person_number) LEFT JOIN jobsnext30daysview ON (workers.person_number = jobsnext30daysview.person_number) LEFT JOIN lastdateworkedview ON (workers.person_number = lastdateworkedview.person_number);";
        ArrayList<Worker> workers = new ArrayList<Worker>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {
                int person_number = res.getInt("person_number");
                String firstname = res.getString("first_name");
                String surname = res.getString("last_name");
                Boolean canWorkOutside = res.getBoolean("can_work_outside");
                int jobsLast30Days = res.getInt("jobslast30days");
                int jobsNext30Days = res.getInt("jobsnext30days");
                Date lastDateWorked = res.getDate("lastdateworked");

                Worker w = new Worker(person_number, firstname, surname, canWorkOutside);
                workers.add(w);
                w.setJobsLast30Days(jobsLast30Days);
                w.setJobsNext30Days(jobsNext30Days);
                w.setLastDateWorked(lastDateWorked);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return workers;
    }

    public ArrayList<Worker> getWorkerTask(Task t) {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM persontask NATURAL JOIN person NATURAL JOIN worker WHERE task_number = " + t.getTaskNumber()+";";
        ArrayList<Worker> liste = new ArrayList<>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);
            while (res.next()) {
                int persnr = res.getInt("person_number");
                Worker worker;
                if(!workerHashMap.containsKey(new Integer(persnr))){
                
                String firstname = res.getString("first_name");
                String surname = res.getString("last_name");
                boolean canWorkOutside = res.getBoolean("can_work_outside");
                
                worker = new Worker(persnr, firstname, surname, canWorkOutside);
                
                } else {
                    worker = workerHashMap.get(new Integer(persnr));
                }
                liste.add(worker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public ArrayList<ContactInfo> getContactInfo(int empNumber) {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT contact_id,type,number FROM contactInfo WHERE employer_number = " + empNumber;
        ArrayList<ContactInfo> liste = new ArrayList<ContactInfo>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {

                String name = res.getString("type");
                String nr = res.getString("number");
                int id = res.getInt("contact_id");

                liste.add(new ContactInfo(id, name, nr));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public ArrayList<Supervisor> getSupervisors() {
        Statement stm = null;
        ResultSet res = null;
        String sql = "SELECT person_number, first_name, last_name, email_adr, phone_number FROM supervisor NATURAL JOIN person ";
        ArrayList<Supervisor> liste = new ArrayList<Supervisor>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(sql);
            //int persnr, String firstname, String surname, String email, String phoneNumber
            while (res.next()) {
                int persnr = res.getInt("person_number");
                String firstname = res.getString("first_name");
                String surname = res.getString("last_name");
                String phoneNumber = res.getString("phone_number");
                String email = res.getString("email_adr");

                liste.add(new Supervisor(persnr, firstname, surname, email, phoneNumber));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public ArrayList<Product> getProducts() {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM product";
        ArrayList<Product> liste = new ArrayList<Product>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {

                int nr = res.getInt("product_number");
                String name = res.getString("p_name");
                int stock = res.getInt("stock");
                double cost = res.getDouble("cost");

                liste.add(new Product(nr, name, stock, cost));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }
    
        public ArrayList<Employer> getEmployers() {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM employer";
        ArrayList<Employer> liste = new ArrayList<Employer>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {

                int p_nr = res.getInt("employer_number");
                String b_name = res.getString("business_name");

                liste.add(new Employer(p_nr, b_name));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public void clearEmployers() {
        employerHashMap.clear();
    }

    public int insertEquipment(int tasknr, Equipment e) {
        ResultSet res = null;
        Statement stm = null;

        String sql = "INSERT INTO equipment values( DEFAULT, " + tasknr + ",'" + e.getTypeName() + "')";

        //System.out.println(sql);
        try {
            dbCon.setAutoCommit(false);
            stm = dbCon.createStatement();
            stm.execute(sql);
            res = stm.executeQuery("SELECT LAST_INSERT_ID() as 'id'");
            res.next();
            return res.getInt("id");

        } catch (Exception exc) {
            exc.printStackTrace();

        } finally {
            Opprydder.setAutoCommit(dbCon, true);
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return -1;
    }

    public ArrayList<Equipment> getEquipment(int tasknr) {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM equipment WHERE task_number = " + tasknr;
        ArrayList<Equipment> liste = new ArrayList<Equipment>();

        try {
            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);

            while (res.next()) {

                int nr = res.getInt("equipment_number");
                String name = res.getString("type_name");

                liste.add(new Equipment(nr, name));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }
        return liste;
    }

    public boolean updateEquipment(Equipment equ) {

        String sqlLine = "UPDATE equipment SET "
                + "type_name = '" + equ.getTypeName() + "' "
                + "WHERE equipment_number = " + equ.getId();
        //System.out.println(sqlLine);

        Statement stm = null;

        try {
            stm = dbCon.createStatement();
            return stm.executeUpdate(sqlLine) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Opprydder.lukkSTM(stm);
        }
        return false;

    }

    private Supervisor getSupevisor(int supNumber) {
        Statement stm = null;
        ResultSet res = null;
        String SQLsetning = "SELECT * FROM supervisor NATURAL JOIN person WHERE person_number = " + supNumber + ";";
        Supervisor supervisor = null;
        try {

            stm = dbCon.createStatement();
            res = stm.executeQuery(SQLsetning);
            
            if (res.next()) {

                String firstname = res.getString("first_name");
                String surname = res.getString("last_name");
                String email = res.getString("email_adr");
                String phoneNumber= res.getString("phone_number");
                
                
                supervisor = new Supervisor(supNumber, firstname, surname, email, phoneNumber);

                }
                

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            Opprydder.lukkRS(res);
            Opprydder.lukkSTM(stm);
        }

         return supervisor;
    }

}
