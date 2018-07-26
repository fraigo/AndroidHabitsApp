package me.franciscoigor.habits.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.ItemHolder;
import me.franciscoigor.habits.base.ListFragment;
import me.franciscoigor.habits.models.OptionsModel;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;


public class ActionListFragment extends ListFragment {

    private String viewModel = "taskActions";
    private static final String ARG_DATE = "param1";
    private long mDate;

    public static ListFragment newInstance(Long datetime) {
        ActionListFragment fragment = new ActionListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, datetime);
        fragment.setArguments(args);

        return fragment;
    }

    public void createFragment(Bundle savedInstanceState) {
        mDate = DateUtils.today().getTime();
        if (getArguments() != null) {
            mDate = getArguments().getLong(ARG_DATE);
        }
    }

    @Override
    public ItemAdapter newAdapter() {
        return new ItemAdapter(viewModel);
    }

    @Override
    protected void setupAdapter(ItemAdapter adapter) {
        Date current = new Date(mDate);
        String currentDate = DateUtils.format(current);
        String currentWeekDay = DateUtils.weekDay(current);
        String todayDate = DateUtils.format(DateUtils.today());

        System.out.println("CURRENT DATE "+ currentDate+ " "+ currentWeekDay);
        String[] columns = { "tasks.*"};

        ArrayList<DataModel> filtered=adapter.findItems(
                String.format("tasks left join taskActions ON taskActions.task_id=tasks._id and taskActions.task_date = '%s'", currentDate),
                columns,
                String.format("taskActions.task_id is null and tasks.enabled = '1' and ( ( tasks.%s = '%s' ) OR ( tasks.%s = '%s' and  tasks.%s = '%s') OR ( tasks.%s = '%s' and  '%s' not in ('saturday','sunday') ) OR ( tasks.%s = '%s' and  '%s' in ('saturday','sunday') ))",
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_DAILY,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKLY,
                        TaskModel.FIELD_FREQ_DETAIL,
                        currentWeekDay,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKDAYS,
                        currentWeekDay,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKENDS,
                        currentWeekDay
                ),
                null);
        if (todayDate.equals(currentDate)){
            for (DataModel item: filtered) {
                adapter.addItem(new TaskActionModel(
                                item.getIntValue("_id"),
                                item.getStringValue(TaskModel.FIELD_TITLE),
                                current,
                                item.getStringValue(TaskModel.FIELD_TIME),
                                item.getStringValue(TaskModel.FIELD_FREQUENCY),
                                0,
                                false,
                                false
                        )
                );
            }
        }

    }

    @Override
    protected ItemHolder createItemHolder(View view) {
        return new TaskItemHolder(view);
    }

    private class TaskItemHolder extends ItemHolder{

        TextView mTextName, mTextDescription, mTextCategory;
        ImageView mDelete, mIcon;
        DataModel model;
        LinearLayout container;
        private static final int REQUEST_DATE = 0;

        public TaskItemHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Nothing to do for now
                }
            });
            mTextName = view.findViewById(R.id.task_list_item_title);
            mTextDescription = view.findViewById(R.id.task_list_item_description);
            mTextCategory = view.findViewById(R.id.task_list_item_category);
            mDelete = view.findViewById(R.id.task_list_item_delete);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setValue(TaskActionModel.FIELD_DELETED, !model.getBooleanValue(TaskActionModel.FIELD_DELETED));
                    getAdapter().updateItem(model);
                    //getAdapter().deleteItem(model);
                }
            });
            mIcon = view.findViewById(R.id.task_list_item_status);
            mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setValue(TaskActionModel.FIELD_FINISHED, !model.getBooleanValue(TaskActionModel.FIELD_FINISHED));
                    getAdapter().updateItem(model);
                }
            });

            container = view.findViewById(R.id.task_list_item_container);


        }

        @Override
        public void bind(DataModel model) {
            this.model=model;
            String categoryName = TaskModel.getString(getActivity(), model.getStringValue(TaskActionModel.FIELD_FREQUENCY));
            mTextName.setText(model.getStringValue(TaskActionModel.FIELD_TITLE));
            mTextDescription.setText(model.getStringValue(TaskActionModel.FIELD_TIME_MINUTES) + " " + getString(R.string.minutes));
            mTextCategory.setText(categoryName + " @ " +model.getStringValue(TaskActionModel.FIELD_TIME) + " " +
                    " " + model.getStringValue(TaskActionModel.FIELD_DATE));

            if (model.getBooleanValue(TaskActionModel.FIELD_FINISHED)){
                mIcon.setImageResource(android.R.drawable.checkbox_on_background);
            }else{
                mIcon.setImageResource(android.R.drawable.checkbox_off_background);
            }
            // set visibility
            container.setBackgroundColor(Color.WHITE);
            container.setVisibility(View.VISIBLE);
            mIcon.setVisibility(View.VISIBLE);
            if (model.getBooleanValue(TaskActionModel.FIELD_DELETED)){
                mIcon.setVisibility(View.INVISIBLE);
                if ("1".equals(OptionsModel.getOptionValue(OptionsModel.OPT_SHOW_DELETED))){
                    container.setBackgroundColor(Color.LTGRAY);
                }else{
                    container.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected int getItemHolderLayout() {
        return R.layout.task_action_list_item;
    }


    @Override
    public ArrayList<DataModel> getItems(String name) {
        String currentDate = DateUtils.format(new Date(mDate));
        return DatabaseHelper.getItems(name, null, String.format("task_date = '%s' ",currentDate), null, null, "task_time");
    }
}
