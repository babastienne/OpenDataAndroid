package fr.iut.babastienne.opendataandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.adapter.ToiletAdapter;
import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean.Request;
import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean.Toilet;

public class FirstScreenActivity extends AppCompatActivity {

    // REQUEST CODES
    static final Integer REQUEST_CODE_PLACE_PICKER = 5;

    // RETURN CODES
    static final Integer RETURN_CODE_ERROR = -1;

    ArrayAdapter<Integer> limitSpinner;
    JSONArray jsonresults = null; // ceci contiendra le résultat de la requête. tant qu'il n'y a pas de requête le résultat est null
    int limit; // correspond au nombre de résultats affichés


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

        // création du spinner permettant le choix de la limite de réulstats par requête
        Integer[] listOfLimit = {5, 10, 15, 20, 25};
        Log.d("LOG", "Creation of the spinner used for the selection of the limit fro the request");
        limitSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listOfLimit);
        Spinner myLimitSpinner = (Spinner) findViewById(R.id.limitSpinner);
        myLimitSpinner.setAdapter(limitSpinner);

        // on initialise le param limit
        limit = Integer.parseInt(((Spinner) findViewById(R.id.limitSpinner)).getSelectedItem().toString()); // récupération de la limite définie à l'aide du spinner

        // on ajoute un listener au spinner pour mettre à jour la limite
        myLimitSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                limit = Integer.parseInt(((Spinner) findViewById(R.id.limitSpinner)).getSelectedItem().toString()); // on met à jour la limite
                if (jsonresults != null) { // si une requête a déjà été effectuée
                    jsonTraitement();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("LOG", "Nothing append in the 'onitemSelectedListener' of the arrayAdapter.");
            }
        });
    }


    /**
     * Code from google documentation. Don't need to comment.
     *
     * @see "https://developers.google.com/android/reference/com/google/android/gms/location/places/ui/PlacePicker"
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
     * Code from google documentation. Don't need to comment.
     *
     * @see "https://developers.google.com/android/reference/com/google/android/gms/location/places/ui/PlacePicker"
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


        } else if (resultCode == RETURN_CODE_ERROR) {
            Log.e("LOG", "Error during the return of an intent. Please consult log for more information");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Method wich create and make the request to the server of Nantes. The server return a Json object.
     *
     * @param position wich correspond to the position selected by the user with the Place_Picker module
     */
    public void requestToOpenData(LatLng position) {

        Request request = new Request(); // on lance une requête vers l'API de Nantes métropole
        request.setFilter(position); // on utilise la position choisie par l'utilisateur comme filtre pour les données

        Log.d("LOG", "Request = " + request.toString()); // on affiche la requête construite
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, request.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            jsonresults = obj.getJSONArray("data");
                            Log.d("LOG", "Actually during the 'onResponse' method");
                            jsonTraitement();
                        } catch (JSONException e) {
                            Log.e("LOG", "Error during the recuperation of the Json object. Message : " + e.getMessage() + "\n Response = " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e("LOG", "Error during the response (Volley error). Message : " + e.getMessage());
                finish();
            }
        });

        queue.add(stringRequest);

    }

    /**
     * This method get the Json text and generate Toilet objects. Then this function create the ListView with the toilets.
     */
    public void jsonTraitement() {
        ArrayList<Toilet> listToilets = new ArrayList<>();

        try {
            for (int index = 0; index < limit; index++) {
                JSONObject result = jsonresults.getJSONObject(index);

                Log.d("LOG", "Creation of a toilet object from the json");
                Toilet toilette = new Toilet(result);

                listToilets.add(toilette);
            }
        } catch (JSONException e) {
            Log.e("LOG", "Error during the traitement of the Json object. Message : " + e.getMessage());
        }

        // Creation of the listView
        ListView resultsView = (ListView) findViewById(R.id.resultsView);

        resultsView.setAdapter(new ToiletAdapter(this, listToilets));

        // on ajoute un listener à la listView. Au clic sur un item on affiche un écran avec plus de détails.
        resultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetails((Toilet) parent.getItemAtPosition(position)); // on
            }
        });
    }

    /**
     * This method launch another activity (DetailScreenActivity) with a toilet description in an intent
     *
     * @param toilet correspond to the parameter in the intent
     */
    public void displayDetails(Toilet toilet) {
        Intent intent = new Intent(this, DetailScreenActivity.class); // creation of an intent
        intent.putExtra(Intent.EXTRA_TEXT, toilet.getJsonObject()); // put all the Json object in the intent (best way to transfert an object from an activity to another if we don't want to implement a Parcelable object)

        startActivity(intent); // launch the detailScreen wich show the details of a toilet
    }

}
