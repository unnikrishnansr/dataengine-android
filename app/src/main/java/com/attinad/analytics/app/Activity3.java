package com.attinad.analytics.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.attinad.analyticsengine.core.datamodels.core.BaseEventMap;
import com.attinad.analyticsengine.core.initializer.DataEngine;

/**
 * Created by unnikrishanansr on 14/8/17.
 */

public class Activity3 extends AppCompatActivity {
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
                map.putValue("ContentId", "333333");
                map.putValue("Page", 2);

                DataEngine.getInstance().trackEvent("Button Click", Activity3.class.getSimpleName(), map);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        DataEngine.getInstance().screenEvent("Page2", null);
    }
}
