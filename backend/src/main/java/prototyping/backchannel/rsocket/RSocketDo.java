package prototyping.backchannel.rsocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RSocketDo {

  @JsonProperty("name")
  private String name;

  @JsonProperty("counter")
  private int counter;
}
