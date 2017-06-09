package com.mqt.solarprogressview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import tyrantgit.explosionfield.ExplosionAnimator;
import tyrantgit.explosionfield.ExplosionField;
import tyrantgit.explosionfield.Utils;

public class SampleActy extends AppCompatActivity {

    private SensorManager manager;
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float intensity = event.values[2];
            int size = (int) ((intensity * sv_light.getMax() / 120));
            sv_light.setPregress(size);
            textView.setText(
                    "\naccuracy : " + event.accuracy
                            + "\ntimestamp : " + event.timestamp
                            + "\nvalues : " + Arrays.toString(event.values)
            );
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private Sensor sensor;
    private SolarView sv_light;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_acty);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sv_light = (SolarView) findViewById(R.id.sv_light);
        textView = (TextView) findViewById(R.id.textView);
        sv_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_light.setPregress((sv_light.getPregress() + 1) % (sv_light.getMax() + 1));
                if (sv_light.getPregress() == sv_light.getMax()) {
                    ExplosionField field = ExplosionField.attach2Window(SampleActy.this);
                    field.explode(v);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // manager.unregisterListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
