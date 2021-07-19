<template>
  <div class="hello">
    <div>
      <div>
        Polling
      </div>
      <div>
        <button @click="pollingIncrease">+100</button>
      </div>
      <div>
        Server counter latest:
        {{counterPolling}}
      </div>
    </div>
    <br>
    <div>
      <div>
        Sse
      </div>
      <div>
        <button @click="sseIncrease">+100</button>
      </div>
      <div>
        Server counter latest:
        {{counterSse}}
      </div>
    </div>
    <br>
    <div>
      <div>
        RSocket
      </div>
      <div>
        <button @click="rSocketIncrease">+100</button>
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
import {APPLICATION_JSON, MESSAGE_RSOCKET_ROUTING} from "rsocket-core/build";

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

let rSocket;

async function startListeningToRSocket() {
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
      dataMimeType: APPLICATION_JSON.string,
      metadataMimeType: MESSAGE_RSOCKET_ROUTING.string
    },
    responder: new RSocketResponder(rSocketFireAndForgetHandler),
    transport: new RSocketWebSocketClient({url: "ws://localhost:7000/rsocket"})
  });
  rSocket = await client.connect();
}

export default {
  data() {
    return {
      counterPolling: 'not received yet',
      counterSse: 'not received yet',
      counterRSocket: 'not received yet'
    }
  },

  mounted() {
    performPolling.call(this);
    startListeningToSse.call(this);
    startListeningToRSocket.call(this);
  },

  methods: {
    pollingIncrease() {
      axios
      .post('http://localhost:8080/state', {increaseValue: 100})
    },
    sseIncrease() {
      axios
      .post('http://localhost:8080/sse', {increaseValue: 100})
    },
    rSocketIncrease() {
      rSocket
      .fireAndForget({
        data: {increaseValue: 100},
        metadata: String.fromCharCode(10) + 'client-2-s', //todo find out why this needs to have 10 chars
      })
    }
  }
}
</script>
<style scoped>

</style>
