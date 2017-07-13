package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by wiliiamwang on 29/06/2017.
 */

public class OmniEditText extends AppCompatEditText {

    public interface OnOmniEditTextActionListener {
        void onSoftKeyboardDismiss();
        void onTouch(MotionEvent event);
    }

    private Context mContext;
    private OnOmniEditTextActionListener mListener;

    public OmniEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        this.setFocusable(false);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OmniEditText.this.setFocusable(true);
                OmniEditText.this.setFocusableInTouchMode(true);
                mListener.onTouch(event);
                return false;
            }
        });

        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    OmniEditText.this.setFocusable(false);

                    mListener.onSoftKeyboardDismiss();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);

            this.setFocusable(false);

            mListener.onSoftKeyboardDismiss();

            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setOnOmniEditTextActionListener(OnOmniEditTextActionListener listener) {
        mListener = listener;
    }
}
