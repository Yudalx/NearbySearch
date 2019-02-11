import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonHelper {
    public static String getBaseUrl( String configPath ) {
        String baseUrl;
        byte[] mapData;
        Map <String, String> jsonMap = new HashMap <>();
        try {
            mapData = Files.readAllBytes(Paths.get(configPath));
            ObjectMapper objectMapper = new ObjectMapper();

            jsonMap = objectMapper.readValue(mapData, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseUrl = jsonMap.get("address") + jsonMap.get("endpoint") + "?key=" + jsonMap.get("key");

    }

    public static ResponseHelper getResponseParam( String response ) {
        ResponseHelper resp = new ResponseHelper();
        byte[] mapData;
        Map <String, Object> jsonMap = new HashMap <>();
        try {
            mapData = response.getBytes("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();

            jsonMap = objectMapper.readValue(mapData, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resp.results = (ArrayList) jsonMap.get("results");
        resp.status = (String) jsonMap.get("status");
        return resp;

    }


    public String createUrl( String location,
                             String radius, String keyword, String rankby, String type,
                             String minprice, String maxprice, String language ) {

        String url = getBaseUrl("src\\main\\resources\\json\\config.json") + location(location) + radius(radius)
                + keyword(keyword) + rankby(rankby) + type(type)
                + minprice(minprice) + maxprice(maxprice) + language(language);

        System.out.println("url: " + url);
        return url;
    }

    private String location( String location ) {
        if (location != null) {
            return "&location=" + location;
        }
        return "";
    }

    private String radius( String radius ) {
        if (radius != null) {
            return "&radius=" + radius;
        }
        return "";
    }

    private String rankby( String rankby ) {
        if (rankby != null) {
            return "&rankby=" + rankby;
        }
        return "";
    }

    private String keyword( String keyword ) {
        if (keyword != null) {
            return "&keyword=" + keyword;
        }
        return "";
    }

    private String type( String type ) {
        if (type != null) {
            return "&type=" + type;
        }
        return "";
    }

    private String minprice( String minprice ) {
        if (minprice != null) {
            return "&minprice=" + minprice;
        }
        return "";
    }

    private String maxprice( String maxprice ) {
        if (maxprice != null) {
            return "&maxprice=" + maxprice;
        }
        return "";
    }

    private String language( String language ) {
        if (language != null) {
            return "&language=" + language;
        }
        return "";
    }

}