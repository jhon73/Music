package com.example.yudiz.wheel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class WheelPickerDialog extends DialogFragment {
    private TextView tvTitle;
    private WheelView wheelhours;
    public ArrayWheelAdapter<String> mAdapter;
    private OnDialogListener mListener;
    private String title = "";

    public WheelPickerDialog() {
    }


    @SuppressLint("ValidFragment")
    public WheelPickerDialog(Context context, OnDialogListener mListener) {
        mAdapter = new ArrayWheelAdapter<>(context, R.layout.wheel_picker_row, R.id.wheel_picker_row_tv, new ArrayList<String>());
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog;
        alertDialog = new Dialog(getActivity(), R.style.Dialog);//,R.style.PickerTheme
//        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(LayoutInflater.from(alertDialog.getContext()).inflate(R.layout.picker_view, null));

        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        alertDialog.show();
        return alertDialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvTitle = (TextView) getDialog().findViewById(R.id.tvTitle);
        tvTitle.setText(title);


        wheelhours = (WheelView) getDialog().findViewById(R.id.businessName);
        wheelhours.setViewAdapter(mAdapter);

        getDialog().findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogOk("" + mAdapter.getItemText(wheelhours.getCurrentItem()), wheelhours.getCurrentItem());
                WheelPickerDialog.this.dismiss();
            }
        });
    }

    public void setList(List<String> mList) {
        if (mAdapter == null) {
            mAdapter = new ArrayWheelAdapter<>(getActivity(), new ArrayList<String>());
            if (wheelhours != null) {
                wheelhours.setViewAdapter(mAdapter);
            }
        }
        mAdapter.setList(mList);
    }


    public void setSelected(final int position) {
        if (wheelhours != null && mAdapter != null && position >= 0 && position < mAdapter.getItemsCount()) {
            wheelhours.post(new Runnable() {
                @Override
                public void run() {
                    wheelhours.setCurrentItem(position, true);
                }
            });
        }
    }


    public void setTitle(String title) {
        this.title = title;
        if (tvTitle != null)
            tvTitle.setText(title);
    }


    public interface OnDialogListener {
        void onDialogOk(String result, int position);
    }
}
