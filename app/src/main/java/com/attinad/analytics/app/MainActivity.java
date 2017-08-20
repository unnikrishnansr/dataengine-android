package com.attinad.analytics.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.attinad.analyticsengine.core.datamodels.core.BaseEventMap;
import com.attinad.analyticsengine.core.initializer.DataEngine;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseEventMap map = new BaseEventMap();
                map.putValue("Action", "Click");
                map.putValue("ContentId", "444444");
                map.putValue("Page", 3);

                DataEngine.getInstance().trackEvent("Button Click", MainActivity.class.getSimpleName(), map);
                startActivity(new Intent(MainActivity.this, Activity2.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        DataEngine.getInstance().screenEvent("Home", null);
    }



}
