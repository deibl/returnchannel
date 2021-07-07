<template>
  <div class="hello">
    <div>
      <div>
        Polling
      </div>
      <div>
        Server counter latest:
        {{counterPolling}}
      </div>
    </div>
    <div>
      <div>
        Sse
      </div>
      <div>
        Server counter latest:
        {{counterSse}}
      </div>
    </div>
    <div>
      <div>
        RSocket
      </div>
      <div>
        Server counter latest:
        {{counterRSocket}}
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";

import {
  IdentitySerializer,
  JsonSerializer,
  RSocketClient
} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";

function performPolling() {
  setInterval(() => {
    axios
    .get('http://localhost:8080/state')
    .then(response => this.counterPolling = response.data)
  }, 200);
}


function startListeningToSse() {
  const evtSource = new EventSource("http://localhost:8080/sse");
  evtSource.addEventListener("counterSse", (event) => {
    this.counterSse = event.data;
  });
}

class RSocketResponder {
  constructor(callback) {
    this.callback = callback;
  }
  fireAndForget(payload) {
    this.callback(payload.data.dataUtf8);
  }
}
function startListeningToRSocket() {
  const rSocketFireAndForgetHandler = data => {
    this.counterRSocket = data;
  };
  const client = new RSocketClient({
    serializers: {
      data: JsonSerializer,
      metadata: IdentitySerializer
    },
    setup: {
      keepAlive: 60000,
      lifetime: 180000,
      dataMimeType: "application/json",
      metadataMimeType: "message/x.rsocket.routing.v0"
    },
    responder: new RSocketResponder(rSocketFireAndForgetHandler),
    transport: new RSocketWebSocketClient({url: "ws://localhost:7000/rsocket"})
  });

  client.connect().subscribe({
    onComplete: () => {
      console.log('RSocket connected');
    },
    onError: error => console.log(`RSocket error: ${error.message}`),
  });
}

export default {
  data() {
    return {
      counterPolling: 'not received yet',
      counterSse: 'not received yet',
      counterRSocket: 'not received yet'
    }
  },

  mounted () {
    performPolling.call(this);
    startListeningToSse.call(this);
    startListeningToRSocket.call(this);
  }
}
</script>
<style scoped>

</style>
