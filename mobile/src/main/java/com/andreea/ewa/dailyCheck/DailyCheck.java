package com.andreea.ewa.dailyCheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andreea.ewa.R;
import com.gigamole.library.PulseView;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.SystemClock;

/**
 * Created by andreeagb on 6/7/2018.
 */

public class DailyCheck extends AppCompatActivity {

    private PulseView pulseView;
    private Button bStartQuiz,bNext;
    private TextView vQuestion;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_quiz_activity);

        pulseView = findViewById(R.id.pulseView);
        bStartQuiz = findViewById(R.id.startQuiz);
        vQuestion = findViewById(R.id.quizQuestion);
        bNext = findViewById(R.id.nextQuestion);

        bNext.setVisibility(View.INVISIBLE);
        vQuestion.setVisibility(View.INVISIBLE);
        pulseView.startPulse();
        state = 0;

        bStartQuiz.setOnClickListener(buttonClickListener);
        bNext.setOnClickListener(buttonClickListener);
    }


    private OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.startQuiz:
                    if(state == 0) {
                        /* User started quiz, we need to update page for first quiz*/
                        vQuestion.setText("Please place your finger on the camera in order to measure heart rate and press START");
                        pulseView.finishPulse();
                        vQuestion.setVisibility(View.VISIBLE);
                        state = 1;
                    }else if(state == 1){
                        pulseView.startPulse();
                        /*User completed heart rate and now needs to check symptoms*/
                        //mEasure heart rate and display it.
                       // SystemClock.sleep(7000);
                        pulseView.finishPulse();
                        bStartQuiz.setVisibility(View.INVISIBLE);
                        bNext.setVisibility(View.VISIBLE);
                        vQuestion.setTextSize(36);
                        vQuestion.setText("89bpm");
                        state = 2;
                    }
                    break;
                case R.id.nextQuestion:
                    if(state == 2) {
                        /* Enter temperature case*/
                        pulseView.startPulse();
                        vQuestion.setText("Please enter temperature");
                        state = 3;
                    }else if(state == 3){
                        vQuestion.setText("Please select symptoms");
                        bNext.setText("Finish");
                        state = 4;
                    }else{
                        /* Sumbmit data and close activity. */

                    }
                default:
                    // TODO Auto-generated method stub
                    break;
            }
        }
    };
}
