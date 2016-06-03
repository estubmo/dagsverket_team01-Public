/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.database;
//@author Kai
import java.sql.*;

class Opprydder{
  private static Connection db = null;
  private String databasedriver = "org.apache.derby.jdbc.ClientDriver";   

  public Opprydder(String db, String user, String pass){
    if(db != null || user != null || pass != null){
      try{
        Class.forName(databasedriver);  // laster inn driverklassa
      }
      catch(ClassNotFoundException cnf){
        System.out.println("Driverfeil: Kunne ikkje laste driver! \n "+ cnf.toString());
        System.exit(0); //Ikkje vits å fortsette om databasen ikkje kan koblast opp...
      }
      
      String databasenavn = ""+db+";user="+user+";password="+pass+";";
      try{
        this.db = DriverManager.getConnection(databasenavn);
      }
      catch(SQLException se){
        System.out.println("Kunne ikkje kople til databasen "+db+" med bruker "+user+".\n" + se);
        System.exit(0);
      }
    }
    else{
      System.out.println(user+":"+pass+"@"+db);
      throw new IllegalArgumentException("Du har ikkje oppgitt bruker, passord eller database.");
    }
  }
  
  public static void lukkForbindelse(){
    try{
      db.close();
    }
    catch(SQLException se){
      System.out.println("DB-feil: Feil ved lukking av databaseforbindelse:\n" + se);
    }
  }
  
  public static void lukkRS(ResultSet res) {
    try {
      if (res != null) {
        res.close();
      }
    } catch (SQLException se) {
      System.out.println("DB-feil: Feil ved lukking av ResultSet\n" + se);
    }
  }
  
  public static Statement nySTM(){
    Statement stm = null;
    try{
      stm = db.createStatement();
      System.out.println("Statement er åpna.");
    }
    catch(SQLException se){
      System.out.println("DB-feil: Feil ved åpning av statement.\n"+ se);
      System.exit(0);
    }
    return stm;
  }
  
  public static void lukkSTM(Statement stm) {
    try{
      if (stm != null) {
        stm.close();
      }
    } 
    catch (SQLException se) {
      System.out.println("DB-feil: Feil ved lukking av Statement\n" + se);
    }
  }
  
  public boolean tryUpdate(String[] updates){
    if(updates[0] != null){
      Statement stm = nySTM();
      try{
        setAC(false);
  //      for(int i = 0; i < updates.length; i++){
  //        Statement stm.executeUpdate(updates[i]);
        for(String update : updates){
          if(stm.executeUpdate(update) == 0) return false;
        }
        commit();
      }
      catch(SQLException se){
        System.out.println("DB-feil: tryUpdate(). \n" + se);
        rollback();
        return false;
      }
      finally{
        lukkSTM(stm);
      }
      
    } //if test på updates[]
    else{ 
      System.out.println("BRUKERFEIL: ingen updates!");
      return false;
    }
    return true;
  }

  public void commit(){
    try{
      db.commit();
      }
    catch(SQLException se){
      System.out.println("DB-feil: Kunne ikkje utføre commit! do barrel roll!\n" + se);
//      db.rollback();
    }
  }
  public void rollback(){
    try {
      db.rollback();
    } 
    catch (SQLException se) {
      System.out.println("DB-feil: Feil ved utføring av rollback\n" + se);
    }
  }

    public static void rullTilbake(Connection c){
        try {
            c.rollback();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setAutoCommit(Connection c, boolean ac){
        try {
            c.setAutoCommit(ac);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  public void setAC(boolean sett) {
    try{
      db.setAutoCommit(sett);
    }
    catch (SQLException se) {
      System.out.println("DB-feil: Kunne ikkje sette AutoCommit.\n" + se);
    }
  }
}
