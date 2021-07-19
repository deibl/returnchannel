package prototyping.backchannel.serversentevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SseIncreaseDo {

  @JsonProperty("increaseValue")
  private int increaseValue;

}
