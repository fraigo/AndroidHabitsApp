package me.franciscoigor.habits.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.models.TaskActionModel;

public class StatsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance(String param1) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Gathering stats...");

        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        String condition;
        int stat;
        TextView label;

        condition =  null;
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_total);
        label.setText(Integer.toString(stat));


        condition =  String.format(" %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 1);
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_completed);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 0);
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_incompleted);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%s'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()));;
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_total_today);
        label.setText(Integer.toString(stat));


        condition =  String.format(" %s.%s = '%s' and %s.%s = '%d' ", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()), TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 1);
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_completed_today);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%s' and %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()), TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 0);
        stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition, null).size();
        label = view.findViewById(R.id.stats_incompleted_today);
        label.setText(Integer.toString(stat));

        return view;
    }

}
