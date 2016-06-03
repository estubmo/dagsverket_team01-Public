package dagsverket.system;

/**
 * Created by ArneChristian on 21.03.14.
 */
public class Supervisor extends Person{

    private String email;
    private String phoneNumber;

    public Supervisor(int persnr, String firstname, String surname, String email, String phoneNumber) {
        super(persnr, firstname, surname);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }
    // <editor-fold> setters and getters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().equals("")){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email ikke utfylt");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().equals("")){
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Telefonnummer er ikke utfylt");
        }
    }

    //</editor-fold>  setters
    
    public String toString(){
        return getFirstname() + " " + getSurname();
        //return "Personnr.: " + getPersnr() + "\nFornavn: " + getFirstname() + "\nEtternavn: " + getSurname() + "\nE-post: " + getEmail() + "\nTelefonnummer: " + getPhoneNumber();
    }
}
