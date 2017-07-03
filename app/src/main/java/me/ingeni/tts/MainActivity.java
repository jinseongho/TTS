package me.ingeni.tts;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener {

    private TextToSpeech mTTS;
    private AppCompatEditText mEditTTS;
    private AppCompatButton mBtnSpeak;
    private SeekBar mSeekBarPich;
    private SeekBar mSeekBarRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTTS = (AppCompatEditText) findViewById(R.id.edit_tts);
        mBtnSpeak = (AppCompatButton) findViewById(R.id.btn_speak);
        mSeekBarPich = (SeekBar) findViewById(R.id.seekbar_pitch);
        mSeekBarRate = (SeekBar) findViewById(R.id.seekbar_rate);
        mEditTTS.addTextChangedListener(this);
        mBtnSpeak.setOnClickListener(this);
        mSeekBarPich.setOnSeekBarChangeListener(this);
        mSeekBarRate.setOnSeekBarChangeListener(this);
        mTTS = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTTS.shutdown();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_speak) {
            if (mEditTTS.getText().toString().trim().length() > 0) {
                ttsSpeak(mEditTTS.getText().toString());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mBtnSpeak.setEnabled(s.toString().trim().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress > 0) {
            switch (seekBar.getId()) {
                case R.id.seekbar_pitch: {
                    mTTS.setPitch(50 / progress);
                    break;
                }
                case R.id.seekbar_rate: {
                    mTTS.setSpeechRate(50 / progress);
                    break;
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            mTTS.setLanguage(Locale.KOREAN);
        }
    }

    private void ttsSpeak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsNewSpeak(text);
        } else {
            ttsOldSpeak(text);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsNewSpeak(String text) {
        String utteranceId = this.hashCode() + "";
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private void ttsOldSpeak(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }
}
