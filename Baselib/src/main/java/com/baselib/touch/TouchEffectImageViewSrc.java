package com.baselib.touch;

/**
 * Created by admin on 20/3/2015.
 */
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class TouchEffectImageViewSrc implements OnTouchListener
{

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        try {
            ImageView iv = (ImageView) v;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                iv.setAlpha(100);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                iv.setAlpha(255);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
