package me.franciscoigor.habits.controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.models.OptionsModel;


public class OptionsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";




    // TODO: Rename and change types of parameters
    private String mParam1;


    public OptionsFragment() {
        // Required empty public constructor
    }


    public static OptionsFragment newInstance(String param1) {
        OptionsFragment fragment = new OptionsFragment();
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
        // Inflate the layout for this fragment
        DataModel tmp=OptionsModel.getOption(OptionsModel.OPT_SHOW_DELETED);
        if (tmp == null){
            tmp = OptionsModel.setOption(OptionsModel.OPT_SHOW_DELETED, "0");
        }
        final DataModel opt = tmp;
        View view =  inflater.inflate(R.layout.fragment_options, container, false);
        Switch switch1 = view.findViewById(R.id.option_view_deleted);
        switch1.setChecked(opt.getBooleanValue(OptionsModel.FIELD_VALUE));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                opt.setValue(OptionsModel.FIELD_VALUE,isChecked);
                DatabaseHelper.update(opt);
            }
        });
        return view;
    }

}
