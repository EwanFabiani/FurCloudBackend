package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;

public enum ResponseStatus {

    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR

}
