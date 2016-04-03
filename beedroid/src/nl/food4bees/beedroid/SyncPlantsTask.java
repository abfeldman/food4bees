package nl.food4bees.beedroid;

import android.app.Activity;

import android.os.AsyncTask;

import android.widget.Toast;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.client.protocol.ClientContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import android.content.Context;

public class SyncPlantsTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = MainActivity.class.getName();

    private static final String DB_PATH = "/data/data/nl.food4bees.beedroid/databases/";
    private static final String DB_NAME = "updates.db";

    private Activity mActivity;

    public SyncPlantsTask(Activity activity) {
        mActivity = activity;
    }

    protected Boolean doInBackground(Void... params) {
        CookieStore cookieStore = CookieStoreStore.getInstance().getCookieStore();

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        boolean result = true;

        DefaultHttpClient client = new DefaultHttpClient();
            
        // @todo: Don't use a hardcoded URL.
        // HttpPost post = new HttpPost("http://food4bees.org/f4bi/add-vegetation");
        HttpPost post = new HttpPost("http://192.168.96.8:98/f4bi/sync-plants");

        try {
            PlantsDatabase plantsDatabase = new PlantsDatabase(mActivity, "plant.db");
            plantsDatabase.open();

            ArrayList<PlantVersion> plantVersions = plantsDatabase.getPlantsVersions();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(plantVersions.size() * 2);

            for (PlantVersion plantVersion : plantVersions) {
                nameValuePairs.add(new BasicNameValuePair("id", new Integer(plantVersion.getId()).toString()));
                nameValuePairs.add(new BasicNameValuePair("version", new Integer(plantVersion.getVersion()).toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post, localContext);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                long size = entity.getContentLength();

                InputStream input = entity.getContent();
                OutputStream output = new FileOutputStream(DB_PATH + "updates.db");

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();

                PlantsDatabase updatesDatabase = new PlantsDatabase(mActivity, "updates.db");
                updatesDatabase.open();

                ArrayList<PlantVersion> updateVersions = updatesDatabase.getPlantsVersions();
                for (PlantVersion updateVersion : updateVersions) {
                    Plant plant = updatesDatabase.getPlant(updateVersion.getId());

                    plantsDatabase.deletePlant(plant.getId());
                    plantsDatabase.addPlant(plant);
                }

                updatesDatabase.close();
            }

            plantsDatabase.close();
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Plants database synchronization exception: ", e);

            result = false;
        } catch (IOException e) {
            Log.e(TAG, "Plants database synchronization exception: ", e);

            result = false;
        }

        return result;
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast toast = Toast.makeText(mActivity.getApplicationContext(), "Successfully updated the plants database.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
