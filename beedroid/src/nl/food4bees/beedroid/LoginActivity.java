package nl.food4bees.beedroid;

import android.os.Bundle;

import android.app.Activity;

import android.view.View;

import android.widget.Spinner;
import android.widget.NumberPicker;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.client.CookieStore;

import com.actionbarsherlock.app.SherlockActivity;

public final class LoginActivity extends Activity {
    private TextView mError;
    private TextView mEmail;
    private TextView mPassword;

    SharedPreferences mSettings; /* @todo: Use Android authentication. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        mError = (TextView)findViewById(R.id.error);
        mEmail = (TextView)findViewById(R.id.email);
        mPassword = (TextView)findViewById(R.id.password);

        mSettings = getPreferences(MODE_PRIVATE);

        String email = mSettings.getString("email", null);
        String password = mSettings.getString("password", null);
        if (email != null) {
            mEmail.setText(email);
        }
        if (password != null) {
            mPassword.setText(password);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    public void logIn(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();

        LoginCredentials credentials = new LoginCredentials(email, password);

        new AuthenticateTask(this).execute(credentials);
    }

    public void authenticate(CookieStore cookieStore) {
        CookieStoreStore.getInstance().setCookieStore(cookieStore);

        Intent returnIntent = new Intent();

        setResult(RESULT_OK, returnIntent);

        syncPlants();

        finish();
    }

    public void error(String error) {
        mError.setText(error);
    }


    private void syncPlants() {
        new SyncPlantsTask(this).execute();
    }
}
