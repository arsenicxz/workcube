package com.vasenin.workcube.misc;

public enum ErrorType {
    INVALID_AUTHENTICATION("invalid_authentication"),
    USER_ALREADY_EXIST("user_already_exist"),
    INVALID_EMAIL("invalid_email"),
    FIELD_MUST_BE_FILLED_IN("fields_must_be_filled_in"),
    INVALID_PASSWORD("invalid_password"),
    PLACE_NAME_ERROR("place_name_error"),
    LAT_LON_NULL("lat_lon_null"),
    PRICE_REQUIRED("price_required"),
    CLOCK_REQUIRED("clock_required");
    private final String value;
    ErrorType(String value)
    {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
