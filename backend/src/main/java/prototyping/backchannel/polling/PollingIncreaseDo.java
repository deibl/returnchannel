package prototyping.backchannel.polling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PollingIncreaseDo {

  @JsonProperty("increaseValue")
  private int increaseValue;

}
