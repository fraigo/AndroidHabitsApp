 package me.franciscoigor.habits.base;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

 public class TimeDataPicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    Calendar selectedTime;
    TimePickerDialog.OnTimeSetListener listener;

    public void setup(String time, TimePickerDialog.OnTimeSetListener listener){
        this.listener = listener;
        selectedTime = Calendar.getInstance();
        String[] parts = time.split(":");
        selectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        selectedTime.set(Calendar.MINUTE,  Integer.parseInt(parts[1]));
    }

    public String getSelectedTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        return fmt.format(selectedTime.getTime());
    }

     @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour = selectedTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedTime.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedTime.set(Calendar.MINUTE, minute);

        listener.onTimeSet(timePicker, hourOfDay, minute);
    }
}