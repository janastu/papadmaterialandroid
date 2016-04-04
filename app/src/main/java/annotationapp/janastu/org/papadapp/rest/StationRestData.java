package annotationapp.janastu.org.papadapp.rest;

/**
 * Created by Graphics-User on 11/17/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationRestData  {

    private List<Station> stations = new ArrayList<Station>();

    public StationRestData() {
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }



}