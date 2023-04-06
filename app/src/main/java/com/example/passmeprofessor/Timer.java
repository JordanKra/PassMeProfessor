package com.example.passmeprofessor;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer implements TimerEndListener{
    private CountDownTimer timer;
    private TextView timeText;
    private int secondsLeft;

    private Game GameInstance;

    public Timer(int seconds, TextView timerText, Game instance){
        long converter = Long.valueOf(seconds) * Long.valueOf(1000);
        long temp = 1000;
        timeText = timerText;
        GameInstance = instance;

        timer = new CountDownTimer(converter, temp){
            public void onTick(long millisUntilFinished){
                secondsLeft = (int) (millisUntilFinished / 1000);
                timerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                GameInstance.fireTimerEndEvent(new TimerEndEvent(this));
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
                GameInstance.fireTimerEndEvent(new TimerEndEvent(this));
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
                GameInstance.fireTimerEndEvent(new TimerEndEvent(this));

            }

        }.start();
    }


    @Override
    public void onTimerEnd(TimerEndEvent event) {
        timeText.setText("done!");
    }
}
