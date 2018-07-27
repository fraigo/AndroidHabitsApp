package me.franciscoigor.habits.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.controllers.NotifierActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity {



    public SingleFragmentActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        // Restore instance state
        Storage.loadStorage(savedInstanceState, this);
        // Setup database
        addSchemas();
        DatabaseHelper.getDatabase(this.getApplicationContext());
        //Load fragment content
        loadFragment(createFragment(),false);
        //start notifier
        NotifierActivity.startNotifier(this.getApplicationContext());
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save instance
        Storage.saveStorage(savedInstanceState);
    }

    @Override
    protected void onStop() {
        Storage.saveStorage(null);
        super.onStop();
    }

    protected void loadFragment(Fragment newFragment, boolean replace){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment != null && replace) {
            fm.beginTransaction()
                    .remove(fragment)
                    .add(R.id.fragment_container, newFragment)
                    .commit();
        }
        if (fragment == null && !replace) {
            fragment = newFragment;
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    protected abstract Fragment createFragment();

    protected abstract void addSchemas();

}
