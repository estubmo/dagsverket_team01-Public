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
public class Equipment {
    private String typeName;
    private int id;

    public Equipment(String name){
        this(-1, name);
    }

    public Equipment (int id, String unitName){
        setId(id);
        setTypeName(unitName);

     
    }
// <editor-fold>
    public String getTypeName() {
        return typeName;
    }

   public void setTypeName(String typeName) {
        if (typeName == null || typeName.trim().equals("")){
            this.typeName = "";//            throw new IllegalArgumentException("Navn kan ikke være ingenting");
        } else {
            this.typeName = typeName;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //</editor-fold>
}