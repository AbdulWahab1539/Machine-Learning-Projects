package com.first.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResultVoiceOver extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    String finalText;
    int finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result_voice_over);

        tts = new TextToSpeech(this, this);

        Bundle getResult = getIntent ().getExtras ();
        String text = getResult.getString ("result");
        String score = getResult.getString ("result");
        //Toast.makeText (ResultVoiceOver.this, score, Toast.LENGTH_LONG).show ();
        finalText = text.replaceAll("[^a-zA-Z]", "");
        score = score.replaceAll("[^\\d]", "");
        score = score.substring (1,score.length ()-1);
        Toast.makeText (ResultVoiceOver.this, score, Toast.LENGTH_LONG).show ();
        finalScore = Integer.parseInt (score);
        // ImageView image = findViewById (R.id.imageView);
        TextView resultTextView  = findViewById (R.id.textView);
       // resultTextView.setText (finalText);
        if(finalScore<=80){
            finalText = "Not a note";
            resultTextView.setText(finalText);
            speak (finalText + "Going back to Camera");
        }
        else{
            resultTextView.setText (finalText);
            speak (finalText + "Going back to Camera");
        }
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                speak (finalText + "! Going back to Camera!");
            }


        } else {
            Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String message) {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener () {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent Intent = new Intent(ResultVoiceOver.this, Camera.class);
                        startActivity(Intent);
                        finish();

                    }
                });


            }

            @Override
            public void onError(String s) {
            }
        });
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, params, "Dummy String");
        }

    }
    @Override
    protected void onPause() {
        finish();
        System.exit(0);
        tts.shutdown();
        super.onPause();
    }

}
