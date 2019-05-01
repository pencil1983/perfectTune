package com.karlotoy.perfecttune;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.karlotoy.perfectune.constants.TuneNote;
import com.karlotoy.perfectune.thread.TuneThread;

import java.lang.reflect.Field;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {

    private SeekBar mSlider_1, mSlider_2;
    private double sliderval;
    private TuneThread perfectTune1, perfectTune2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mSlider_1 = (SeekBar) findViewById(R.id.freq_1_sb);
        mSlider_2 = (SeekBar) findViewById(R.id.freq_2_sb);

        findViewById(R.id.play_1_btn).setOnClickListener(this);
        findViewById(R.id.play_2_btn).setOnClickListener(this);
        findViewById(R.id.stop_1_btn).setOnClickListener(this);
        findViewById(R.id.stop_2_btn).setOnClickListener(this);

        perfectTune1 = new TuneThread();
        perfectTune1.start();

        perfectTune2 = new TuneThread();
        perfectTune2.start();

        mSlider_1.setOnSeekBarChangeListener(getSeekBarChangeListener());
        mSlider_2.setOnSeekBarChangeListener(getSeekBarChangeListener());
    }

    public SeekBar.OnSeekBarChangeListener getSeekBarChangeListener(){
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sliderval = i / (double)seekBar.getMax();
                if(b){
                    switch (seekBar.getId()){
                        case R.id.freq_1_sb:
                            //perfectTune1.setTuneFreq(sliderval * TuneNote.A4);

                            Thread thread = new Thread(){
                                public void run(){
                                    TuneNote tn = new TuneNote();
                                    allKindsOfTest(tn);
                                }
                            };
                            thread.start();
                            break;
                        case R.id.freq_2_sb:
                            perfectTune2.setTuneFreq(sliderval * TuneNote.A4);
                            break;
                    }
                }
            }

            private void allKindsOfTest(Object obj){
                Field[] field = obj.getClass().getDeclaredFields();

                for(int i = 0; i < field.length; i++){
                    String name = field[i].getName();
                    try {
                        boolean accessFlag = field[i].isAccessible();
                        field[i].setAccessible(true);
                        Object o;

                        try {
                            o = field[i].get(obj);
                            //Log.e("LandingPage", "����Ķ����а���һ�����µı�����" + name + " = " + o);
                            Double d = Double.parseDouble(o.toString());
                            //Log.e("LandingPage", "����Ķ����а���һ�����µı�����" + name + " = " + d);

                            perfectTune1.setTuneFreq(d);
                            Thread.sleep(2000);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        field[i].setAccessible(accessFlag);
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_1_btn:
                perfectTune1 = new TuneThread();
                perfectTune1.start();
                break;
            case R.id.play_2_btn:
                perfectTune2 = new TuneThread();
                perfectTune2.start();
                break;
            case R.id.stop_1_btn:
                if(perfectTune1 != null){
                    perfectTune1.stopTune();
                    perfectTune1 = null;
                }
                break;
            case R.id.stop_2_btn:
                if(perfectTune2 != null){
                    perfectTune2.stopTune();
                    perfectTune2 = null;
                }
                break;
        }
    }

    public void onDestroy(){
        if(perfectTune1 != null){
            perfectTune1.stopTune();
        }
        if(perfectTune2 != null){
            perfectTune2.stopTune();
        }
        super.onDestroy();
    }


}
