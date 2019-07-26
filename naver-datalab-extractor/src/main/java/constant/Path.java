package constant;

public class Path {
    public static final String ABS_DEST_WEBDRIVER_PATH = "/webdriver/chromedriver2.exe";
    public static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    public static final String WEBDRIVER_COPY_PATH = String.format("%s/%s", WORKING_DIRECTORY, ABS_DEST_WEBDRIVER_PATH);
    public static final String CHROME_WEBDRIVER_ID = "webdriver.chrome.driver";
    public static final String WEBDRIVER_PATH_IN_RESOURCE = "/webdriver/chromedriver2.exe";

    public static final String ABS_DEST_TEST_HTML_PATH = "/test/home.txt";
    public static final String TEST_HTML_COPY_PATH = String.format("%s/%s", WORKING_DIRECTORY, ABS_DEST_TEST_HTML_PATH);
    public static final String TEST_HTML_PATH_IN_RESOURCE = "/test/home.txt";
}
