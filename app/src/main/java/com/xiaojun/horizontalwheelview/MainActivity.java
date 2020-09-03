package com.xiaojun.horizontalwheelview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xiaojun.horizontalwheelview.view.HorizontalWheelView;

public class MainActivity extends AppCompatActivity {

    private EditText mEdt;
    private HorizontalWheelView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdt = findViewById(R.id.edt);
        mView = findViewById(R.id.horizontalWheelView);
    }

    public void click(View view){
        int position = Integer.parseInt(mEdt.getText().toString());
        mView.scrollToPosition(position);
    }

}