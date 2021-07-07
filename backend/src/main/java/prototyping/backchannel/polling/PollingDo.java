package prototyping.backchannel.polling;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PollingDo {

  @JsonProperty("name")
  private String name;

  @JsonProperty("counter")
  private int counter;
}
