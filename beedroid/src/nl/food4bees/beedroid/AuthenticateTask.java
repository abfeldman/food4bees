package nl.food4bees.beedroid;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.net.UnknownHostException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.List;
import java.util.ArrayList;

public class AuthenticateTask extends AsyncTask<LoginCredentials, Void, Boolean> {
    LoginActivity mLoginActivity = null;
    CookieStore mCookieStore = null;

    String mError = null;

    public AuthenticateTask(LoginActivity loginActivity) {
        super();

        mLoginActivity = loginActivity;
    }

    protected Boolean doInBackground(LoginCredentials... credentials) {
        DefaultHttpClient client = new DefaultHttpClient();
        /* @todo: Don't use a hardcoded URL. */
        //        HttpPost post = new HttpPost("http://food4bees.org/f4bi/authenticate-user");
        HttpPost post = new HttpPost("http://192.168.96.8:98/f4bi/authenticate-user");

        mCookieStore = new BasicCookieStore();

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("beedroid", "true"));
        nameValuePairs.add(new BasicNameValuePair("email", credentials[0].getEmail()));
        nameValuePairs.add(new BasicNameValuePair("password", credentials[0].getPassword()));

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post, localContext);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String body = "";
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                body += line;
            }

            System.out.println("<" + body + ">");

            if ("Missing email".equals(body)) {
                /* This should never happen because we check in the client. */
                mError = body;

                return false;
            }
            if ("Missing password".equals(body)) {
                /* This should never happen because we check in the client. */
                mError = body;

                return false;
            }
            if ("Internal error".equals(body)) {
                mError = "Backend internal error";
                /* @todo: Add message "report to system administrator", etc. */

                return false;
            }
            if ("Bad credentials".equals(body)) {
                mError = body;

                return false;
            }
            if ("Success".equals(body)) {
                return true;
            }
        } catch (HttpHostConnectException e) {
            mError = "Communication problem";

            return false;
        } catch (ClientProtocolException e) {
            mError = "Internal error";

            // @todo: Handle the exception.

            return false;
        } catch (UnknownHostException e) {
            mError = "Unable to connect to server (check Internet connection)";

            return false;
        } catch (IOException e) {
            mError = "Internal input/output error";

            return false;
        }

        mError = "Internal error";

        return false;
    }

    protected void onPostExecute(Boolean result)
    {
        if (result) {
            mLoginActivity.authenticate(mCookieStore);

            return;
        }

        assert mError != null;

        mLoginActivity.error(mError);
    }
}
