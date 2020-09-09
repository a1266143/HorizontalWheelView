package com.xiaojun.horizontalwheelview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojun.horizontalwheelview.view.HorizontalWheelView;
import com.xiaojun.horizontalwheelview.view.OnProgressChangeListener;
import com.xiaojun.horizontalwheelview.view.TYPE;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdt;
    private HorizontalWheelView mView, mView2;
    private TextView mTv;
    private TextView mTvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdt = findViewById(R.id.edt);
        mView = findViewById(R.id.horizontalWheelView);
        mView2 = findViewById(R.id.horizontalWheelView2);
        mTv = findViewById(R.id.tv);
        mTvRight = findViewById(R.id.tvRight);
        mView.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position, boolean fromUser) {
                mTvRight.setText(position + "");
            }

            @Override
            public void onProgressSelected(int position) {
                mTv.setText(position + "");
            }
        });
        mView2.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position, boolean fromUser) {
                mTvRight.setText(position + "");
            }

            @Override
            public void onProgressSelected(int position) {
                mTv.setText(position + "");
            }
        });
        mView.setDatas(100, 99, TYPE.CONTINUED);

        mView2.setDatas(100, 50, TYPE.DISCRETE);
    }


    private TYPE mType;

    public void click(View view) {
//        int position = Integer.parseInt(mEdt.getText().toString());
//        mView.scrollToPosition(position);
        if (mType == TYPE.DISCRETE){
            mView.setDatas(50, 25, TYPE.CONTINUED);
            mType = TYPE.CONTINUED;
        }
        else{
            mView.setDatas(50, 0, TYPE.DISCRETE);
            mType = TYPE.DISCRETE;
        }

    }

}