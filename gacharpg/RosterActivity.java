package com.zybooks.gacharpg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RosterActivity extends AppCompatActivity {
    private GachaDB db;
    private String[] names;
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        db = GachaDB.getInstance(this);
        names = db.getNames();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.spinnerNames);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        for(int i = 0; i < names.length; i++) {
            spinnerAdapter.add(names[i]);
        }
        spinnerAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = (String)parent.getItemAtPosition(position);
                updateFragment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                db.resetDB();
                finish();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    public void updateFragment() {
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction trx = mgr.beginTransaction();
        String[] values = db.getStats(selection);

        Bundle bundle = new Bundle();
        bundle.putString("id", values[0]);
        bundle.putString("name", selection);
        bundle.putString("element", values[1]);
        bundle.putString("role", values[2]);
        bundle.putString("level", values[3]);

        DetailsFragment df = new DetailsFragment();
        df.setArguments(bundle);
        trx.replace(R.id.fragment_container, df);
        trx.commit();
    }
}
