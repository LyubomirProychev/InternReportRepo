package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;


public class ResultReport {
    private ArrayList<EmployeeObj> employeeRegister = new ArrayList<>();
    private double maxScore = 0.00;
    private double minScore = Double.MAX_VALUE;


    protected void getReportDefinition (){
        JSONParser parser = new JSONParser();

        //Get the information by which the employees will be sorted and check if criteria is met.
        try{
            Object obj = parser.parse(new FileReader("C:\\Users\\Lubo\\IdeaProjects\\PerformanceReport\\src\\JSONFiles\\ReportDefinition"));
            JSONObject jsonObjRep = (JSONObject) obj;

            int topPerformersThreshold =((Long) jsonObjRep.get("topPerformersThreshold")).intValue();
            boolean useExperienceMultiplier = (boolean) jsonObjRep.get("useExperienceMultiplier");
            int periodLimit = ((Long) jsonObjRep.get("periodLimit")).intValue();


            getEmployeeInformation(topPerformersThreshold, useExperienceMultiplier, periodLimit);


        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Input/Output Error");
            e.printStackTrace();
        }catch (ParseException e){
            System.out.println("Parsing error");
            e.printStackTrace();
        }catch (Exception e) {
            System.out.println("Unknown error");
            e.printStackTrace();
        }

    }
    private void getEmployeeInformation(int topPerformers, boolean useExperienceMultiplier, int periodLimit) {
        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader("C:\\Users\\Lubo\\IdeaProjects\\PerformanceReport\\src\\JSONFiles\\DataFile"));
            JSONObject jsonObjEmp = (JSONObject) obj;

            JSONArray employeeArray = (JSONArray) jsonObjEmp.get("employee");
            JSONObject employee;
            //Cycle all employees ; check if they have enough sales

            for(int i = 0; i < employeeArray.size(); i++){

                employee = (JSONObject) employeeArray.get(i);
                if (periodLimit < ((Long) employee.get("salesPeriod")).intValue()){
                    calculateScore(employee, useExperienceMultiplier);
                }
            }
            topPercentage(topPerformers);
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Input/Output Error");
            e.printStackTrace();
        }catch (ParseException e){
            System.out.println("Parsing error");
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("Unknown error");
            e.printStackTrace();
        }
    }

    private void calculateScore (JSONObject employee, boolean useExperienceMultiplier ) {
        double tempScore ;

        //Calculate the score of each employee which has enough sales;
        if (useExperienceMultiplier){
            tempScore =(((Long) employee.get("totalSales")).doubleValue()/((Long) employee.get("salesPeriod")).doubleValue())* ((Double) employee.get("experienceMultiplier"));

        }else {
            tempScore =(((Long) employee.get("totalSales")).doubleValue()/((Long) employee.get("salesPeriod")).doubleValue());
        }

        if(tempScore > maxScore){
            maxScore = tempScore;
        }
        if(tempScore< minScore){
            minScore = tempScore;
        }

        EmployeeObj newEmployee = new EmployeeObj((String) employee.get("name"), tempScore);
        employeeRegister.add(newEmployee);
    }

    private void topPercentage (int topPerformers) {
        orderByScore();
        double topPercentEmployee;

        System.out.println("Name        Score");

        topPercentEmployee = (maxScore - ((maxScore-minScore)*(topPerformers/100.0))); // calculating top percentage based on the highest and lowest score

        for(int i =0;i<employeeRegister.size();i++){

            if(employeeRegister.get(i).getEmployeeScore() >= topPercentEmployee){
                System.out.println(employeeRegister.get(i));
            }
        }
    }

    private void orderByScore (){

        Comparator<EmployeeObj> comparator = (o1, o2) -> {
            if(o1.getEmployeeScore() > o2.getEmployeeScore())
                return -1;
            else if(o1.getEmployeeScore() == o2.getEmployeeScore())
                return 0;
            else
                return 1;
        };
        employeeRegister.sort(comparator);
    }
}
