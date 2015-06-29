package com.faber.circlestepview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.faber.circlestepview.CircleStepView;

public class MainActivity extends AppCompatActivity {

    private CircleStepView mCircleStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleStepView = ((CircleStepView) findViewById(R.id.circle_step_view));
        mCircleStepView.setTextBelowCircle("Step 1", "Step 2", "Step 3");
        mCircleStepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCircleStepView.setCurrentCircleIndex(mCircleStepView.getCurrentCircleIndex() + 1, true);
            }
        });
    }
}
