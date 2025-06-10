package com.example.barista.adpater;

public interface OnShiftActionListener {
    void onClockIn(String shiftId, int position);
    void onClockOut(String shiftId, int position);
}
