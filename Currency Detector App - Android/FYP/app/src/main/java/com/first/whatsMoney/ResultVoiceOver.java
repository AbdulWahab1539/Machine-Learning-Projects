package com.first.whatsMoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResultVoiceOver extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    String finalText;
    int finalScore;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result_voice_over2);

        tts = new TextToSpeech(this, this);
        imageView = findViewById (R.id.imageView);

        Bundle getResult = getIntent ().getExtras ();
        if(getIntent().hasExtra("byteArray")) {
            Bitmap _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            imageView.setImageBitmap(_bitmap);
        }
        assert getResult!=null;
        String text = getResult.getString ("result");
        String score = getResult.getString ("result");
        //Toast.makeText (ResultVoiceOver.this, score, Toast.LENGTH_LONG).show ();
        assert text!=null;
        assert score!=null;
        finalText = text.replaceAll("[^a-zA-Z]", "");
        score = score.replaceAll("[^\\d]", "");
        score = score.substring (1,score.length ()-1);
        Toast.makeText (ResultVoiceOver.this, score, Toast.LENGTH_LONG).show ();
        finalScore = Integer.parseInt (score);
        // ImageView image = findViewById (R.id.imageView);
        TextView resultTextView  = findViewById (R.id.textView);
       // resultTextView.setText (finalText);
        if(finalScore<=80){
            finalText = "No note";
            resultTextView.setText(finalText);
        }
        else{
            if(finalText.contains ("OneThousandRupees")){
                finalText="One Thousand Rupees";
                resultTextView.setText (finalText);
            }
            else if(finalText.contains ("OneHundredRupees")){
                finalText="One Hundred Rupees";
                resultTextView.setText (finalText);
            }
            else if(finalText.contains ("TenRupees")){
                finalText="Ten Rupees";
                resultTextView.setText (finalText);
            }
            else if(finalText.contains ("TwentyRupees")){
                finalText="Twenty Rupees";
                resultTextView.setText (finalText);
            }
            else if(finalText.contains ("FiveThousandRupees")){
                finalText="Five Thousand Rupees";
                resultTextView.setText (finalText);
            }
            else if(finalText.contains ("FiveHundredRupees")){
                finalText="Five Hundred Rupees";
                resultTextView.setText (finalText);
            }
            else {
                finalText="Fifty Rupees";
                resultTextView.setText (finalText);
            }
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
                speak ("You have " + finalText + "! Going back to Camera!");
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
