package me.franciscoigor.habits.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.ItemHolder;
import me.franciscoigor.habits.base.ListFragment;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;


public class ActionListFragment extends ListFragment {

    private String viewModel = "taskActions";
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static ListFragment newInstance(String param1) {
        ActionListFragment fragment = new ActionListFragment();
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
    public ItemAdapter newAdapter() {
        return new ItemAdapter(viewModel);
    }

    @Override
    protected void setupAdapter(ItemAdapter adapter) {
        String[] columns = { "tasks._id", "tasks.title"};
        ArrayList<DataModel> filtered=adapter.findItems(
                "tasks left join taskActions ON taskActions.task_id=tasks._id",
                columns,
                "taskActions.task_id is null and tasks.enabled = '1'",
                null);
        for (DataModel item: filtered
                ) {
            adapter.addItem(new TaskActionModel(item.getIntValue("_id"),  item.getStringValue(TaskActionModel.FIELD_TITLE),0,false));
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
        private static final int REQUEST_DATE = 0;

        public TaskItemHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    System.out.println("DIALOG "+model);
                    TaskDialogFragment dialog = TaskDialogFragment.newInstance(model);
                    dialog.setTargetFragment(ActionListFragment.this, REQUEST_DATE);

                    dialog.show(manager, TaskDialogFragment.DIALOG_ITEM);
                }
            });
            mTextName = view.findViewById(R.id.task_list_item_title);
            mTextDescription = view.findViewById(R.id.task_list_item_description);
            mTextCategory = view.findViewById(R.id.task_list_item_category);
            mDelete = view.findViewById(R.id.task_list_item_delete);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapter().deleteItem(model);
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


        }

        @Override
        public void bind(DataModel model) {
            this.model=model;
            System.out.println(model);
            mTextName.setText(model.getStringValue(TaskActionModel.FIELD_TITLE));
            mTextDescription.setText(model.getStringValue(TaskActionModel.FIELD_TIME_MINUTES) + " minutes");

            if (model.getBooleanValue(TaskActionModel.FIELD_FINISHED)){
                mIcon.setImageResource(android.R.drawable.checkbox_on_background);
            }else{
                mIcon.setImageResource(android.R.drawable.checkbox_off_background);
            }

        }
    }

    @Override
    protected int getItemHolderLayout() {
        return R.layout.task_action_list_item;
    }
}
