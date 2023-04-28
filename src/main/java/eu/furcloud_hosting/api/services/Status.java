package eu.furcloud_hosting.api.services;

import com.google.gson.annotations.SerializedName;

public enum Status {

    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR

}
