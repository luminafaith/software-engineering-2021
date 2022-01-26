package ca.uottawa.myapplication;
import androidx.annotation.NonNull;

import java.util.Date;

public class CustomerAccount extends Account{
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String addressString;
    private Address address;

    public CustomerAccount() {};

    /**
     * Constructor to initialize a new Branch Account
     * @param username username of the customer account
     * @param password hashed password associated the customer account
     * @param email email of the customer
     * @param dateOfBirth date of birth of the customer
     * @param firstName first name of the customer
     * @param lastName last name of the customer
     */

    public CustomerAccount(String username, String password, String email, String dateOfBirth, String firstName, String lastName){
        super(username, password, email);
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

//    public String getAddress() { return addressString; }
    public Address getAddress() { return address; }

//    public void setAddress(String address) { this.addressString = address; }
    public void setAddress(Address address) { this.address = address; }
}
