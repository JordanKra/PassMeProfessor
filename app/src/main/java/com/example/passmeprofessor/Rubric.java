package com.example.passmeprofessor;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Rubric {
    Hashtable<String, Boolean> grade_map = new Hashtable<>();
    ArrayList<TextView> gradeTexts = new ArrayList<>();
    Rubric(TextView a, TextView b, TextView c, TextView d, TextView e){
        gradeTexts.add(a);
        gradeTexts.add(b);
        gradeTexts.add(c);
        gradeTexts.add(d);
        gradeTexts.add(e);
        generateRandomRubric();
    }

    public void generateRandomRubric(){
        Boolean val;
        Random random = new Random();
        String FAIL = String.valueOf(R.string.rubric_fail);
        String PASS = String.valueOf(R.string.rubric_pass);
        for(TextView text: gradeTexts){
            val = random.nextBoolean();
            if(!val){//if val is false
                text.setText(FAIL);//set the text to Fail
            }
            if(val){//if val is true
                text.setText(PASS);//set the text to Pass
            }
            grade_map.put(String.valueOf(text.getTag()), val); //add each letter, PASS/FAIL pair to the map
        }
    }

    public boolean getPassFailFromGrade(String grade){
        if(grade_map.containsKey(grade)){//if the grade_map has the key
            return grade_map.get(grade); //return it's PASS FAIL value
        }
        throw new NullPointerException("Grade map does not contain grade"); //otherwise, return NULL
    }
}
