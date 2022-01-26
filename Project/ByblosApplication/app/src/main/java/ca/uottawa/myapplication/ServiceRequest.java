package ca.uottawa.myapplication;

import java.util.ArrayList;
import java.util.Objects;

public class ServiceRequest {
    BranchAccount branch;
    CustomerAccount customer;
    ArrayList<Field> fields;
    String requestDate;
    String requestID;
    Service serviceType;
    String status;

    public ServiceRequest() {
    }

    public ServiceRequest(String branchName, String customerUsername, String requestDate, String serviceType, String fieldData) {
        branch = findBranch(branchName);
        customer = findCustomer(customerUsername);
        this.requestDate = requestDate;
        this.serviceType = findService(serviceType);
        fields = parseFields(fieldData);
        this.requestID = branchName + "-" + customerUsername + "-" + serviceType + "-" + requestDate; // should be unique ?? Unix Time Signatures do not repeat, unlikely user could make more than one per second
        this.status = "PENDING";
    }

    public ServiceRequest(String branchName, String customerUsername, String requestDate, Service s) {
        branch = findBranch(branchName);
        customer = findCustomer(customerUsername);
        this.requestDate = requestDate;
        this.serviceType = s;
        fields = s.getFields();
        this.requestID = branchName + "-" + customerUsername + "-" + serviceType.getServiceName() + "-" + requestDate; // should be unique ?? Unix Time Signatures do not repeat, unlikely user could make more than one per second
        this.status = "PENDING";
    }

    public ServiceRequest(BranchAccount b, CustomerAccount c, String requestDate, String serviceType, String fieldData) {
        branch = b;
        customer = c ;
        this.requestDate = requestDate;
        this.serviceType = findService(serviceType);
        fields = parseFields(fieldData);
        this.requestID = b.getBranchName() + "-" + c.getUsername() + "-" + serviceType + "-" + requestDate; // should be unique ?? Unix Time Signatures do not repeat, unlikely user could make more than one per second
        this.status = "PENDING";
    }

    private BranchAccount findBranch(String branchName) {
        for (BranchAccount b : AccountGenerator.branchAccounts) {
            if (b.getBranchName() != null && b.getBranchName().equals(branchName)) return b;
        }
        return null;
    }

    private CustomerAccount findCustomer(String customerUsername) {
        for (CustomerAccount c : AccountGenerator.customerAccounts) {
            if (c.getUsername().equals(customerUsername)) return c;
        }
        return null;
    }

    private Service findService(String serviceType) {
        for (Service s : AccountGenerator.services) {
            if (s.getServiceName().equals(serviceType)) return s;
        }
        return null;
    }

    private ArrayList<Field> parseFields(String fieldData) {
        ArrayList<Field> res = new ArrayList<>();
        String[] fieldDataValues = fieldData.split("\t");
        ArrayList<String> fieldNames = serviceType.getFieldNames();
        for (int i = 0; i < fieldDataValues.length; i++) {
            res.add(new Field(fieldNames.get(i), fieldDataValues[i]));
        }
        return res;
    }

    public BranchAccount getBranch() {
        return branch;
    }

    public void setBranch(BranchAccount branch) {
        this.branch = branch;
    }

    public CustomerAccount getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerAccount customer) {
        this.customer = customer;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Service getServiceType() {
        return serviceType;
    }

    public void setServiceType(Service serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
