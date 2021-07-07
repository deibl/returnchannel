package prototyping.backchannel.serversentevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SseDo {

  @JsonProperty("name")
  private String name;

  @JsonProperty("counter")
  private int counter;
}
