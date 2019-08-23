package appcoup.f.testing_kamal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import appcoup.f.testing_kamal.utils.AppController;
import appcoup.f.testing_kamal.utils.UrlHelper;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100 ;
    ImageView profile_1 , profile_2 ;
    EditText username , mobileNo ,address , sex ;
    Button submit ;

    String Username , MobileNo , Address , Sex ;
    private static final int SELECT_PICTURE_1 = 1;
    private static final int SELECT_PICTURE_2 = 2;

    private String selectedImagePath_1;
    private String selectedImagePath_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_1 = (ImageView)findViewById(R.id.img_user);
        profile_2 = (ImageView)findViewById(R.id.img_name);

        username = (EditText) findViewById(R.id.edt_username);
        mobileNo = (EditText) findViewById(R.id.edt_MobileNo);
        address = (EditText) findViewById(R.id.edt_Address);
        sex = (EditText) findViewById(R.id.edt_Sex);

        submit = (Button)findViewById(R.id.btn_Submit);

       /* Picasso.with(getApplicationContext()).load(R.mipmap.ic_launcher).into(profile_1);
        Picasso.with(getApplicationContext()).load(R.mipmap.ic_launcher).into(profile_2);*/


        profile_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE_1);
            }
        });

        profile_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_CAMERA_BUTTON);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE_2);*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, SELECT_PICTURE_2);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Username = username.getText().toString().trim();
                MobileNo = mobileNo.getText().toString().trim();
                Address = address.getText().toString().trim();
                Sex = sex.getText().toString().trim();

                SubmitValues();
            }
        });

    }

    private void SubmitValues() {

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, UrlHelper.URL_LOGIN, null,
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("", Username);
                params.put("", Address);
                params.put("", MobileNo);
                params.put("", Sex);
                params.put("", selectedImagePath_1);
                params.put("", selectedImagePath_2);

                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, SELECT_PICTURE_2);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_1) {
                Uri selectedImageUri = data.getData();
                Log.d("selected_uri","" + selectedImageUri);
              /*  Picasso.with(getApplicationContext()).load(selectedImageUri).into(profile_1);*/
            }
            else {
                if (requestCode == SELECT_PICTURE_2) {
                   /* Uri selectedImageUri = data.getData();
                    Picasso.with(getApplicationContext()).load(selectedImageUri).into(profile_2);*/

                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    profile_2.setImageBitmap(image);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
     /*   if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();*/

        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}
