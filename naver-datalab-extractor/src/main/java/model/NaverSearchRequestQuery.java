package model;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class NaverSearchRequestQuery {
    private String cliId;
    private String cliSecret;

    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<Map> category;
    private String divice;
    private String gender;
    private List<String> ages;

}
