package nl.food4bees.beedroid;

import android.os.Build;
import android.os.Bundle;

import android.app.Activity;

import android.view.View;

import android.content.Intent;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockActivity;

import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Menu;

import java.util.Arrays;
import java.util.ArrayList;

public final class PlantsNamesActivity extends SherlockActivity {
    private ListView mList;
    private TextView mEmpty;
    private ArrayAdapter<PlantCommonName> mListAdapter;

    private Activity mActivity;

    static final int PICK_PLANT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.plants_names);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mList = (ListView)findViewById(R.id.list);
        mEmpty = (TextView)findViewById(R.id.empty);

        mListAdapter = new ArrayAdapter<PlantCommonName>(this, R.layout.simple_row);

        PlantsDatabase plantsDatabase = new PlantsDatabase(this, "plant.db");
        plantsDatabase.open();
        ArrayList<PlantCommonName> plantsCommonNames = plantsDatabase.getPlantsCommonNames();
        plantsCommonNames = plantsDatabase.getPlantsCommonNames();
        for (PlantCommonName plantCommonName : plantsCommonNames) {
            mListAdapter.add(plantCommonName);
        }
        plantsDatabase.close();

        mList.setEmptyView(mEmpty);
        mList.setAdapter(mListAdapter);

        mActivity = this;

        mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int plant_id = ((PlantCommonName)mList.getItemAtPosition(position)).getId();

                Intent intent = new Intent(mActivity, PlantsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("plant_id", plant_id);
                intent.putExtras(bundle);
                
                startActivityForResult(intent, PICK_PLANT);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int plant_id = -1;
        int percentage = -1;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PLANT:
                    plant_id = data.getIntExtra("plant_id", -1);
                    percentage = data.getIntExtra("percentage", -1);

                    assert(plant_id != -1);
                    assert(percentage != -1);

                    Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);

                    returnIntent.putExtra("plant_id", plant_id);
                    returnIntent.putExtra("percentage", percentage);

                    setResult(RESULT_OK, returnIntent);

                    finish();

                    break;
                default:
                    break;
            }
        }
    }
}
