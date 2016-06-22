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

        TextView commune = (TextView) findViewById(R.id.commune);
        commune.setText("Commune : " + toilet.getCommune());

        TextView pole = (TextView) findViewById(R.id.pole);
        pole.setText("Nom du pôle de rattachement : " + toilet.getPole());

        TextView type = (TextView) findViewById(R.id.type);
        if (toilet.getType() != "null") {
            type.setText("Typologie des toilettes installées: " + toilet.getType());
        } else {
            type.setText("");
        }

        TextView automatique = (TextView) findViewById(R.id.automatique);
        if (toilet.isAutomatique()) {
            automatique.setText("Les toilettes sont automatiques");
        } else {
            automatique.setText("Les toilettes ne sont pas automatiques");
        }

        TextView accessibilite = (TextView) findViewById(R.id.accessibilitePMR);
        if (toilet.isAccessibilitePMR()) {
            accessibilite.setText("Les toilettes sont accessibles aux personnes à mobilité réduite");
        } else {
            accessibilite.setText("Les toilettes ne sont pas accessibles aux personnes à mobilité réduite");
        }

        TextView horaires = (TextView) findViewById(R.id.infosHoraires);
        if (toilet.getInfosHoraires() == "en journée" || toilet.getInfosHoraires() == "journée") {
            horaires.setText("Les toilettes sont accessibles uniquement en journée");
        } else if (toilet.getInfosHoraires() == "restreints") {
            horaires.setText("L'accès aux toilettes est restreint");
        } else if (toilet.getInfosHoraires() == "24/24") {
            horaires.setText(("Toilettes accessibles 24/24H"));
        } else {
            horaires.setText("Aucune information sur les horaires d'ouverture des toilettes");
        }

    }


    public void startNavigation(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Double.toString(toilet.getGeoLocation().latitude) + "," + Double.toString(toilet.getGeoLocation().longitude)));
        this.startActivity(intent);
    }

}
