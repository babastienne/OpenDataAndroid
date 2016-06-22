package fr.iut.babastienne.opendataandroid.fr.iut.babastienne.opendataandroid.bean;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bastien on 16/06/2016.
 */
public class Request {
    private String body;
    private String nom_jeu;
    private String nom_table;
    private static String format = "json"; // by default json is the selected format
    private String filter = null;
    private int limit;
    private int offset = 0;

    public Request() {
        this.body = "http://data.nantes.fr/api/publication/";
        this.setNom_jeu("24440040400129_NM_NM_00170"); // Obligatoire : correspond à l'API donnée dans les consignes
        this.setNom_table("Toilettes_publiques_nm_STBL"); // Obligatoire : correspond à l'API donnée dans les consignes
        this.setLimit(25); // choix de conception : on choisi de demander maximum 25 résultats
    }

    public String getNom_jeu() {
        return nom_jeu;
    }

    public void setNom_jeu(String nom_jeu) {
        this.nom_jeu = nom_jeu;
    }

    public String getNom_table() {
        return nom_table;
    }

    public void setNom_table(String nom_table) {
        this.nom_table = nom_table;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(LatLng position) {
        this.filter = "{\"_l\":{\"$near\":[" + position.latitude + "," + position.longitude + "]}}";
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return (filter == null) ?
                (body + nom_jeu + "/" + nom_table + "/content?format=" + format + "&limit=" + limit + "&offset=" + offset) :
                (body + nom_jeu + "/" + nom_table + "/content?format=" + format + "&limit=" + limit + "&offset=" + offset + "&filter=" + filter);
    }
}
