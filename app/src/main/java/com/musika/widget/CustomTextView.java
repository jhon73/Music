package com.musika.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.musika.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Yudiz on 01/07/16.
 */
public class CustomTextView extends com.rey.material.widget.TextView {

    private static Map<String, Typeface> mTypefaces;
    private Spannable spanRange = null;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

                array.recycle();
            }

        }
    }

    public void setText(CharSequence text, String bold) {


        String newString = new String(text + "");

        int startSpan = 0, endSpan = 0;
        if (spanRange == null)
            spanRange = new SpannableString(newString);

        while (true) {
            startSpan = newString.indexOf(bold, endSpan);

            if (startSpan < 0)
                break;
            endSpan = startSpan + bold.length();
            spanRange.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        this.setText(spanRange);
    }

    public void setText(String message) {
        if (message == null)
            message = "";
        super.setText(message);
    }

    public void setCapitalWord(String text) {
        if (text == null)
            text = "";
        String[] strArray = text.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        super.setText(builder);
    }
}

