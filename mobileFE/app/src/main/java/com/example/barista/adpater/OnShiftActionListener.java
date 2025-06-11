package com.example.barista.adpater;

import android.view.View;

import com.example.barista.data.WorkShift;

public interface OnShiftActionListener {
    void onClockIn(String shiftId, int position);
    void onClockOut(String shiftId, int position);
    void onMoreOptionsClicked(WorkShift shift, View anchorView);
}
