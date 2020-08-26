package trade.order.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TradeGeneratorApplication {
	
	  private static final Logger log = LoggerFactory.getLogger(TradeGeneratorApplication.class);
	  private String [] accountIds = {"U1234", "M33445", "D12345", "U112345", "D11900", "M67890", "N1234"}; 
	  private String [] symbolIds = {"TSLA", "MSFT", "AAPL", "VOO", "SAP","ORACLE", "IBKR"};
	  private List<Integer> quantities = Arrays.asList(10,20,30,15,12,11,40,50,-2,-3,-4, -5,-8);
	  private List<Integer> prices = Arrays.asList(10,20,30,15,12,11,40,50,100,200);
	  
	  public static void main(String[] args) {
		    SpringApplication.run(TradeGeneratorApplication.class, args);
		    log.info("The Trading Order Application has started...");
		  }	  

	  @Bean
	  public Supplier<TradeOrder> supplyOrder(){

	    Supplier<TradeOrder> orderSupplier = () -> {
	      TradeOrder order = new TradeOrder(getRandomString(accountIds),
	    		  getRandomString(symbolIds),
	    		  quantities.get(new Random().nextInt(quantities.size())),
	              prices.get(new Random().nextInt(prices.size())));
	      log.info("{} {} quantity {} for ${} each", order.getAccountId(), order.getSymbolId(), order.getQuantity(), order.getPrice());
	      return order;
	    };
	    return orderSupplier;
	  }
	  

	  static String getRandomString(String [] arr){
        int r = (int) (Math.random()*5);
        String name = arr[r];
        return name;
	  }

}
