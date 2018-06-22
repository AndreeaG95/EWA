package com.andreea.ewa.dailyCheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.andreea.ewa.R;
import com.gigamole.library.PulseView;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

/**
 * Created by andreeagb on 6/7/2018.
 */

public class DailyCheck extends AppCompatActivity {

    private PulseView pulseView;
    private Button bStartQuiz,bNext;
    private TextView vQuestion;
    private EditText eTemperature;
    private int state;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_quiz_activity);

        pulseView = findViewById(R.id.pulseView);
        bStartQuiz = findViewById(R.id.startQuiz);
        vQuestion = findViewById(R.id.quizQuestion);
        bNext = findViewById(R.id.nextQuestion);
        eTemperature = findViewById(R.id.editTemperatre);

        eTemperature.setVisibility(View.INVISIBLE);
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
                        eTemperature.setVisibility(View.VISIBLE);
                        pulseView.startPulse();
                        vQuestion.setText("Please enter temperature");
                        state = 3;
                    }else if(state == 3){
                        eTemperature.setVisibility(View.INVISIBLE);
                        vQuestion.setVisibility(View.INVISIBLE);
                        bNext.setText("Finish");
                        pulseView.setVisibility(View.INVISIBLE);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_quiz_container, new SymptomsFragment());
                        fragmentTransaction.commit();
                        state = 4;
                    }else if (state == 4){
                        /* Sumbmit data and close activity. */

                    }else{
                        // Handle error.
                    }
                default:
                    // TODO Auto-generated method stub
                    break;
            }
        }
    };
   float button = (float) 0.5;
    public void symptonClicked(View view) {

        if(view.getAlpha() == 0.5) {
            view.setAlpha(1);
        }else{
            view.setAlpha(button);
        }
    }
}
