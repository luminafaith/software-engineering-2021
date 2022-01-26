package ca.uottawa.myapplication;

import android.util.Log;
import android.widget.ListView;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class AccountGenerator {

    // All account lists stored as part of Account Generator since this class is never instantiated

    // Static accounts lists
    static ArrayList<BranchAccount> branchAccounts = new ArrayList<>();
    static ArrayList<CustomerAccount> customerAccounts = new ArrayList<>();
    static ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
    static ArrayList<AvailableService> availableServices = new ArrayList<>();

    // localAccounts only stores admin as of right now, more local accounts can be added in the future for fixed accounts
    static ArrayList<Account> localAccounts = new ArrayList<>();

    // Static services lists
    static ArrayList<Service> services = new ArrayList<>();

    private static final SecureRandom random = new SecureRandom();
    public enum AccountType {ADMIN, BRANCH, CUSTOMER};

    public static Account generate(String username, String password, String email, AccountType type) {

        // Encrypts password using MD5 encryption before any password handling
        String encryptedPassword;
        try {
            encryptedPassword = hashPassword(password);
            // Log.d("encrypt", encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            encryptedPassword = password;
        }

        // Only to be used to disable encryption
        // encryptedPassword = password;

        switch (type) {
            case BRANCH:
                return new BranchAccount(username, encryptedPassword, email, null, null);

            case CUSTOMER:
                return new CustomerAccount(username, encryptedPassword, email, null, null, null);

            default:
                throw new UnsupportedOperationException("The " + type + " role is not supported in the Byblos application!");
        }

    }

    /**
     * This method is public to be used from MainActivity
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Algorithm from https://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash-in-java
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(password));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

}
