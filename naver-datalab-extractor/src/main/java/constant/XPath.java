package constant;

public final class XPath {
    public static final String N_CATEGORY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[1]/div/div[1]/span";
    public static final String N_STRING_FORMAT_CATEGORY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[1]/div/div[1]/ul/li/a[contains(text(), '%s')]";

    public static final String N_START_YEAR = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[1]/span";
    public static final String N_START_MONTH = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[2]/span";
    public static final String N_START_DAY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[3]/span";

    public static final String N_END_YEAR = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[3]/div[1]/span";
    public static final String N_END_MONTH = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[3]/div[2]/span";
    public static final String N_END_DAY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[3]/div[3]/span";

    public static final String N_STRING_FORMAT_START_YEAR = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[1]/ul/li/a[contains(text(),'%s')]";
    public static final String N_STRING_FORMAT_START_MONTH = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[2]/ul/li/a[contains(text(),'%s')]";
    public static final String N_STRING_FORMAT_START_DAY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[3]/ul/li/a[contains(text(),'%s')]";

    public static final String N_STRING_FORMAT_END_YEAR = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[1]/ul/li/a[contains(text(),'%s')]";
    public static final String N_STRING_FORMAT_END_MONTH = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[2]/ul/li/a[contains(text(),'%s')]";
    public static final String N_STRING_FORMAT_END_DAY = "//*[@id=\"content\"]/div[2]/div/div[1]/div/div/div[2]/div[2]/span[1]/div[3]/ul/li/a[contains(text(),'%s')]";

    public static final String N_SEARCH = "//*[@id=\"content\"]/div[2]/div/div[1]/div/a";
    public static final String N_NEXT_PAGE = "//*[@id=\"content\"]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/a[2]";
}
