

package dagsverket.system;


public class Car {
    
    private int carNumber;
    private String plate;
    private String colour;
    private int seats;
    
    public Car(int carNumber, String plate, String colour, int seats){
        this.carNumber = carNumber;
        setPlateNumber(plate);
        setColour(colour);
        setSeats(seats);
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public String getPlateNumber() {
        return plate;
    }

    public void setPlateNumber(String plate) {
        if (plate!= null){
            this.plate = plate;
        } else {
            this.plate = "";
        }
    }

    public String getColour() {
        return colour;
    }
    
    public void setColour(String colour) {
        if (colour != null){
            this.colour = colour;
        } else {
            this.colour = "";
        }
    }
    
    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        if (seats >= 0){
            this.seats = seats;
        } else {
            throw new IllegalArgumentException("Varebeholdning kan ikke være mindre enn 0");
        }
    }

}
