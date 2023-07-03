package br.com.ceub.timesheet.service;

import br.com.ceub.timesheet.domain.entities.Position;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PositionService {
    private double lat1 = -15.764587;
    private double long1 = -47.895148;

    private double lat2 = -15.769664;
    private double long2 = -47.893491;

    private double lat3 = -15.764244;
    private double long3 = -47.894041;

    private double lat4 = -15.769370;
    private double long4 = -47.892553;

    public Boolean checkPosition(Position position) {
        String latitude = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
        String longitude = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";

        if (!Double.toString(position.getLatitude()).matches(latitude) || !Double.toString(position.getLongitude()).matches(longitude)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coordenadas invalidas!");
        }

        return checkArea(position.getLatitude(), position.getLongitude(), lat1, long1, lat2, long2, lat3, long3, lat4, long4);
    }

    public static boolean checkArea(double latitude, double longitude, double lat1, double long1, double lat2, double long2, double lat3, double long3, double lat4, double long4) {
        double minLat = Math.min(Math.min(lat1, lat2), Math.min(lat3, lat4));
        double maxLat = Math.max(Math.max(lat1, lat2), Math.max(lat3, lat4));
        double minLong = Math.min(Math.min(long1, long2), Math.min(long3, long4));
        double maxLong = Math.max(Math.max(long1, long2), Math.max(long3, long4));

        if (latitude >= minLat && latitude <= maxLat && longitude >= minLong && longitude <= maxLong) {
            return true;
        } else {
            return false;
        }
    }
}
