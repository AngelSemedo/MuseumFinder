package es.unex.saee.museumfinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 30/01/2017.
 */

public class Response {

    private List<GooglePlace> places = new ArrayList<GooglePlace>();

    public void addPlace(GooglePlace gplace){
        places.add(gplace);
    }

    public List<GooglePlace> getPlaces() {
        return places;
    }
}
