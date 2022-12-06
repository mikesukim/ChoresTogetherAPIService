package chorestogetherapiservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChoresTogetherAPIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChoresTogetherAPIService.class);
    public static void main(String[] args) {
        ChoresTogetherAPIService choresTogetherAPIService = new ChoresTogetherAPIService();
        System.out.print(choresTogetherAPIService.sayHello());
        LOGGER.debug("Logger testing");
    }
    public String sayHello(){
        return "SayHello";
    }
}
