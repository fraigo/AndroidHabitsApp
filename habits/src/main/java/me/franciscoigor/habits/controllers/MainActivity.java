package me.franciscoigor.habits.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.LocaleHelper;
import me.franciscoigor.habits.base.NotificationHelper;
import me.franciscoigor.habits.base.SingleFragmentActivity;
import me.franciscoigor.habits.models.OptionsModel;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;

public class MainActivity extends SingleFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Fragment fragment;
    boolean fromNotification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent currentIntent = getIntent();
        String notificationParam = currentIntent.getStringExtra(NotificationHelper.EXTRA_PARAM_NOTIFICATION);
        if (notificationParam != null){
            System.out.println("Creating with param "+ notificationParam);
            fromNotification = true;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToView(R.id.nav_tasks);
                FragmentManager manager = getSupportFragmentManager();
                TaskDialogFragment dialog = TaskDialogFragment.newInstance(new TaskModel());
                dialog.show(manager,  TaskDialogFragment.DIALOG_ITEM);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void addSchemas() {
        DatabaseHelper.addSchema(new TaskModel());
        DatabaseHelper.addSchema(new TaskActionModel());
        DatabaseHelper.addSchema(new OptionsModel());

    }

    @Override
    protected Fragment createFragment() {
        fragment = TaskListFragment.newInstance(null);
        return fragment;
    }

    public static Fragment getFragment() {
        return fragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            goToView(R.id.nav_manage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        goToView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean goToView(int viewId){
        if (viewId == R.id.nav_today){
            setTitle(R.string.menu_today);
            loadFragment(fragment = ActionListFragment.newInstance(DateUtils.today().getTime()),true);
            OptionsModel.setOption(OptionsModel.OPT_STARTVIEW, Integer.toString(viewId));
            return true;
        }
        if (viewId == R.id.nav_yesterday){
            setTitle(R.string.menu_yesterday);
            loadFragment(fragment = ActionListFragment.newInstance(DateUtils.yesterday().getTime()),true);
            OptionsModel.setOption(OptionsModel.OPT_STARTVIEW, Integer.toString(viewId));
            return true;
        }
        if (viewId == R.id.nav_tasks) {
            setTitle(R.string.menu_tasks);
            loadFragment(fragment = TaskListFragment.newInstance(null), true);
            OptionsModel.setOption(OptionsModel.OPT_STARTVIEW, Integer.toString(viewId));
            return true;
        }
        if (viewId == R.id.nav_stats) {
            setTitle(R.string.menu_stats);
            loadFragment(fragment = StatsFragment.newInstance(null), true);
            OptionsModel.setOption(OptionsModel.OPT_STARTVIEW, Integer.toString(viewId));
            return true;
        }
        if (viewId == R.id.nav_manage) {
            setTitle(R.string.menu_manage);
            loadFragment(fragment = OptionsFragment.newInstance(null), true);
            OptionsModel.setOption(OptionsModel.OPT_STARTVIEW, Integer.toString(viewId));
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("Starting app");
        Intent currentIntent = getIntent();
        String notificationParam = currentIntent.getStringExtra(NotificationHelper.EXTRA_PARAM_NOTIFICATION);
        if (notificationParam != null){
            System.out.println("Starting with param "+ notificationParam);
            fromNotification = true;
            goToView(R.id.nav_today);
        }


        DataModel optTmp = OptionsModel.getOption(OptionsModel.OPT_LOCALE);
        if (optTmp != null){
            LocaleHelper.setLocale(this, optTmp.getStringValue(OptionsModel.FIELD_VALUE));
        }else{

        }

        DataModel optView = OptionsModel.getOption(OptionsModel.OPT_STARTVIEW);
        boolean viewOk = false;
        if (optView != null){
            int viewId = optView.getIntValue(OptionsModel.FIELD_VALUE);
            viewOk = goToView(viewId);
        }
        if (!viewOk){
            if (TaskActionModel.getItems(TaskActionModel.TABLE_NAME).size()==0){
                goToView(R.id.nav_tasks);
            }else{
                goToView(R.id.nav_today);
            }
        }

    }

}
