package ca.uottawa.myapplication;

import androidx.annotation.NonNull;

public class BranchAccount extends Account {

    private WorkingHours workingHours; // Total working hours of the specific branch
    private String branchName; // Name of the specific branch, separate from username
    private String addressString; // Address of the branch
    private Address address;
    private String phoneNumber; // Branch phone number
    public BranchAccount() {};

    /**
     * Constructor to initialize a new Branch Account
     * @param username username of the branch account
     * @param password hashed password associated the branch account
     * @param email email of the branch
     * @param workingHours total working hours of the branch
     * @param branchName name of the branch
     */
    public BranchAccount(String username, String password, String email, WorkingHours workingHours, String branchName) {
        super(username, password, email);
        this.workingHours = workingHours;
        this.branchName = branchName;
    }

    // Assign Branch role to all Branch account instances
    @Override
    public String getRole() {
        return "Branch";
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

//    public String getAddress() {
//        return addressString;
//    }
    public Address getAddress() { return address; }

//    public void setAddress(String address) {
//        this.addressString = address;
//    }
    public void setAddress(Address address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}

    @Override
    public String toString() {
        return "BranchAccount{" +
                "workingHours=" + workingHours +
                ", branchName='" + branchName + '\'' +
                ", addressString='" + addressString + '\'' +
                ", address=" + address +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
