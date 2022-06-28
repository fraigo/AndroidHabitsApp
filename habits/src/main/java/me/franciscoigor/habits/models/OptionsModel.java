package me.franciscoigor.habits.models;


import java.util.ArrayList;

import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;

/**
 * Data model for options.
 *
 * Table name: options
 *
 * Objective: To save application settings
 */
public class OptionsModel extends DataModel {

    public static final String TABLE_NAME = "options";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_VALUE = "value";

    public static final String OPT_SHOW_DELETED = "show_deleted";
    public static final String OPT_LOCALE = "app_locale";
    public static final String OPT_STARTVIEW = "app_startview";
    public static final String OPT_FIRST_RUN = "app_firstrun";

    public OptionsModel(){
        this(null, null);
    }

    public OptionsModel(String name, String value){
        super(TABLE_NAME);
        addField(FIELD_NAME);
        addField(FIELD_VALUE);

        setValue(FIELD_NAME, name);
        setValue(FIELD_VALUE, value);

    }

    public static String getOptionValue(String name){
        DataModel opt= getOption(name);
        if (opt == null) return null;
        return opt.getStringValue(FIELD_VALUE);
    }

    public static DataModel getOption(String name){
        ArrayList<DataModel> items = DatabaseHelper.getItems(TABLE_NAME,
                null,
                String.format(" %s = '%s' ",FIELD_NAME, name),
                null);
        if (items.size()==0){
            return null;
        }
        return items.get(0);
    }

    public static DataModel setOption(String name, String value){
        DataModel opt=getOption(name);
        if (opt == null){
            opt = new OptionsModel(name, value);
            DatabaseHelper.insert(opt);
        }else{
            opt.setValue(FIELD_VALUE, value);
            DatabaseHelper.update(opt);
        }
        return opt;
    }
}
