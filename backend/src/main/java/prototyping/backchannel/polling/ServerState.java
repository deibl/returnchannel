package prototyping.backchannel.polling;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
@Getter
public class ServerState {

  private int counterPolling = 0;

  @Scheduled(fixedDelay = 200)
  public void increaseCounter() {
    counterPolling++;
  }

  public void increaseCounterBy(int value) {
    counterPolling += value;
  }
}
