package ca.uottawa.myapplication;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class BranchRating implements Serializable {
    String branchUsername;
    String customerUsername;
    int rating;

    public BranchRating() {};

    public BranchRating(String _bu, String _cu, int _r) {
        branchUsername = _bu;
        customerUsername = _cu;
        rating = _r;
    }

    private BranchRating(BranchAccount _b, CustomerAccount _c, int _r) {
        branchUsername = _b.getUsername();
        customerUsername = _c.getUsername();
        rating = _r;
    }

    public String generateKey() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String date = df.format(new Date());
        return branchUsername + "-" + customerUsername + "-" + rating + "-" + date;
    }

    public String getBranchUsername() {
        return branchUsername;
    }

    public void setBranchUsername(String branchUsername) {
        this.branchUsername = branchUsername;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchRating that = (BranchRating) o;
        return rating == that.rating && Objects.equals(branchUsername, that.branchUsername) && Objects.equals(customerUsername, that.customerUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchUsername, customerUsername, rating);
    }

    @Override
    public String toString() {
        return "BranchRating{" +
                "branchUsername='" + branchUsername + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", rating=" + rating +
                '}';
    }
}
