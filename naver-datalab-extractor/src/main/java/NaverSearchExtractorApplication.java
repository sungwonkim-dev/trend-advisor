import exception.NaverSearchExtractorException;
import module.NaverDataLabExtractor;

import java.io.IOException;

public class NaverSearchExtractorApplication {
    public static void main(String args[]) throws NaverSearchExtractorException, IOException {
//        final String N_CLI_ID_SW = "t2phL7o1nlS6MqRByr78";
//        final String N_CLI_PW_SW = "KnJh6CVC7T";
        NaverDataLabExtractor naverDataLabExtractor = new NaverDataLabExtractor();
        naverDataLabExtractor.init();
        naverDataLabExtractor.run();


    }
}