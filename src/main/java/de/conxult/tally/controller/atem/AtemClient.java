    /*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem;

import de.conxult.tally.controller.TallyControllerState;
import de.conxult.tally.controller.TallyLogger;
import de.conxult.tally.controller.atem.handler.InputLabelsHandler;
import de.conxult.tally.controller.atem.handler.MessageHandler;
import de.conxult.tally.controller.atem.handler.OutputLabelsHandler;
import de.conxult.tally.controller.atem.handler.UnknownHandler;
import de.conxult.tally.controller.atem.handler.VideoOutputRoutingHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author joerg
 */
@Accessors(chain = true)
public class AtemClient {

  @Setter
  TallyControllerState states;

  AtemConfiguration configuration = new AtemConfiguration();

  String hostname;
  int    port;

  Socket socket;

  SocketReader   socketReader;
  SocketWriter   socketWriter;
  MessageHandler messageHandler;

  public AtemClient(String hostname) {
    this(hostname, 9990);
  }

  public AtemClient(String hostname, int port) {
    this.hostname = hostname;
    this.port = port;
  }

  public AtemClient connect() throws IOException {
    close();
    socket = new Socket(hostname, port);
    socketReader = new SocketReader();
    socketWriter = new SocketWriter();

    return this;
  }

  void reconnect() {
    while (true) {
      try {
        connect();
        sleep(1000);
      } finally {
        return;
      }
    }
  }

  public void close() {
    socketReader = null;
    socketWriter = null;
  }

  class SocketReader extends Thread {
    BufferedReader reader;

    SocketReader() throws IOException {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      start();
    }

    @Override
    public void run() {
      try {
        while (socketReader != null) {
          String line = reader.readLine();
          if (line == null) {
            reconnect();
            return;
          }
          if (messageHandler != null) {
            if (line.isEmpty()) {
              messageHandler.close();
              messageHandler = null;
            } else {
              messageHandler.handleMessage(line);
            }
          } else if (line.equals("VIDEO OUTPUT ROUTING:")) {
            messageHandler = new VideoOutputRoutingHandler(configuration, states);
          } else if (line.equals("INPUT LABELS:")) {
            messageHandler = new InputLabelsHandler(configuration);
          } else if (line.equals("OUTPUT LABELS:")) {
            messageHandler = new OutputLabelsHandler(configuration);
          } else {
            messageHandler = new UnknownHandler(configuration, line);
          }
        }
      } catch (Exception exception) {
        System.err.println("reconnect reader " + exception);
        reconnect();
      }
    }
  }

  class SocketWriter extends Thread {
    PrintWriter writer;

    SocketWriter() throws IOException {
      writer = new PrintWriter(socket.getOutputStream(), true);
      start();
    }

    @Override
    public void run() {
      try {
        while (socketWriter != null) {
          AtemClient.this.sleep(10000);
          TallyLogger.info("SYN StandBy: " + states.getStandBy().getName());
          TallyLogger.info("SYN OnAir  : " + states.getOnAir().getName());
          writer.println("");
        }
      } catch (Exception exception) {
        System.err.println("reconnect writer " + exception);
        reconnect();
      }
    }
  }


  void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (Exception any) {
    }
  }

  public static void main(String... args) throws Exception {
    new AtemClient("10.47.11.81")
      .setStates(new TallyControllerState())
      .connect();
  }

}
