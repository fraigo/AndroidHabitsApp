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
        String todayDate = DateUtils.format(DateUtils.today());
        if (todayDate.equals(currentDate)){
            TaskActionModel.createTaskActions(current, adapter);
        }

    }

    @Override
    protected ItemHolder createItemHolder(View view) {
        return new TaskItemHolder(view);
    }

    private class TaskItemHolder extends ItemHolder{

        TextView mTextName, mTextDescription, mTextDetail, mTextCategory;
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
            mTextDetail = view.findViewById(R.id.task_list_item_frequency);
            mTextCategory = view.findViewById(R.id.task_list_item_category1);
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
            System.out.println(model);
            String frequencyName = TaskModel.getString(getActivity(), model.getStringValue(TaskActionModel.FIELD_FREQUENCY));
            mTextName.setText(model.getStringValue(TaskActionModel.FIELD_TITLE));
            mTextDescription.setText(model.getStringValue(TaskActionModel.FIELD_TIME_MINUTES) + " " + getString(R.string.minutes));
            mTextDetail.setText(frequencyName + " @ " +model.getStringValue(TaskActionModel.FIELD_TIME) + " " +
                    " " + model.getStringValue(TaskActionModel.FIELD_DATE));
            String categoryName = TaskModel.getCategoryName(getActivity(),model.getStringValue(TaskActionModel.FIELD_CATEGORY));
            mTextCategory.setText(categoryName);
            mTextCategory.setBackgroundColor(TaskModel.getColor(model));

            if (model.getBooleanValue(TaskActionModel.FIELD_FINISHED)){
                mIcon.setImageResource(R.drawable.checkbox_on);
            }else{
                mIcon.setImageResource(R.drawable.checkbox_off);
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
