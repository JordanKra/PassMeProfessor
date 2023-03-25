package com.example.passmeprofessor;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer {
    private CountDownTimer timer;
    private TextView timeText;
    private int secondsLeft;


    public Timer(int seconds, TextView timerText){
        long converter = Long.valueOf(seconds) * Long.valueOf(1000);
        long temp = 1000;
        timeText = timerText;

        timer = new CountDownTimer(converter, temp){
            public void onTick(long millisUntilFinished){
                secondsLeft = (int) (millisUntilFinished / 1000);
                timerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerText.setText("done!");
            }

        }.start();
    }

    public void addTime(int additionalSec){
        timer.cancel();

        long newTime = (secondsLeft + additionalSec) * 1000;

        timer = new CountDownTimer(newTime, 1000){
            public void onTick(long millisUntilFinished){
                secondsLeft = (int) (millisUntilFinished / 1000);
                timeText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeText.setText("done!");
            }

        }.start();
    }

    public void subtractTime(int secLost){
        timer.cancel();

        long newTime = (secondsLeft - secLost) * 1000;

        timer = new CountDownTimer(newTime, 1000){
            public void onTick(long millisUntilFinished){
                secondsLeft = (int) (millisUntilFinished / 1000);
                timeText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeText.setText("done!");
            }

        }.start();
    }



}
