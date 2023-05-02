package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class DataResponse {

    @SerializedName("status")
    private ResponseStatus status;
    private HashMap<String, String> data;

    public DataResponse(ResponseStatus status, HashMap<String, String> data) {
        this.status = status;
        this.data = data;
    }

}
