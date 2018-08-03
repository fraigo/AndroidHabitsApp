package me.franciscoigor.habits.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;

/**
 * StatsFragment
 *
 * Controller for the Statistics view
 * To get current task's statistics and to show calculated values
 */
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

        String globalCondition = String.format(" AND %s = '0' ",TaskActionModel.FIELD_DELETED);
        String condition;
        int stat;
        TextView label;

        condition =  "1";
        int total = stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_total);
        label.setText(Integer.toString(stat));


        condition =  String.format(" %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 1);
        int completed = stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_completed);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 0);
        int incompleted = stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_incompleted);
        label.setText(Integer.toString(stat));

        TextView bar1 = view.findViewById(R.id.stats_progress);
        if (total == 0 ) {
            total = 1;
        }
        bar1.setText((completed * 100 / total) + "%");

        condition =  String.format(" %s.%s = '%s'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()));;
        int totalToday = stat =  DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_total_today);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%s' and %s.%s = '%d' ", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()), TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 1);
        int completedToday = stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_completed_today);
        label.setText(Integer.toString(stat));

        condition =  String.format(" %s.%s = '%s' and %s.%s = '%d'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_DATE, DateUtils.format(DateUtils.today()), TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 0);
        int incompletedToday = stat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
        label = view.findViewById(R.id.stats_incompleted_today);
        label.setText(Integer.toString(stat));

        TextView bar2 = view.findViewById(R.id.stats_progress_today);
        if (totalToday == 0 ) {
            totalToday = 1;
        }
        bar2.setText((completedToday * 100 / totalToday) + "%");

        String[] categories = TaskModel.CATEGORIES;
        for (int i = 0; i < categories.length; i++) {
            condition =  String.format(" %s.%s = '%s'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_CATEGORY, categories[i]);
            int totalCat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
            int resID = getResources().getIdentifier( String.format("stats_%s_total" , categories[i]), "id", getActivity().getPackageName());
            label = view.findViewById(resID);

            condition =  String.format(" %s.%s = '%d' and %s.%s = '%s'", TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_FINISHED, 1, TaskActionModel.TABLE_NAME, TaskActionModel.FIELD_CATEGORY, categories[i]);
            int finishedCat = DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, condition + globalCondition, null).size();
            label.setText(String.format("%d / %d", finishedCat, totalCat));
        }

        return view;
    }

}
