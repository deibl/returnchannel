package prototyping.backchannel.polling;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
