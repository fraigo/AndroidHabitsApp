package me.franciscoigor.habits.base;

import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class DataModel {
    private String name;
    private ArrayList<String> fieldNames;
    private HashMap<String,String> values;
    public static final String FIELD_UUID = "uuid";

    public DataModel(String name){
        this.name =name;
        this.fieldNames = new ArrayList<String>();
        addField(FIELD_UUID);
        this.values = new HashMap<String,String>();
        setValue(FIELD_UUID, UUID.randomUUID().toString());


    }


    public void create(SQLiteDatabase db) {
        String sqlCreate=String.format("CREATE TABLE %s (", name);
        sqlCreate += "_id integer primary key autoincrement";
        for (String field:fieldNames) {
            sqlCreate += String.format(" , %s",field);
        }
        sqlCreate += " )";
        System.out.println("SQL "+sqlCreate);
        db.execSQL(sqlCreate);
    }

    public void createField(SQLiteDatabase db, String field) {
        String sqlCreate=String.format("ALTER TABLE %s ADD FIELD ", name);
        sqlCreate += field;
        System.out.println("SQL "+sqlCreate);
        db.execSQL(sqlCreate);
    }


    public HashMap<String, String> getValues() {
        return values;
    }

    public void addField(String name){
        fieldNames.add(name);
    }

    public void setValue(String field, String value) {
        values.put(field,value);
    }

    public void setValue(String field, boolean value) {
        values.put(field,value ? "1" : "0");
    }

    public void setValue(String field, int value) {
        values.put(field,Integer.toString(value) );
    }

    public void setValue(String field, Date value) {
        values.put(field, DateUtils.format(value));
    }

    public String getStringValue(String key){
        return values.get(key);
    }

    public boolean getBooleanValue(String key){
        return "1".equals(values.get(key));
    }

    public int getIntValue(String key){
        if (values.get(key) == null ) return 0;
        return Integer.parseInt(values.get(key));
    }

    public Date getDateValue(String key){
        if (values.get(key) == null ) return null;
        try {
            return DateUtils.parse(values.get(key));
        } catch (ParseException e) {
            return null;
        }
    }

    public String getUUID() {
        return getStringValue(FIELD_UUID);
    }

    public String getModelName() {
        return name;
    }

    @Override
    public String toString() {
        return "DataModel." +
                name + " : " +
                //" {" + fieldNames + "} "
                " (" + values +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel dataModel = (DataModel) o;
        return Objects.equals(name, dataModel.name) &&
                Objects.equals(getUUID(), dataModel.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getUUID());
    }


    public ArrayList<String> getFieldNames() {
        return fieldNames;
    }

    public static ArrayList<DataModel> getItems(String table){
        return DatabaseHelper.getAll(table);
    }
}
