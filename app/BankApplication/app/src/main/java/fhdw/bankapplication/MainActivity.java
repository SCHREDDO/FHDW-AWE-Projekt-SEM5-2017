package fhdw.bankapplication;

        import android.app.Activity;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.EditText;
        import android.os.AsyncTask;

        import android.util.Pair;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;

        import org.apache.http.HttpResponse;
        import org.apache.http.HttpStatus;

        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.util.EntityUtils;

        import java.io.IOException;
        import java.util.LinkedList;
        import java.util.List;

public class MainActivity extends Activity {

    EditText loginTv;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginTv =(EditText)findViewById(R.id.LoginTV);
        loginBtn = (Button) findViewById(R.id.LoginBtn);
        loginTv.callOnClick();

    }

    public void get() {
        new AsyncTask<String, Void, Pair<String, Integer>>() {

            @Override
            protected Pair<String, Integer> doInBackground(String... params) {


                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if(httpResponse == null) return null;

                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if(statusCode == HttpStatus.SC_OK){
                        String json = EntityUtils.toString(httpResponse.getEntity());
                        return Pair.create(json, statusCode);
                    }
                    else {
                        return Pair.create(null, statusCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Pair<String, Integer> result) {
                if (result != null && result.first != null) {
                    String json = result.first;
                    Gson gson = new GsonBuilder().create();
                    RestData restData = gson.fromJson(json, RestData.class);
                    //textOutput.setText(restData.getInfo());
                }
            }
        }.execute("http://10.0.2.2:9998/rest/data");
    }




}
