package ca.uottawa.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkingHours implements Serializable {

    private ArrayList<String> startingHours;
    private ArrayList<String> endingHours;

    public WorkingHours() {};

    public WorkingHours(ArrayList<String> start, ArrayList<String> end) {
        startingHours = start;
        endingHours = end;
    }

    public WorkingHours(String defaultStart, String defaultEnd) {
        startingHours = new ArrayList<>();
        endingHours = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            startingHours.add(defaultStart);
            endingHours.add(defaultEnd);
        }
    }

    public ArrayList<String> getStartingHours() {
        return startingHours;
    }

    public void setStartingHours(ArrayList<String> startingHours) {
        this.startingHours = startingHours;
    }

    public ArrayList<String> getEndingHours() {
        return endingHours;
    }

    public void setEndingHours(ArrayList<String> endingHours) {
        this.endingHours = endingHours;
    }

    @Override
    public String toString() {
        return "WorkingHours{" +
                "startingHours=" + startingHours +
                ", endingHours=" + endingHours +
                '}';
    }
}
