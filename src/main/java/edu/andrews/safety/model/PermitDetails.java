package edu.andrews.safety.model;

/**
 * Data Transfer Object (DTO) combining Vehicle data (MySQL)
 * and Personnel data (Oracle/Banner).
 */
public class PermitDetails {

    // --- Vehicle Info (From MySQL: gwrpapp) ---
    private String plate;
    private String state;
    private String make;
    private String color;
    private String vin;

    // --- Personnel Info (From Oracle: spriden, spraddr, sprtele) ---
    private String id;
    private String fullName;
    private String workPhone;
    private String depart;
    private String title;
    private String location;

    // --- Constructors ---
    public PermitDetails() {}

    // --- Getters and Setters ---

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getWorkPhone() { return workPhone; }
    public void setWorkPhone(String workPhone) { this.workPhone = workPhone; }

    public String getDepart() { return depart; }
    public void setDepart(String depart) { this.depart = depart; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // --- Debug Helper ---
    @Override
    public String toString() {
        return "PermitDetails{" +
                "plate='" + plate + '\'' +
                ", id='" + id + '\'' +
                ", name='" + fullName + '\'' +
                ", dept='" + depart + '\'' +
                '}';
    }
}