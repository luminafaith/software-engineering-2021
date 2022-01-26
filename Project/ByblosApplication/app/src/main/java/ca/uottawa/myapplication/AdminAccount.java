package ca.uottawa.myapplication;

public class AdminAccount extends Account {

    /**
     * Constructor to initialize an Admin account with:
     * username 'Admin'
     * password 'Admin' but encrypted
     * email 'admin@byblos.app'
     */
    public AdminAccount() {
        super("admin", "admin", "admin@byblos.app");
    }

    // Assign Admin role to all Admin account instances
    @Override
    public String getRole() {
        return "Admin";
    }

}
