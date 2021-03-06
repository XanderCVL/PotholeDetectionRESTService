package json;

import core.exceptions.FormatException;
import utils.Formatting;
import utils.Utils;

import java.util.Objects;
import java.util.Optional;

public class GeoCoordinates {

    private final Double lng;
    private final Double lat;
    private final Double radius = Utils.EarthRadius;

    public GeoCoordinates(final Double lng, final Double lat) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Double[] toLngLat() {
        return new Double[]{this.getLng(), this.getLat()};
    }

    public Double[] toLatLng() {
        return new Double[]{this.getLat(), this.getLng()};
    }

    public Double[] toArray() {
        return this.toLngLat();
    }

    @Override
    public String toString() {
        return "[" + this.getLng() + " E," + this.getLat() + " N]";
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoCoordinates)) return false;
        GeoCoordinates that = (GeoCoordinates) o;
        return Double.compare(that.getLat(), getLat()) == 0 &&
                Double.compare(that.getLng(), getLng()) == 0 &&
                Double.compare(that.getRadius(), getRadius()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLat(), getLng(), getRadius());
    }


    public static GeoCoordinates fromString(final String gc) throws Exception {
        return GeoCoordinates.fromString(gc, Formatting.FORMAT.LNG_LAT);
    }

    public static GeoCoordinates fromString(final String gc, final Formatting.FORMAT format) throws Exception {

        final Tuple<Optional<GeoCoordinates>, String> p = Formatting.checkCoordinatesFormat(gc);

        return format.apply(p.getX().orElseThrow(() -> new FormatException(p.getY())).toArray());
    }

    public static GeoCoordinates empty() {
        return new GeoCoordinates(null, null);
    }
}
