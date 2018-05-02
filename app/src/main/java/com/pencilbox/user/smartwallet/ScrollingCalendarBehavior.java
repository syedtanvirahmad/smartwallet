package com.pencilbox.user.smartwallet;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by User on 4/30/2018.
 */
@SuppressWarnings("unused")
public class ScrollingCalendarBehavior extends AppBarLayout.Behavior {
    public ScrollingCalendarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        //return super.onInterceptTouchEvent(parent, child, ev);
        return false;
    }
}
