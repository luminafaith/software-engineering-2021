package ca.uottawa.myapplication;

import java.util.Objects;

public class Field {
    String fieldName; // used for display
    String fieldValue; // used to store the value the user input

    public Field() {};

    public Field(String name, String value) {
        fieldName = name;
        fieldValue = value;
    }

    public Field(String name) {
        fieldName = name;
        fieldValue = null;
    }

    public String getName() {
        return fieldName;
    }

    public void setName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(fieldName, field.fieldName) && Objects.equals(fieldValue, field.fieldValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldValue);
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                '}';
    }
}
