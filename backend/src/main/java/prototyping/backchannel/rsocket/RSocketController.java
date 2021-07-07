package prototyping.backchannel.rsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;
import io.rsocket.util.ByteBufPayload;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class RSocketController {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private int counterRSocket = 0;
  private final BlockingQueue<RSocketRequester> blockingQueue = new LinkedBlockingQueue<>();

  @ConnectMapping
  void newClientConnect(RSocketRequester requester) {
    System.out.println("new client connected via RSocket");
    blockingQueue.add(requester);
    requester
        .rsocket()
        .onClose()
        .subscribe(null, null, () -> {
          System.out.println("client connection closed via RSocket");
          blockingQueue.remove(requester);
        });
  }

  @Scheduled(fixedDelay = 300)
  public void increaseCounter() {
    blockingQueue.forEach(consumer -> {
      final RSocketDo rSocketDo = new RSocketDo();
      rSocketDo.setCounter(counterRSocket++);
      rSocketDo.setName("rsocket do");
      try {
        final Payload payload = ByteBufPayload.create(objectMapper.writeValueAsBytes(rSocketDo));
        consumer.route("fancy-route")
            .data(payload)
            .send()
            .block();
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
