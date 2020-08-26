package trade.order.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

//@Component
public interface MessageProcessor {

  String APPLICATIONS_IN = "output";
  String ACCEPTED_OUT = "accepted";
  String DECLINED_OUT = "declined";

  @Input(APPLICATIONS_IN)
  SubscribableChannel sourceOfLoanApplications();

  @Output(ACCEPTED_OUT)
  MessageChannel accepted();

  @Output(DECLINED_OUT)
  MessageChannel declined();

}
