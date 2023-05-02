package eu.furcloud_hosting.api.models;

import com.google.gson.annotations.SerializedName;

public class SuccessResponse {

    @SerializedName("status")
    private ResponseStatus status;
    private String message;

    public SuccessResponse(String message) {
        this.status = ResponseStatus.SUCCESS;
        this.message = message;
    }

}
