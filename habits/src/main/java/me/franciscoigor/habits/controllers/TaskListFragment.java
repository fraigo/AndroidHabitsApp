package me.franciscoigor.habits.controllers;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.DialogHelper;
import me.franciscoigor.habits.base.ItemHolder;
import me.franciscoigor.habits.base.ListFragment;
import me.franciscoigor.habits.models.TaskModel;

/**
 * TaskListFragment
 *
 * Controller to show the task management view
 */
public class TaskListFragment extends ListFragment {

    private String viewModel = "tasks";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static ListFragment newInstance(String param1) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void createFragment(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public ListFragment.ItemAdapter newAdapter() {
        return new ListFragment.ItemAdapter(viewModel);
    }

    @Override
    protected void setupAdapter(ListFragment.ItemAdapter adapter) {
        ArrayList<DataModel> list=adapter.loadItems(viewModel);
        if (list.size()==0){
            adapter.addItem(new TaskModel("Read a book",
                    "Keep my mind busy",
                    TaskModel.FREQUENCY_DAILY,
                    null,
                    TaskModel.CATEGORY_STUDY,
                    "06:30",
                    true));
            adapter.addItem(new TaskModel("Go to work",
                    "Office work",
                    TaskModel.FREQUENCY_WEEKDAYS,
                    null,
                    TaskModel.CATEGORY_WORK,
                    "08:00",
                    true));
            adapter.addItem(new TaskModel("Go to Gym",
                    "Do some body work",
                    TaskModel.FREQUENCY_WEEKLY,
                    DateUtils.WEEKDAY_SATURDAY,
                    TaskModel.CATEGORY_HEALTH,
                    "09:00",
                    true));
            adapter.addItem(new TaskModel("Laundry",
                    "Wash my clothes",
                    TaskModel.FREQUENCY_WEEKLY,
                    DateUtils.WEEKDAY_SUNDAY,
                    TaskModel.CATEGORY_HOME,
                    "09:00",
                    true));
            adapter.addItem(new TaskModel("Have some fun",
                    "Watch a movie, Play with friends",
                    TaskModel.FREQUENCY_WEEKLY,
                    DateUtils.WEEKDAY_FRIDAY,
                    TaskModel.CATEGORY_ENTERTAINMENT,
                    "21:00",
                    true));
            adapter.addItem(new TaskModel("Family Activities",
                    "Go camping, visit family",
                    TaskModel.FREQUENCY_WEEKENDS,
                    null,
                    TaskModel.CATEGORY_ENTERTAINMENT,
                    "16:00",
                    true));
            adapter.addItem(new TaskModel("Pay bills",
                    "No debt",
                    TaskModel.FREQUENCY_MONTHLY,
                    "1",
                    TaskModel.CATEGORY_FINANCIAL,
                    "09:00",
                    true));
        }
        ArrayList<DataModel> filtered=adapter.findItems(viewModel, null, String.format("%s <> '%s'", TaskModel.FIELD_ENABLED,"1"),new String[0]);
    }

    @Override
    protected ItemHolder createItemHolder(View view) {
        return new TaskItemHolder(view);
    }

    private class TaskItemHolder extends ItemHolder{

        TextView mTextName, mTextDescription, mTextFrequency, mTextCategory;
        ImageView mDelete;
        Switch mSwitch;
        DataModel model;
        private static final int REQUEST_DATE = 0;

        public TaskItemHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    System.out.println("DIALOG "+model);
                    TaskDialogFragment dialog = TaskDialogFragment.newInstance(model);
                    dialog.setTargetFragment(TaskListFragment.this, REQUEST_DATE);

                    dialog.show(manager, TaskDialogFragment.DIALOG_ITEM);
                }
            });
            mTextName = view.findViewById(R.id.task_list_item_title);
            mTextDescription = view.findViewById(R.id.task_list_item_description);
            mTextFrequency = view.findViewById(R.id.task_list_item_frequency);
            mTextCategory = view.findViewById(R.id.task_list_item_category);
            mDelete = view.findViewById(R.id.task_list_item_delete);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper.confirmDialog(getActivity(), getActivity().getString(R.string.are_you_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getAdapter().deleteItem(model);
                        }
                    });

                }
            });
            mSwitch = view.findViewById(R.id.task_list_item_enabled);
            mSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setValue(TaskModel.FIELD_ENABLED, !model.getBooleanValue(TaskModel.FIELD_ENABLED));
                    getAdapter().updateItem(model);
                }
            });

        }

        @Override
        public void bind(DataModel model) {
            this.model=model;
            System.out.println(model);
            Boolean active = model.getBooleanValue(TaskModel.FIELD_ENABLED);
            mTextName.setText(model.getStringValue(TaskModel.FIELD_TITLE));
            if (!active){
                mTextName.setTextColor(Color.LTGRAY);
            }else{
                mTextName.setTextColor(Color.GRAY);
            }
            mTextDescription.setText(model.getStringValue(TaskModel.FIELD_DESCRIPTION));
            String category = model.getStringValue(TaskModel.FIELD_FREQUENCY);
            String categoryName = TaskModel.getCategoryName(getActivity(),model.getStringValue(TaskModel.FIELD_CATEGORY));
            mTextCategory.setText(categoryName);
            mTextCategory.setBackgroundColor(TaskModel.getColor(model));
            String frequencyText = TaskModel.getString(getActivity(), category);
            String subcategory = model.getStringValue(TaskModel.FIELD_FREQ_DETAIL);

            String time = model.getStringValue(TaskModel.FIELD_TIME);
            mTextFrequency.setText(frequencyText);
            mTextFrequency.setTextColor(TaskModel.getColor(model));
            if (category.equals(TaskModel.FREQUENCY_DAILY)){
                mTextFrequency.setText(frequencyText);
            }
            if (category.equals(TaskModel.FREQUENCY_WEEKLY)){
                String dayName = DateUtils.getWeekDayName(getActivity(), subcategory);
                mTextFrequency.setText(frequencyText + " : " + dayName);
            }
            if (category.equals(TaskModel.FREQUENCY_MONTHLY)){
                mTextFrequency.setText(frequencyText + " / " + subcategory);
            }
            mTextFrequency.setText(mTextFrequency.getText()+" @ " + time);
            mSwitch.setChecked(model.getBooleanValue(TaskModel.FIELD_ENABLED));

        }
    }

    @Override
    protected int getItemHolderLayout() {
        return R.layout.task_list_item;
    }
}
