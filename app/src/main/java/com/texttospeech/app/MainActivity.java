package com.texttospeech.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import java.util.Locale;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity implements OnClickListener, OnInitListener {

    //TextToSpeech object
    private TextToSpeech tts;
    //Status Check Code:
    private int A_CHECK_CODE = 0;

    //Activity Creating:
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get a reference to the button element listed in the layout
        Button speakButton = (Button) findViewById(R.id.speak_button);

        //listen for clicks
        speakButton.setOnClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, A_CHECK_CODE);
    }

    public void onClick(View v) {
       //get text entered
        EditText enteredText = (EditText)findViewById(R.id.editText);
        String words = enteredText.getText().toString();
        speakWords(words);
    }

    //speak the user text
    private void speakWords(String speech) {
        //speak straight away
       tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }


    //act on result of TTS data check

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == A_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, this);
            }
            else {
                //no data is there - get some data
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //setup tts
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(Locale.US);
            }
        }
        else if (initStatus == TextToSpeech.ERROR) {
           Toast.makeText(this, "Apologies, text to speech has phailed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
