package prototyping.backchannel.rsocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RSocketIncreaseDo {

  @JsonProperty("increaseValue")
  private int increaseValue;

}
