package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("status")
    private ResponseStatus status;
    private String message;

    public ErrorResponse(String errorMessage) {
        this.status = ResponseStatus.ERROR;
        this.message = errorMessage;
    }

}
