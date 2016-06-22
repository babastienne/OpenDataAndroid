package fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bastien on 21/06/2016.
 */
public class Toilet {
    private String id;
    private String adresse;
    private String commune;
    private String pole;
    private String type;
    private boolean automatique;
    private boolean accessibilitePMR;
    private String infosHoraires;
    private LatLng geoLocation;

    public Toilet(String id, String adresse, String commune, String pole, String type, boolean automatique, boolean accessibilitePMR, String infosHoraires, LatLng geoLocation) {
        this.id = id;
        this.adresse = adresse;
        this.commune = commune;
        this.pole = pole;
        this.type = type;
        this.automatique = automatique;
        this.accessibilitePMR = accessibilitePMR;
        this.infosHoraires = infosHoraires;
        this.geoLocation = geoLocation;
    }

    public Toilet(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getJSONObject("geo").getString("name");
            this.adresse = jsonObject.getString("ADRESSE");
            this.commune = jsonObject.getString("COMMUNE");
            this.pole = jsonObject.getString("POLE");
            this.type = jsonObject.getString("TYPE");
            this.automatique = jsonObject.getString("AUTOMATIQUE").equals("oui");
            this.accessibilitePMR = jsonObject.getString("ACCESSIBILITE_PMR").equals("oui");
            this.infosHoraires = jsonObject.getString("INFOS_HORAIRES");
            this.geoLocation = new LatLng(jsonObject.getJSONArray("_l").getDouble(0), jsonObject.getJSONArray("_l").getDouble(1));
        } catch (JSONException e) {
            Log.e("LOG", "Error duiring the creation of the 'Toilet' object. Message = " + e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public boolean isAccessibilitePMR() {
        return accessibilitePMR;
    }

    public void setAccessibilitePMR(boolean accessibilitePMR) {
        this.accessibilitePMR = accessibilitePMR;
    }

    public String getInfosHoraires() {
        return infosHoraires;
    }

    public void setInfosHoraires(String infosHoraires) {
        this.infosHoraires = infosHoraires;
    }

    public LatLng getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(LatLng geoLocation) {
        this.geoLocation = geoLocation;
    }
}
