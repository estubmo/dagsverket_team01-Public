/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dagsverket.system;

/**
 *
 * @author Eirik
 */
public class ContactInfo {
    String type;
    String number;
    int id;
    
    public ContactInfo(int id, String name, String number){
        setId(id);
        setType(name);
        setNumber(number);
    }
    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }
    
    public void setType(String name) {
        if (name != null && !name.trim().equals("")){
            this.type = name;
        } else {
            this.type = name; //NY KUNDE
            //throw new IllegalArgumentException("Kontaktinfotype ikke utfylt");
        }
    }
    
    public void setNumber(String number) {
        if (number != null && !number.trim().equals("")){
            this.number = number;
        } else {
            this.number = number;
            //throw new IllegalArgumentException("Kontaktinfo ikke utfylt");
        }
    }
    
    public String toString(){
        return getType() + ": " + getNumber();
    }

    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
}
