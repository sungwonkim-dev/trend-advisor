package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONParser {
    private JsonParser jsonParser;

    public void init() {
        this.jsonParser = new JsonParser();
    }

    public String parseValueByKeyInJsonObject(JsonObject jsonObject, String key) {
        return "";
    }

    public JsonObject parseJsonObjectByKey(JsonObject jsonObject, String key) {
        return new JsonObject();
    }
}
