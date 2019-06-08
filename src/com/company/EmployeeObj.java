package com.company;

import java.text.DecimalFormat;

public class EmployeeObj {

    private String employeeName;
    private double employeeScore;

    public EmployeeObj(String employeeName, double employeeScore) {
        this.employeeName = employeeName;
        this.employeeScore = employeeScore;
    }

    public double getEmployeeScore() {
        return employeeScore;
    }

    @Override
    public String toString(){
        DecimalFormat formatter = new DecimalFormat("#.##");
        return employeeName  + ", " + Double.parseDouble(formatter.format(employeeScore));
    }

}
