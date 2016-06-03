/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.system;

import java.util.ArrayList;

/**
 *
 * @author Eirik
 */
public class Employer{
    
    private int employerNumber;
    private String empName;
    private ArrayList<ContactInfo> contacts;
  
    public Employer(int employerNumber, String empName){
        setEmployerNumber(employerNumber);
        setEmpName(empName);
        contacts = new ArrayList<ContactInfo>();
    }

//<editor-fold> Getters
    public int getEmployerNumber() {
        return employerNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public ArrayList<ContactInfo> getContactInfo() {
        return contacts;
    }
   
//<//editor-fold> Getters    
//<editor-fold> Setters
    public void setEmpName(String empName) {
        if (empName != null){
            this.empName = empName;
        } else {
            this.empName = ""; //NY KUNDE
        }
    }
    public void setEmployerNumber(int employerNumber){
        if (employerNumber > 0){
            this.employerNumber = employerNumber;
        } else if (employerNumber == -1) { //NY KUNDE
            this.employerNumber = employerNumber;
        } else {
            throw new IllegalArgumentException("Arbeidsgivernummer kan ikke være et negativt tall eller 0");

        }
    }
    
    public void regContactInfo(int id,String type, String number){
        ContactInfo ci = new ContactInfo(id, type, number);
        contacts.add(ci);
    }
    public void regContactInfo(String type, String number){
        ContactInfo ci = new ContactInfo(-1, type, number);
        contacts.add(ci);
    }

    public void regContactInfo(ContactInfo ci){
        contacts.add(ci);
    }


//</editor-fold> Setters
     public String toString(){
        return empName;
        //return "Arbeidsgivernummer: " + getEmployerNumber() + "\nArbeidsgivernavn: " + getEmpName() + "\n" + getContactInfo();
    }

    
    
    
}
