package com.example.passmeprofessor;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer {

    public Timer(int seconds, TextView timerText){
        long converter = Long.valueOf(seconds) * Long.valueOf(1000);
        long temp = 1000;

        new CountDownTimer(converter, temp){
            public void onTick(long millisUntilFinished){
                timerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerText.setText("done!");
            }

        }.start();
    }



}
