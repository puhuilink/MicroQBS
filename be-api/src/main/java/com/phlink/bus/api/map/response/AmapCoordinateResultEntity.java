package com.phlink.bus.api.map.response;

import jodd.util.StringPool;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AmapCoordinateResultEntity {
    private String status;
    private String info;
    private String locations;
    private String infocode;

    private boolean requestSuccess() {
        return "1".equals(status) && "10000".equals(infocode);
    }

    public List<Double[]> getPoints() {
        List<Double[]> points = new ArrayList<>();
        if(this.requestSuccess()) {
            String[] locationsArray = locations.split("\\|");
            for(int i=0; i<locationsArray.length; i++) {
                String subLocation = locationsArray[i];
                String[] subLocationArray = subLocation.split(StringPool.COMMA);
                Double[] subPoints = new Double[2];
                Double p1 = Double.parseDouble(subLocationArray[0]);
                Double p2 = Double.parseDouble(subLocationArray[1]);
                subPoints[0] = p1;
                subPoints[1] = p2;
                points.add(subPoints);
            }
        }
        return points;
    }
}
