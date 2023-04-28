package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;
import eu.furcloud_hosting.api.services.Status;

public class SuccessResponse {

    @SerializedName("status")
    private Status status;
    private String message;

    public SuccessResponse(String message) {
        this.status = Status.SUCCESS;
        this.message = message;
    }

}
