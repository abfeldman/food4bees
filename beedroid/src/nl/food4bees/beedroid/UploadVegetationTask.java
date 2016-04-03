package nl.food4bees.beedroid;

// Android
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;
import android.content.Context;

// Android HTTP client library
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

// Java
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.List;
import java.util.ArrayList;

public class UploadVegetationTask extends AsyncTask<Vegetation, Void, Boolean> {
    private static final String TAG = UploadVegetationTask.class.getName();

    private MainActivity mMainActivity;

    public UploadVegetationTask(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    protected Boolean doInBackground(Vegetation... vegetations) {
        CookieStore cookieStore = CookieStoreStore.getInstance().getCookieStore();

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        boolean result = true;

        for (int i = 0; i < vegetations.length; i++) {
            Vegetation vegetation = vegetations[i];

            DefaultHttpClient client = new DefaultHttpClient();

            String url = mMainActivity.getString(R.string.webapp_url);

            HttpPost post = new HttpPost(url + "add-vegetation");

            try {
                Polygon area = vegetation.getArea();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2 + area.size() * 2);
                nameValuePairs.add(new BasicNameValuePair("plant", vegetation.getPlantId().toString()));
                nameValuePairs.add(new BasicNameValuePair("amount", vegetation.getAmount().toString()));

                int j = area.size();

                for (int k = 0; k < j; k++) {
                    Point point = area.getPoint(k);

                    nameValuePairs.add(new BasicNameValuePair("x", new Double(point.getX()).toString()));
                    nameValuePairs.add(new BasicNameValuePair("y", new Double(point.getY()).toString()));
                }

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post, localContext);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String body = "";
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    body += line;
                }

                if ("Success".equals(body)) {
                    continue;
                }

                result = false;
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Submit vegetation exception: ", e);

                result = false;
            } catch (IOException e) {
                Log.e(TAG, "Submit vegetation exception: ", e);

                result = false;
            }
        }

        return result;
    }

    protected void onPostExecute(Boolean result) {
        String message = mMainActivity.getString(result ? R.string.submit_success : R.string.submit_error);

        Toast toast = Toast.makeText(mMainActivity.getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();
    }
}
