package util;

import java.net.URI;
import java.net.URISyntaxException;

public class StringUtils {
    public String addUrlParameter(String url, String key, String value) {
        if (org.apache.commons.lang3.StringUtils.isBlank(url) ||
                org.apache.commons.lang3.StringUtils.isBlank(key) ||
                org.apache.commons.lang3.StringUtils.isBlank(value)) {

            url = replaceUrlForSpecialSymbolAtTail(url);
            if (isValidURI(url)) {
                if (org.apache.commons.lang3.StringUtils.containsNone(url, "?"))
                    url = String.format("%s?%s=%s", url, key, value);
                else
                    url = String.format("%s&%s=%s", url, key, value);
            }
        }
        return url;
    }

    private boolean isValidURI(String uriStr) {
        try {
            URI uri = new URI(uriStr);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private String replaceUrlForSpecialSymbolAtTail(String url) {
        for (int index = url.length() - 1; index > -1; index--)
            if (!String.valueOf(url.charAt(index)).matches("[^a-zA-Z0-9_-]")) {
                url = url.substring(0, index);
                break;
            }
        return url;
    }

}
