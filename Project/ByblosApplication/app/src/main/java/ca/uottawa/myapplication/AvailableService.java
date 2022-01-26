package ca.uottawa.myapplication;

public class AvailableService {
    private String username;
    private String listOfServices;

    public AvailableService() {}

    public AvailableService(String username, String listOfServices) {
        this.username = username;
        this.listOfServices = listOfServices;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getListOfServices() {
        return listOfServices;
    }

    public void setListOfServices(String listOfServices) {
        this.listOfServices = listOfServices;
    }

    @Override
    public String toString() {
        return "AvailableService{" +
                "username='" + username + '\'' +
                ", listOfServices='" + listOfServices + '\'' +
                '}';
    }
}
