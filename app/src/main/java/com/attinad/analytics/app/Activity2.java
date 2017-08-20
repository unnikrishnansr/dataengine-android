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

public class Activity2 extends AppCompatActivity {
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
                map.putValue("ContentId", "22222");
                map.putValue("Page", 1);

                DataEngine.getInstance().trackEvent("Button Click", Activity2.class.getSimpleName(), map);

                startActivity(new Intent(Activity2.this, Activity3.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        DataEngine.getInstance().screenEvent("Page1",  null);
    }
}
