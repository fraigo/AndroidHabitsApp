package me.franciscoigor.habits.controllers;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Arrays;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.ItemDialogFragment;
import me.franciscoigor.habits.base.ListFragment;
import me.franciscoigor.habits.base.StringHelper;
import me.franciscoigor.habits.base.TimeDataPicker;
import me.franciscoigor.habits.models.TaskModel;


public class TaskDialogFragment extends ItemDialogFragment {

    public static final String DIALOG_ITEM = "item";
    private DataModel item;

    public static TaskDialogFragment newInstance(DataModel item) {
        Bundle args = new Bundle();
        TaskDialogFragment fragment = new TaskDialogFragment();
        fragment.setItem(item);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.task_dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        enableOkButton();
    }

    private void enableOkButton(){
        AlertDialog dialog=(AlertDialog)this.getDialog();

        String title=item.getStringValue(TaskModel.FIELD_TITLE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(title!= null && title.trim().length()>0);
    }

    @Override
    protected void bindDialog(final DataModel item, View v) {

        this.item = item;

        final Button taskTime = v.findViewById(R.id.task_dialog_time);
        taskTime.setText(item.getStringValue(TaskModel.FIELD_TIME));
        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final TimeDataPicker newFragment = new TimeDataPicker();
                    newFragment.setup(taskTime.getText().toString(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            taskTime.setText(newFragment.getSelectedTime());
                            item.setValue(TaskModel.FIELD_TIME,newFragment.getSelectedTime());
                        }
                    });
                    newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        final String[] monthDays = new String[31];
        for (int i = 0; i < 31; i++) {
            monthDays[i] = Integer.toString(i+1);
        }

        final String[] days= DateUtils.WEEKDAYS;
        final String[] dayNames = DateUtils.getWeekDays(getActivity());
        final String[] categories = TaskModel.CATEGORIES;
        final String[] categoryNames = TaskModel.getCategoryNames(getActivity());

        final String[] empty = { "" };

        final Spinner taskCategory = v.findViewById(R.id.task_dialog_category);
        String category = item.getStringValue(TaskModel.FIELD_CATEGORY);
        taskCategory.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, categoryNames ));
        taskCategory.setSelection(Arrays.asList(categories).indexOf(category));
        taskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setValue(TaskModel.FIELD_CATEGORY, categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner taskFreqDetail = v.findViewById(R.id.task_dialog_freqdetail);
        final int currentDayOfWeek= Arrays.asList(days).indexOf(item.getStringValue(TaskModel.FIELD_FREQ_DETAIL));
        final int currentDayOfMonth= Arrays.asList(monthDays).indexOf(item.getStringValue(TaskModel.FIELD_FREQ_DETAIL));
        taskFreqDetail.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, dayNames ));
        taskFreqDetail.setSelection(currentDayOfWeek);
        taskFreqDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (TaskModel.FREQUENCY_WEEKLY.equals(item.getStringValue(TaskModel.FIELD_FREQUENCY))){
                    item.setValue(TaskModel.FIELD_FREQ_DETAIL, days[position]);
                }
                if (TaskModel.FREQUENCY_MONTHLY.equals(item.getStringValue(TaskModel.FIELD_FREQUENCY))){
                    item.setValue(TaskModel.FIELD_FREQ_DETAIL, monthDays[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] localeItems= TaskModel.getFrequencyList(getActivity());
        final String[] items= TaskModel.FREQUENCIES;

        final Spinner taskFrequency = v.findViewById(R.id.task_dialog_frequency);
        taskFrequency.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, localeItems ));
        taskFrequency.setSelection(Arrays.asList(items).indexOf(item.getStringValue(TaskModel.FIELD_FREQUENCY)));
        taskFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setValue(TaskModel.FIELD_FREQUENCY, items[position]);
                if (items[position].equals(TaskModel.FREQUENCY_WEEKLY)) {
                    taskFreqDetail.setAdapter(new ArrayAdapter<String>(TaskDialogFragment.this.getContext(), R.layout.spinner_item, R.id.item_data, dayNames));
                    taskFreqDetail.setSelection(currentDayOfWeek);
                    item.setValue(TaskModel.FIELD_FREQ_DETAIL, currentDayOfWeek);
                    taskFreqDetail.setEnabled(true);
                } else if (items[position].equals(TaskModel.FREQUENCY_MONTHLY)){
                        taskFreqDetail.setAdapter(new ArrayAdapter<String>(TaskDialogFragment.this.getContext(), R.layout.spinner_item, R.id.item_data, monthDays ));
                        taskFreqDetail.setSelection(currentDayOfMonth);
                        item.setValue(TaskModel.FIELD_FREQ_DETAIL, currentDayOfMonth);
                        taskFreqDetail.setEnabled(true);
                }else{
                    taskFreqDetail.setAdapter(new ArrayAdapter<String>(TaskDialogFragment.this.getContext(), R.layout.spinner_item, R.id.item_data, empty ));
                    item.setValue(TaskModel.FIELD_FREQ_DETAIL, "");
                    taskFreqDetail.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final CheckBox checkBox= v.findViewById(R.id.task_dialog_enabled);
        checkBox.setChecked(item.getBooleanValue(TaskModel.FIELD_ENABLED));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setValue(TaskModel.FIELD_ENABLED, isChecked);
            }
        });


        TextView itemTitle=v.findViewById(R.id.task_dialog_title);
        itemTitle.setText(item.getStringValue(TaskModel.FIELD_TITLE));
        itemTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setValue(TaskModel.FIELD_TITLE, StringHelper.upperCaseFirst(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableOkButton();
            }
        });
        TextView itemDesc=v.findViewById(R.id.task_dialog_description);
        itemDesc.setText(item.getStringValue(TaskModel.FIELD_DESCRIPTION));
        itemDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.setValue(TaskModel.FIELD_DESCRIPTION,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected String getDialogTitle(DataModel item) {
        return getString(R.string.task);
    }

    @Override
    public void onDialogResult(DialogInterface dialog, DataModel item) {
        super.onDialogResult(dialog, item);
        if (MainActivity.getFragment() instanceof ListFragment){
            ListFragment list= (ListFragment) MainActivity.getFragment();
            ListFragment.ItemAdapter adapter= list.getAdapter();
            if (!adapter.findItem(item)){
                adapter.addItem(item);
                adapter.notifyItemInserted(adapter.getItemCount()-1);
            }else{
                adapter.updateItem(item);
            }
        }


    }
}
