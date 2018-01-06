/*
 * 
 */
package com.musika.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;


import com.musika.R;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class TextView.
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created
     * every time when they are referenced.
     */
    private static Map<String, Typeface> mTypefaces;


    public CustomEditText(final Context context) {
        this(context, null);
    }

    public CustomEditText(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setAttribute(context, attrs);
    }


    private void setAttribute(Context context, AttributeSet attrs) {
        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.textview);
        if (array != null) {
            final String typefaceAssetPath = array.getString(R.styleable.textview_font_type);

            if (typefaceAssetPath != null) {
                Typeface typeface;
                if (mTypefaces.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces.get(typefaceAssetPath);
                } else {
                    AssetManager assets = context.getAssets();
                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath);
                    mTypefaces.put(typefaceAssetPath, typeface);
                }
                setTypeface(typeface);
            }


            array.recycle();
        }
    }


}