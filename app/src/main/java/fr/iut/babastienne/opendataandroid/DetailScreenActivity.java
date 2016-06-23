package fr.iut.babastienne.opendataandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean.Toilet;

public class DetailScreenActivity extends AppCompatActivity {

    private Toilet toilet;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        try {
            Bundle bund = getIntent().getExtras();
            toilet = new Toilet(new JSONObject(bund.getString(Intent.EXTRA_TEXT)));
        } catch (JSONException e) {
            Log.e("LOG", "Error during the reconstruction of the toilet object in the DetailScreenActivity. Message = " + e.getMessage());
        }

        this.setTitle(toilet.getAdresse());

        // Adresse
        TextView adresse = (TextView) findViewById(R.id.adresse);
        adresse.setText(toilet.getAdresse());
        TextView commune = (TextView) findViewById(R.id.commune);
        commune.setText("Commune : " + toilet.getCommune());

        // Type
        TextView type = (TextView) findViewById(R.id.type);
        if (toilet.getType().equals("Mobilier")) {
            type.setText("Les toilettes sont du mobilier urbain");
        } else if (toilet.getType().equals("Bâti")) {
            type.setText("Les toilettes sont dans un bâtiment");
        } else {
            type.setText("");
        }

        // Automatisme
        TextView automatique = (TextView) findViewById(R.id.automatique);
        if (toilet.isAutomatique()) {
            automatique.setText("Les toilettes sont automatiques");
        } else {
            automatique.setText("Les toilettes ne sont pas automatiques");
        }

        // Accessibilite au PMR
        TextView accessibilite = (TextView) findViewById(R.id.accessibilitePMR);
        if (toilet.isAccessibilitePMR()) {
            accessibilite.setText("Les toilettes sont accessibles aux personnes à mobilité réduite");
        } else {
            accessibilite.setText("Les toilettes ne sont pas accessibles aux personnes à mobilité réduite");
        }

        // Horaires
        TextView horaires = (TextView) findViewById(R.id.infosHoraires);
        if (toilet.getInfosHoraires().equals("en journée") || toilet.getInfosHoraires().equals("journée")) {
            horaires.setText("Les toilettes sont accessibles uniquement en journée");
        } else if (toilet.getInfosHoraires().equals("restreints")) {
            horaires.setText("L'accès aux toilettes est restreint");
        } else if (toilet.getInfosHoraires().equals("24/24")) {
            horaires.setText(("Toilettes accessibles 24/24H"));
        } else {
            horaires.setText("Aucune information sur les horaires d'ouverture des toilettes");
        }

    }

    /**
     * Launch the navigation with google maps to the toilet
     *
     * @param v View of the button (method called by onClick)
     */
    public void startNavigation(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Double.toString(toilet.getGeoLocation().latitude) + "," + Double.toString(toilet.getGeoLocation().longitude)));
        this.startActivity(intent);
    }

}
