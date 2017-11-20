package com.nof.pwv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class PassWordView extends LinearLayout{

    private Context mContext;
    private List<Integer> mPassList = new ArrayList<>();
    private int mPassLength = 0;
    private int maxLength;

    private InputMethodManager imm = (InputMethodManager) getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);

    public PassWordView(Context context) {
        this(context,null);
    }

    public PassWordView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PassWordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PassWordView);
        int inputColor = ta.getColor(R.styleable.PassWordView_inputColor, Color.WHITE);
        int noInputColor = ta.getColor(R.styleable.PassWordView_noInputColor, Color.BLACK);
        int drawShapeType = ta.getInt(R.styleable.PassWordView_drawShapeType, 1);
        int drawPassType = ta.getInt(R.styleable.PassWordView_drawPassType, 1);
        maxLength = ta.getInt(R.styleable.PassWordView_maxLength, 6);
        int itemWidth = (int) ta.getDimension(R.styleable.PassWordView_itemWidth, 30);
        int itemSpace = (int) ta.getDimension(R.styleable.PassWordView_itemSpace, 1);
        ta.recycle();
        for (int i = 0; i < maxLength; i++) {
            PassWordItemView passWordView = new PassWordItemView(context);
            LayoutParams params = new LayoutParams(Util.dp2px(itemWidth),
                    Util.dp2px(itemWidth));
            if(i != 0){
                params.setMargins(Util.dp2px(itemSpace),0,0,0);
            }
            passWordView.initParams(inputColor,noInputColor,drawShapeType,drawPassType,"");
            addView(passWordView,params);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
                imm.showSoftInput(PassWordView.this, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode >= KeyEvent.KEYCODE_0 &&
                            keyCode <= KeyEvent.KEYCODE_9){
                        if(mPassLength < maxLength){
                            changeItemState(mPassLength,2,keyCode-7+"");
                            mPassList.add(keyCode - 7);
                            mPassLength = mPassList.size();
                            changeItemState(mPassLength,1,"");
                            return true;
                        }
                        Toast.makeText(mContext,"密码最多"+maxLength+"位",Toast.LENGTH_SHORT).show();
                    }
                    if(keyCode == KeyEvent.KEYCODE_DEL){
                        if(mPassLength > 0){
                            changeItemState(mPassLength,0,"");
                            mPassLength --;
                            mPassList.remove(mPassLength);
                            changeItemState(mPassLength,1,"");
                        }else{
                            Toast.makeText(mContext,"请输入密码",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    imm.hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("onFocusChange:"+hasFocus);
                changeItemState(mPassLength,hasFocus ? 1 : 0,"");
                if(!hasFocus){
                    imm.hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }else{
                    imm.showSoftInput(PassWordView.this, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void changeItemState(int position, int state, String text){
        PassWordItemView passWordItemView = (PassWordItemView) getChildAt(position);
        if(passWordItemView !=null){
            passWordItemView.changeState(state,text);
        }
    }
}
