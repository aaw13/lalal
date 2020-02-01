package com.example.janet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.janet.models.Message;

public class messageView extends View {
    public messageView(Context context) {
        super(context);
    }

    public messageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public messageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public messageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMessage(Message message)
    {

    }
}
