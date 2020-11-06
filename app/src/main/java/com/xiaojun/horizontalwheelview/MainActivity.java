package com.xiaojun.horizontalwheelview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private TextView mTvDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdt = findViewById(R.id.edt);
        mView = findViewById(R.id.horizontalWheelView);
        mView2 = findViewById(R.id.horizontalWheelView2);
        mTv = findViewById(R.id.tv);
        mTvRight = findViewById(R.id.tvRight);
        mTvDelay = findViewById(R.id.tvred);
        mView.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position, boolean fromUser) {
                Log.e("xiaojun","onProgressChange:"+position);
                mTvRight.setText(position + "");
            }

            @Override
            public void onProgressSelected(int position,int lastPosition) {
                Log.e("xiaojun","onProgressSelected:"+position+",lastPosition="+lastPosition);
                mTv.setText(position + "");
            }

            @Override
            public void onProgressDelayChange(int position) {
                mTvDelay.setText(""+position);
            }
        });
        mView2.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position, boolean fromUser) {
                mTvRight.setText(position + "");
            }

            @Override
            public void onProgressSelected(int position,int lastPosition) {
                mTv.setText(position + "");
            }

            @Override
            public void onProgressDelayChange(int position) {

            }
        });

        mView.setDatas(100,3,TYPE.CONTINUED);

//        mView2.setDatas(100, 50, TYPE.DISCRETE);
    }


    private TYPE mType;

    public void click(View view) {
//        mView.setDatas(50, 0, TYPE.DISCRETE);
        int position = Integer.parseInt(mEdt.getText().toString());
        mView.scrollToPosition(position,false);
//        if (mType == TYPE.DISCRETE){
//            mView.setDatas(50, 25, TYPE.CONTINUED);
//            mType = TYPE.CONTINUED;
//        }
//        else{
//            mView.setDatas(50, 0, TYPE.DISCRETE);
//            mType = TYPE.DISCRETE;
//        }

    }

}