package trade.order.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(MessageProcessor.class)
public class TradeOrderProcessorApplication {

  public static final Logger log = LoggerFactory.getLogger(TradeOrderProcessorApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TradeOrderProcessorApplication.class, args);
    log.info("The Order Processor Application has started...");
  }
  


}
