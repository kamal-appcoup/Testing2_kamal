package appcoup.f.testing_kamal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import appcoup.f.testing_kamal.utils.AppController;
import appcoup.f.testing_kamal.utils.UrlHelper;

public class Dashboard extends AppCompatActivity {

    ImageView profile_1 , profile_2 ;
    EditText username , mobileNo ,address , sex ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profile_1 = (ImageView)findViewById(R.id.img_user);
        profile_2 = (ImageView)findViewById(R.id.img_name);

        username = (EditText) findViewById(R.id.edt_username);
        mobileNo = (EditText) findViewById(R.id.edt_MobileNo);
        address = (EditText) findViewById(R.id.edt_Address);
        sex = (EditText) findViewById(R.id.edt_Sex);


        getDetails();

    }

    private void getDetails() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlHelper.URL_DASHBOARD, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("URL_Response","" + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("URL_Error","" + error.toString());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
