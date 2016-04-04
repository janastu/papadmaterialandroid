package annotationapp.janastu.org.papadapp.rest;

/**
 * Created by Graphics-User on 11/17/2015.
 */import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Station
{
    private String name;

    private List<String> adminUsers = new ArrayList<String>();
    private String store;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(List<String> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", adminUsers=" + adminUsers +
                ", store='" + store + '\'' +
                '}';
    }
}