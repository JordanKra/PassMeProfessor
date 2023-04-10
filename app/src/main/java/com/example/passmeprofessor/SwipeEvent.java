package com.example.passmeprofessor;

public class SwipeEvent extends java.util.EventObject{
    Boolean swipeDirection;
        //0 = Left = Fail
        //1 = Right = Pass
    Boolean correct;
        //0 = Swipe was incorrect
        //1 = Swipe was correct
    public SwipeEvent(Object source, Boolean swipeDir){
        super(source);
        swipeDirection = swipeDir;
    }
}
