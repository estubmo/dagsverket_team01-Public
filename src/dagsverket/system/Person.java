package dagsverket.system;

/**
 * Created by ArneChristian on 21.03.14.
 */
public class Person {

    private int persnr;
    private String firstname;
    private String surname;

    public Person(int persnr, String firstname, String surname) {
        if (firstname == null || firstname.trim().equals("")){
            throw new IllegalArgumentException("Firstname is null or blank");
        }
        if (surname == null || surname.trim().equals("")){
            throw new IllegalArgumentException("Surname is null or blank");
        }
        this.persnr = persnr;
        this.firstname = firstname;
        this.surname = surname;
    }
//<editor-fold> Getters
    public String getFirstname() {
        return firstname;
    }

    public int getPersnr() {
        return persnr;
    }

    public String getSurname() {
        return surname;
    }
//</editor-fold>Getters
//<editor-fold>Setters
    public void setFirstname(String firstname) {
        if (firstname != null && !firstname.trim().equals("")){
            this.firstname = firstname;
        } else {
            throw new IllegalArgumentException("Fornavn er blank");
        }
    }
    public void setSurname(String surname) {
        if (surname != null && !surname.trim().equals("")){
            this.surname = surname;
        } else {
            throw new IllegalArgumentException("Etternavn er blank");
        }
    }
//</editor-fold>Setters
    public String toString(){
        return "Personnr.: " + getPersnr() + "\nFornavn: " + getFirstname() + "\nEtternavn: " + getSurname();
    }
    
}
