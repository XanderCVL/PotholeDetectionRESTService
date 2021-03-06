package prototype;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.GeoCoordinates;
import json.OSMAddressNode;
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TryHTTPRequest {

    static final Pattern regex = Pattern.compile("(?:\"address\":)\\{(.*?)\\}");
    static final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) throws IOException {
        tryMarkerAdding();
    }

    private static final void tryMarkerAdding() throws IOException{
//        OSMAddressNode osmAddrNode = new OSMAddressNode(260,
//                "Broadway",
//                "null",
//                "Civic Center",
//                "New York County",
//                "null",
//                "10003",
//                "United States of America",
//                "us",
//                "null");
        GeoCoordinates coordinates = new GeoCoordinates(40.7128, -74.0060);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/add")
                .post(RequestBody.create(MediaType.parse("application/json"), gson.toJson(coordinates)))
                .build();

        Response response = client.newCall(request).execute();
    }


    private static final void tryReverseGeocoding() throws IOException{
        String json = "";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + 40.7128 + "&lon=" + -74.0060)
                .build();

        Response response = client.newCall(request).execute();

//      System.out.println(response.body().string());

        assert response.body() != null;
        Matcher matcher = regex.matcher(response.body().string());

        if (matcher.find()){
            String address = matcher.group(1);

            address = address.replaceFirst("address[0-9]+", "place");
            address = address.replaceFirst("suburb", "town");
            address = address.replaceFirst("village", "neighbourhood");
            address = address.replaceFirst("country_code", "countryCode");
            address = address.replaceFirst("house_number", "houseNumber");

            System.out.println(address);

            OSMAddressNode node = gson.fromJson("{" + address + "}", OSMAddressNode.class);

            System.out.println(node.toString());
        }

    }
}
