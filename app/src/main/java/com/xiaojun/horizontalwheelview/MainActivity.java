package com.xiaojun.horizontalwheelview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojun.horizontalwheelview.view.HorizontalWheelView;
import com.xiaojun.horizontalwheelview.view.OnProgressChangeListener;

public class MainActivity extends AppCompatActivity {

    private EditText mEdt;
    private HorizontalWheelView mView;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdt = findViewById(R.id.edt);
        mView = findViewById(R.id.horizontalWheelView);
        mTv = findViewById(R.id.tv);
        mView.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position, boolean fromUser) {

            }

            @Override
            public void onProgressSelected(int position) {
                mTv.setText(position+"");
            }
        });
    }

    public void click(View view){
        int position = Integer.parseInt(mEdt.getText().toString());
        mView.scrollToPosition(position);
    }

}