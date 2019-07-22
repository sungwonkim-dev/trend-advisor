package module;

import utils.ElementsParser;
import utils.HttpConnector;
import utils.JSONParser;

public class NaverShoppingInsightExtractor {
    private ElementsParser elementsParser;
    private HttpConnector httpConnector;
    private JSONParser jsonParser;

    protected void init() {
        this.elementsParser = new ElementsParser();
        this.httpConnector = new HttpConnector();
        this.jsonParser = new JSONParser();
    }
}
