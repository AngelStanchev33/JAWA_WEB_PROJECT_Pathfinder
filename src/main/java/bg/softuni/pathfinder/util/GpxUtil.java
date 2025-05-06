package bg.softuni.pathfinder.util;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.Length;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class GpxUtil {


    public double calculateDistance(Object gpxInput) {
        try {
            System.out.println("GPX input type: " + (gpxInput == null ? "null" : gpxInput.getClass().getName()));
            if (gpxInput instanceof String) {
                System.out.println("GPX input as String (first 100 chars): " + ((String) gpxInput).substring(0, Math.min(100, ((String) gpxInput).length())));
            } else if (gpxInput instanceof File) {
                System.out.println("GPX input as File: " + ((File) gpxInput).getAbsolutePath());
            }

            GPX gpx;
            if (gpxInput instanceof String) {
                gpx = GPX.reader().read(new ByteArrayInputStream(((String) gpxInput).getBytes(StandardCharsets.UTF_8)));
            } else if (gpxInput instanceof File) {
                gpx = GPX.reader().read((File) gpxInput);
            } else {
                throw new IllegalArgumentException("Input must be either String or File");
            }

            double totalDistance = 0.0;
            System.out.println("Tracks found: " + gpx.getTracks().size());

            for (Track track : gpx.getTracks()) {
                System.out.println("Track segments: " + track.getSegments().size());
                for (TrackSegment segment : track.getSegments()) {
                    System.out.println("Segment points: " + segment.getPoints().size());
                    if (segment.getPoints().size() >= 2) {
                        WayPoint start = segment.getPoints().get(0);
                        WayPoint end = segment.getPoints().get(segment.getPoints().size() - 1);
                        double segmentDistance = start.distance(end).to(Length.Unit.KILOMETER);
                        System.out.println("Start: " + start + ", End: " + end + ", Segment distance: " + segmentDistance);
                        totalDistance += segmentDistance;
                    }
                }
            }
            
            String formattedDistance = String.format("%.2f", totalDistance)
                    .replaceAll("0*$", "")
                    .replaceAll("\\.$", "");

            System.out.println("Total distance: " + formattedDistance);

            return Double.parseDouble(formattedDistance);

        } catch (Exception e) {
            System.out.println("Error parsing GPX data: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    public double calculateDistance(String gpxCoordinates) {
        return calculateDistance((Object) gpxCoordinates);
    }

    public double calculateDistanceFromFile(String path) {
        return calculateDistance(new File(path));
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
        return firstSegment.getPoints().get(firstSegment.getPoints().size() - 1);
    }

}
