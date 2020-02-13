package com.phlink.bus.api.common.domain;

import java.util.HashMap;

public class BusApiResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public BusApiResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public BusApiResponse data(Object data) {
        if(data != null) {
            this.put("data", data);
        }
        return this;
    }

    @Override
    public BusApiResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
