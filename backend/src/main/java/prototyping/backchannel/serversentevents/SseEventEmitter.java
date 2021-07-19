package prototyping.backchannel.serversentevents;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Slf4j
@RestController
public class SseEventEmitter {

  private int counterSse = 0;
  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  @Scheduled(fixedDelay = 250)
  public void setup() {
    final SseDo sseDo = new SseDo();
    sseDo.setName("sse do");
    sseDo.setCounter(counterSse++);
    SseEventBuilder event = SseEmitter.event()
        .data(sseDo)
        .id(String.valueOf(sseDo.getCounter()))
        .name("counterSse");
    emitters.forEach(e -> {
      try {
        e.send(event);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    log.debug("Sent events to a total of " + emitters.size() + " clients");
  }

  @GetMapping("/sse")
  public SseEmitter streamSseMvc() {
    SseEmitter emitter = new SseEmitter();
    this.emitters.add(emitter);
    emitter.onCompletion(() -> this.emitters.remove(emitter));
    emitter.onTimeout(() -> this.emitters.remove(emitter));
    return emitter;
  }

  @PostMapping("/sse")
  public void increaseCurrentState(@RequestBody SseIncreaseDo sseIncreaseDo) {
    counterSse += sseIncreaseDo.getIncreaseValue();
  }
}
