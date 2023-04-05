package com.example.passmeprofessor;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<TimerEndListener> TimerEndListeners = new ArrayList<>();
    private List<SwipeListener> SwipeListeners = new ArrayList<>();
    private Timer timer;
    private Rubric currentRubric;
    private Paper currentPaper;

    private long score;

    public Game() {

    }

    public void addTimerEndEventListener(TimerEndListener listener){
        TimerEndListeners.add(listener);
    }

    //Performs all operations necessary to build first Timer
    public void buildTimer(int totalGameSeconds, TextView timerText) {
        timer = new Timer(totalGameSeconds, timerText, this);
        addTimerEndEventListener(this.timer);
    }

    //Performs all operations necessary to build first Rubric
    public void buildRubric() {
        currentRubric = new Rubric();
    }

    //Performs all operations necessary to build first Rubric
    public void buildPaper(ImageView imgView) {
        currentPaper = new Paper(imgView);
    }

    //Add a number of seconds to the timer
    public void addTime(int addGameSeconds) {
        //timer.addTime(addGameSeconds);
    }

    //Add a number of points to the score
    public void addScore(long addScorePoints) {
        score += addScorePoints;
    }

    public void evalSwipe() {

    }

    public void showGameOver() {

    }

    public void setNewPaper() {
        currentPaper.generateRandomPaper();
    }

    public void updateUI() {
        // currentPaper.updateUI()
        //
    }

    public static void sendTimerEndEvent(){

    }

    public void fireTimerEndEvent(TimerEndEvent event){
        for(TimerEndListener listener: TimerEndListeners){
            listener.onTimerEnd(event);
        }
    }

}
