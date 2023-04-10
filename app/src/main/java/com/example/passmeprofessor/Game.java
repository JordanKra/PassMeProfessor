package com.example.passmeprofessor;

import android.util.Log;
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

    private TextView scoreText;

    private long score;
    private boolean started;
    private long streak;

    public Game() {
        score = 0;
        streak = 0;
        started = false;
    }

    public boolean getStarted(){
        return started;
    }
    public void addTimerEndEventListener(TimerEndListener listener) {
        TimerEndListeners.add(listener);
    }

    public void addSwipeEventListener(SwipeListener listener) {
        SwipeListeners.add(listener);
    }

    //Performs all operations necessary to build first Timer
    public void buildTimer(int totalGameSeconds, TextView timerText) {
        timer = new Timer(totalGameSeconds, timerText, this);
        addTimerEndEventListener(this.timer);
        addSwipeEventListener(this.timer);
    }

    //Performs all operations necessary to build first Rubric
    public void buildRubric(TextView a, TextView b, TextView c, TextView d, TextView e) {
        currentRubric = new Rubric(a, b, c, d, e);
    }

    //Performs all operations necessary to build first Rubric
    public void buildPaper(ImageView imgView) {
        currentPaper = new Paper(imgView);
        currentPaper.generateRandomPaper();
        addSwipeEventListener(this.currentPaper);
        Log.d("PaperBuilder", currentPaper.getView().getTag().toString());
    }

    public void findScoreText(TextView score){
        this.scoreText = score;
    }

    //Update score based on whether the SwipeEvent was correct
    public void updateScore(SwipeEvent event) {
        //Christian
        // please use method to encapsulate your logic for updating the score
        // on the condition that the swipe is correct
        //event has attribute Boolean correct that is initialized in Game.evalSwipe()

        if(event.correct) {
            if(streak > 2) {
                score = score + (100 * streak);
                timer.addTime(5);
            } else {
                score += 100;
            }
            streak++;
        } else {
            timer.subtractTime(5);
            streak = 0;
        }

        scoreText.setText("" + score);

    }

    public SwipeEvent evalSwipe(SwipeEvent event) {

        //Get/Store the tag of the currentPaper's ImageView, use getTag, expect the values from R.string.letter_a, R.string.letter_b, .... see strings.xml
        String paperLetterGrade = String.valueOf(currentPaper.getView().getTag());
        Log.d("Tag: ", paperLetterGrade);
        //Get the Boolean PASS/FAIL value of the letter grade from the rubric
        Boolean correctAnswer = currentRubric.getPassFailFromGrade(paperLetterGrade);
        Boolean userGuess = event.swipeDirection;

        if(userGuess == correctAnswer){
            event.correct = true;
        } else {
            event.correct = false;
        }
        return event;
    }

    public void showGameOver() {
        //John
        // please use this method to encapsulate all your game over screen state changes
    }

    public void setNewPaper() {
        currentPaper.generateRandomPaper();
    }


    public void fireTimerEndEvent(TimerEndEvent event) {
        for (TimerEndListener listener : TimerEndListeners) {
            listener.onTimerEnd(event);
        }
        //implement game over screen here
        //call methods that present game over screen
        showGameOver();
    }

    public void fireSwipeEvent(SwipeEvent event) {
        event = evalSwipe(event);
        for (SwipeListener listener : SwipeListeners) {
            listener.onSwipeEvent(event);
        }
        //implement score update here
        //call method that updates score based on the event swipe
        updateScore(event);
        //Generate new paper
        currentPaper.generateRandomPaper();
    }


}
