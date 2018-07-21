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



        final String[] days= DateUtils.WEEKDAYS;
        final String[] items= TaskModel.CATEGORIES;
        final String[] empty = { "" };

        final Spinner taskSubcategory = v.findViewById(R.id.task_dialog_subcategory);
        taskSubcategory.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, days ));
        taskSubcategory.setSelection(Arrays.asList(days).indexOf(item.getStringValue(TaskModel.FIELD_SUBCATEGORY)));
        taskSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setValue(TaskModel.FIELD_SUBCATEGORY, days[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Spinner taskCategory = v.findViewById(R.id.task_dialog_category);
        taskCategory.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, items ));
        taskCategory.setSelection(Arrays.asList(items).indexOf(item.getStringValue(TaskModel.FIELD_CATEGORY)));
        taskCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setValue(TaskModel.FIELD_CATEGORY, items[position]);
                if (items[position].equals(TaskModel.CATEGORY_WEEKLY)){
                    taskSubcategory.setAdapter(new ArrayAdapter<String>(TaskDialogFragment.this.getContext(), R.layout.spinner_item, R.id.item_data, days ));
                    taskSubcategory.setSelection(0);
                    item.setValue(TaskModel.FIELD_SUBCATEGORY, days[0]);
                    taskSubcategory.setEnabled(true);
                }else{
                    taskSubcategory.setAdapter(new ArrayAdapter<String>(TaskDialogFragment.this.getContext(), R.layout.spinner_item, R.id.item_data, empty ));
                    item.setValue(TaskModel.FIELD_SUBCATEGORY, "");
                    taskSubcategory.setEnabled(false);
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
                item.setValue(TaskModel.FIELD_TITLE,s.toString());
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
                NotifierActivity.notifyUser(this.getContext(), "Tasks", String.format("New task (%s) was created", item.getBooleanValue(TaskModel.FIELD_TITLE)));
            }else{
                adapter.updateItem(item);
            }
        }


    }
}
