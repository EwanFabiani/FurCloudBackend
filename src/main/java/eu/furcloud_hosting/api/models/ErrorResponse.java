package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;
import eu.furcloud_hosting.api.services.Status;

public class ErrorResponse {

    @SerializedName("status")
    private Status status;
    private String message;

    public ErrorResponse(String errorMessage) {
        this.status = Status.ERROR;
        this.message = errorMessage;
    }

}
