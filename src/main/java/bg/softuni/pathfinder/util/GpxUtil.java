package bg.softuni.pathfinder.util;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.Length;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class GpxUtil {

    public double calculateDistance(String gpxCoordinates) {
        if (gpxCoordinates == null || gpxCoordinates.trim().isEmpty()) {
            System.out.println("Empty or null GPX data provided");
            return 0.0;
        }

        try {
            System.out.println("Attempting to parse GPX data");
            System.out.println("Raw GPX data: " + gpxCoordinates);
            System.out.println("Data length: " + gpxCoordinates.length());

            GPX gpx = GPX.reader().read(new ByteArrayInputStream(gpxCoordinates.getBytes(StandardCharsets.UTF_8)));

            double totalDistance = 0.0;

            for (Track track : gpx.getTracks()) {
                for (TrackSegment segment : track.getSegments()) {
                    if (segment.getPoints().size() >= 2) {
                        WayPoint start = segment.getPoints().get(0);
                        System.out.println("Start point coordinates:");
                        System.out.println("Latitude: " + start.getLatitude());
                        System.out.println("Longitude: " + start.getLongitude());
                        System.out.println(
                                "Elevation: " + start.getElevation().map(e -> e.to(Length.Unit.METER)).orElse(0.0));

                        WayPoint end = segment.getPoints().get(segment.getPoints().size() -1);
                        System.out.println("End point coordinates:");
                        System.out.println("Latitude: " + end.getLatitude());
                        System.out.println("Longitude: " + end.getLongitude());
                        System.out.println(
                                "Elevation: " + end.getElevation().map(e -> e.to(Length.Unit.METER)).orElse(0.0));

                        totalDistance += start.distance(end).to(Length.Unit.KILOMETER);
                    }
                }
            }
            // Форматиране на числото, за да премахнем излишните нули
            String formattedDistance = String.format("%.2f", totalDistance).replaceAll("0*$", "").replaceAll("\\.$",
                    "");
            System.out.println("Successfully calculated total distance: " + formattedDistance + " km");
            return Double.parseDouble(formattedDistance);
        } catch (Exception e) {
            System.out.println("Error parsing GPX data: " + e.getMessage());
            System.out.println("GPX data content: " + gpxCoordinates);
            e.printStackTrace();
            return 0.0;
        }
    }

    public WayPoint getStartWayPoint(String gpxCoordinates) throws IOException {
        GPX gpx = GPX.reader().read(new ByteArrayInputStream(gpxCoordinates.getBytes(StandardCharsets.UTF_8)));
        Track firstTrack = gpx.getTracks().get(0);
        TrackSegment firstSegment = firstTrack.getSegments().get(0);
        return firstSegment.getPoints().get(0);
    }

    public WayPoint getEndWayPoint(String gpxCoordinates) throws IOException {
        GPX gpx = GPX.reader().read(new ByteArrayInputStream(gpxCoordinates.getBytes(StandardCharsets.UTF_8)));
        Track firstTrack = gpx.getTracks().get(0);
        TrackSegment firstSegment = firstTrack.getSegments().get(0);
        return firstSegment.getPoints().get(firstSegment.getPoints().size() -1);
    }

}
