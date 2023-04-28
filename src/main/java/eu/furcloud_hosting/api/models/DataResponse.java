package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;
import eu.furcloud_hosting.api.services.Status;

import java.util.HashMap;

public class DataResponse {

    @SerializedName("status")
    private Status status;
    private HashMap<String, String> data;

    public DataResponse(Status status, HashMap<String, String> data) {
        this.status = status;
        this.data = data;
    }

}
