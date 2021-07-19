package prototyping.backchannel.polling;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class PollingController {

  private final ServerState serverState;

  @GetMapping("/state")
  public PollingDo currentState() {
    final PollingDo pollingDo = new PollingDo();
    pollingDo.setName("polling do");
    pollingDo.setCounter(serverState.getCounterPolling());
    return pollingDo;
  }

  @PostMapping("/state")
  public void increaseCurrentState(@RequestBody PollingIncreaseDo pollingIncreaseDo) {
    serverState.increaseCounterBy(pollingIncreaseDo.getIncreaseValue());
  }
}
