package ca.uottawa.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

public abstract class Account implements Serializable {

    private String username;
    private String password;
    private String email;

    public Account() {};

    /**
     * Constructor with common fields of all accounts
     * @param username username for the account
     * @param password password for the account
     * @param email email for the account
     */
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getRole();

    @NonNull
    @Override
    public String toString() {
        return this.getUsername();
    }

}
