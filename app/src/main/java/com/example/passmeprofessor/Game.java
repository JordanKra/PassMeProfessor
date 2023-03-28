package com.example.passmeprofessor;
import android.widget.TextView;

public class Game {

    private Timer timer;
    private Rubric currentRubric;
    private Paper currentPaper;

    private long score;

    public Game(){

    }

    //Performs all operations necessary to build first Timer
    public void buildTimer(int totalGameSeconds, TextView timerText){
        timer = new Timer(totalGameSeconds, timerText);
    }

    //Performs all operations necessary to build first Rubric
    public void buildRubric(){
        currentRubric = new Rubric();
    }

    //Performs all operations necessary to build first Rubric
    public void buildPaper(){
        currentPaper = new Paper();
    }

    //Add a number of seconds to the timer
    public void addTime(int addGameSeconds){
        //timer.addTime(addGameSeconds);
    }

    //Add a number of points to the score
    public void addScore(long addScorePoints){
        score += addScorePoints;
    }

    public void evalSwipe(){

    }

    public void showGameOver(){

    }

    public void setNewPaper(){
        currentPaper.generateRandomPaper();

    }

    public void updateUI(){
        // currentPaper.updateUI()
        //
    }

}
