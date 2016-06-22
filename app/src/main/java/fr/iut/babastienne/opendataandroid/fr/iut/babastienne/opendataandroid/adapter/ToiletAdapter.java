package fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iut.babastienne.opendataandroid.R;
import fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean.Toilet;

/**
 * Created by Bastien on 22/06/2016.
 * This class create and complete the listView used to see the toilets
 */
public class ToiletAdapter extends ArrayAdapter<Toilet> {

    public ToiletAdapter(Activity context, ArrayList<Toilet> listToilets) {
        super(context, R.layout.first_screen, listToilets);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        Toilet item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.toilet_list, null);

        TextView adresse = (TextView) row.findViewById(R.id.adresse);
        adresse.setText(item.getAdresse());

        TextView commune = (TextView) row.findViewById(R.id.commune);
        commune.setText(item.getCommune());

        TextView infosHoraires = (TextView) row.findViewById(R.id.infosHoraires);
        if (item.getInfosHoraires() != "null") {
            infosHoraires.setText("Horaires : " + item.getInfosHoraires());
        } else {
            infosHoraires.setText("");
        }

        TextView accessibilitePMR = (TextView) row.findViewById(R.id.accessibilitePMR);
        if (item.isAccessibilitePMR()) {
            accessibilitePMR.setText("Accessible au PMR");
        } else {
            accessibilitePMR.setText("Non accessible au PMR");
        }

        return (row);
    }
}
