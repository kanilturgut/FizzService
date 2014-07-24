package com.kanilturgut.mylib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author   : kanilturgut
 * Date     : 13/06/14
 * Time     : 20:36
 */
public class MyTextView extends TextView{

    static Map<String, Typeface> myTypeFaces;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (myTypeFaces == null)
            myTypeFaces = new HashMap<String, Typeface>();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        if (typedArray != null) {
            final String assetPath = typedArray.getString(R.styleable.MyTextView_font);

            if (assetPath != null) {
                Typeface typeface = null;

                if (myTypeFaces.containsKey(assetPath)) {
                    typeface = myTypeFaces.get(assetPath);
                } else {
                    typeface = Typeface.createFromAsset(context.getAssets(), assetPath);
                    myTypeFaces.put(assetPath, typeface);
                }

                setTypeface(typeface);
            }
            typedArray.recycle();
        }
    }
}
