package ca.uottawa.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class Service {
    String serviceName;
    float hourlyCost;
    int version;
    ArrayList<Field> fields;

    public Service() {};

    public Service(String name, float cost, String[] fieldNames) {
        fields = new ArrayList<>();
        version = 1;
        serviceName = name;
        hourlyCost = cost;

        for (String x : fieldNames) {
            fields.add(new Field(x)); // TODO: ???
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public float getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(float hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public String getFieldNamesAsString() {
        String result = "";
        for (Field f : fields) {
            result += f.getName();
            if (!f.equals(fields.get(fields.size() - 1))) result += "\t";
        }
        return result;
    }

    public ArrayList<String> getFieldNames() {
        ArrayList<String> res = new ArrayList<>();
        for (Field f : fields) {
            res.add(f.getName());
        }
        return res;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field f) {
        fields.add(f);
    }

    public Field getFieldByName(String name) {
        for (Field f : fields) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", hourlyCost=" + hourlyCost +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Float.compare(service.hourlyCost, hourlyCost) == 0 &&
                serviceName.equals(service.serviceName) &&
                Objects.equals(fields, service.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, hourlyCost, fields);
    }
}
