package com.nof.pwv;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    private PassWordView passView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passView = findViewById(R.id.pwView);
    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            int[] position = {0,0};
//            passView.getLocationInWindow(position);
//            int left = position[0];
//            int leftTop = position[1];
//            int right = position[0] + passView.getWidth();
//            int rightBottom = position[1] + passView.getHeight();
//            if(ev.getRawX() > left && ev.getRawX() < right &&
//                    ev.getRawY() > leftTop && ev.getRawY() < rightBottom){
//                super.dispatchTouchEvent(ev);
//            }else{
//                passView.clearFocus();
//                return super.dispatchTouchEvent(ev);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
