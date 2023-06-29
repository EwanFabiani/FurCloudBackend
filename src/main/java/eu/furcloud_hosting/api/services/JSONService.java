package eu.furcloud_hosting.api.services;

import com.google.gson.Gson;
import eu.furcloud_hosting.api.models.DataResponse;
import eu.furcloud_hosting.api.models.ErrorResponse;
import eu.furcloud_hosting.api.models.ResponseStatus;
import eu.furcloud_hosting.api.models.SuccessResponse;

import java.util.HashMap;

public class JSONService {

    public static String createJsonData(ResponseStatus status, HashMap<String, String> message) {
        DataResponse dataResponse = new DataResponse(status, message);
        Gson gson = new Gson();
        return gson.toJson(dataResponse);
    }

    public static String createJsonError(String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        Gson gson = new Gson();
        return gson.toJson(errorResponse);
    }

    public static String createJsonSuccess(String message) {
        SuccessResponse successResponse = new SuccessResponse(message);
        Gson gson = new Gson();
        return gson.toJson(successResponse);
    }

}
