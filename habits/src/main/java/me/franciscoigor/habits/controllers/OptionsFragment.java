package me.franciscoigor.habits.controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.models.OptionsModel;
import me.franciscoigor.habits.models.TaskModel;


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
        DataModel optTmp;
        optTmp=OptionsModel.getOption(OptionsModel.OPT_SHOW_DELETED);
        if (optTmp == null){
            optTmp = OptionsModel.setOption(OptionsModel.OPT_SHOW_DELETED, "0");
        }
        final DataModel optDeleted = optTmp;
        View view =  inflater.inflate(R.layout.fragment_options, container, false);
        Switch switch1 = view.findViewById(R.id.option_view_deleted);
        switch1.setChecked(optDeleted.getBooleanValue(OptionsModel.FIELD_VALUE));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                optDeleted.setValue(OptionsModel.FIELD_VALUE,isChecked);
                DatabaseHelper.update(optDeleted);
            }
        });

        final String[] languages = {
                "en",
                "es",
                "tr"
        };
        final String[] languageNames = {
                "English",
                "Spanish (Español)",
                "Turkish (Türkçe)"
        };

        optTmp =OptionsModel.getOption(OptionsModel.OPT_LOCALE);
        if (optTmp == null){
            optTmp = OptionsModel.setOption(OptionsModel.OPT_LOCALE, "en");
        }

        final DataModel optLocale = optTmp;
        final Spinner localeSelection = view.findViewById(R.id.option_locale);
        localeSelection.setAdapter(new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, R.id.item_data, languageNames ));
        localeSelection.setSelection(Arrays.asList(languages).indexOf(optLocale.getStringValue(OptionsModel.FIELD_VALUE)));
        localeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                optLocale.setValue(OptionsModel.FIELD_VALUE, languages[position]);
                DatabaseHelper.update(optLocale);
                MainActivity.setLocale(getActivity(), languages[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

}
