package marketing;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.support.JmsGatewaySupport;

import domain.Motorist;

public class RantzMarketingGatewayImpl 
    extends JmsGatewaySupport
    implements RantzMarketingGateway {
  public RantzMarketingGatewayImpl() {}
  
  public void sendMotoristInfo(final Motorist motorist) {
    getJmsTemplate().convertAndSend(motorist);
  }

}
