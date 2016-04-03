package nl.food4bees.beedroid;

import android.os.Build;
import android.os.Bundle;

import android.view.View;

import android.text.Html;

import android.content.Intent;

import android.widget.SeekBar;
import android.widget.TextView;

import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PlantsActivity extends SherlockActivity {
    private TextView mCommonName;
    private TextView mScientificName;
    private TextView mPercentageText;
    private SeekBar mPercentage;
    private TextView mDescription;

    private Plant mPlant = null;

    private Menu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.plants);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mCommonName = (TextView)findViewById(R.id.plant_name);
        mScientificName = (TextView)findViewById(R.id.scientific_name);
        mPercentageText = (TextView)findViewById(R.id.percentage_text);
        mPercentage = (SeekBar)findViewById(R.id.percentage);
        mDescription = (TextView)findViewById(R.id.description);

        mPercentage.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPercentageText.setText(progress + "%");
            }
        });

        Bundle bundle = getIntent().getExtras();
        Integer plant_id = bundle.getInt("plant_id");

        PlantsDatabase plantsDatabase = new PlantsDatabase(this, "plant.db");
        plantsDatabase.open();
        mPlant = plantsDatabase.getPlant(plant_id);
        plantsDatabase.close();

        showPlant();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;

        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.plants, menu);

        if (mPlant == null) {
            mMenu.findItem(R.id.menu_submit).setVisible(false);
        } else {
            mMenu.findItem(R.id.menu_submit).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_submit:
                if (mPlant != null) {
                    int plant_id = mPlant.getId();
                    int percentage = mPercentage.getProgress();

                    Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);

                    returnIntent.putExtra("plant_id", plant_id);
                    returnIntent.putExtra("percentage", percentage);

                    setResult(RESULT_OK, returnIntent);

                    finish();
                }

                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPlant() {
        mCommonName.setText(mPlant.getCommonName());
        mScientificName.setText(mPlant.getScientificName());

        String description = mPlant.getDescription();

        if (description != null) {
            mDescription.setText(Html.fromHtml(description));
        }
    }

}
