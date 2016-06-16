package fr.iut.babastienne.opendataandroid;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean.Request;

public class FirstScreenActivity extends AppCompatActivity {

    // REQUEST CODES
    static final Integer REQUEST_CODE_PLACE_PICKER = 5;

    // RETURN CODES
    static final Integer RETURN_CODE_OK = 1;
    static final Integer RETURN_CODE_ERROR = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);
    }


    /**
     * Code from google documentation.
     *
     * @param v
     */
    public void onPickButtonClick(View v) {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_CODE_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            Log.e("LOG", "Error with google play services. Message : " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("LOG", "Error with google play services : Not available. Message : " + e.getMessage());
        }
    }

    /**
     * Code from google documentation.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PLACE_PICKER
                && resultCode == RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            LatLng position = place.getLatLng();
            if (attributions == null) {
                attributions = "";
            }

            Log.d("LOG", "name = " + name);
            Log.d("LOG", "address = " + address);
            Log.d("LOG", "attributions (html) = " + Html.fromHtml(attributions));

            requestToOpenData(position);


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void requestToOpenData(LatLng position) {
        int limit = Integer.parseInt(((EditText) findViewById(R.id.limit)).getText().toString());
        Request request = new Request("24440040400129_NM_NM_00170", "Toilettes_publiques_nm_STBL", limit);
        request.setFilter(position);

        Log.d("LOG", "Request = " + request.toString());
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.toString()));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
