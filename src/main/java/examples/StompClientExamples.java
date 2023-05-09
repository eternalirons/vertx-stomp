/*
 *  Copyright (c) 2011-2015 The original author or authors
 *  ------------------------------------------------------
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *       The Eclipse Public License is available at
 *       http://www.eclipse.org/legal/epl-v10.html
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.ext.stomp.StompClient;
import io.vertx.ext.stomp.StompClientOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class StompClientExamples {

  public void example1(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        // use the connection
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example2(Vertx vertx) {
    StompClient.create(vertx)
      .connect(61613, "0.0.0.0")
      .onSuccess(connection -> {
        // use the connection
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example21(Vertx vertx) {
    StompClient.create(vertx)
      .errorFrameHandler(frame -> {
        // Received the ERROR frame
      })
      .connect(61613, "0.0.0.0")
      .onSuccess(connection -> {
        // use the connection
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example3(Vertx vertx) {
    StompClient
      .create(vertx, new StompClientOptions().setHost("localhost").setPort(1234))
      .connect()
      .onSuccess(connection -> {
        // use the connection
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example4(Vertx vertx) {
    StompClient client = StompClient
      .create(vertx, new StompClientOptions().setHost("localhost").setPort(1234));

    client
      .connect()
      .onSuccess(connection -> {
        // use the connection
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));

    client.close();
  }

  public void example5(Vertx vertx) {
    StompClient
      .create(vertx, new StompClientOptions().setHost("localhost").setPort(1234))
      .connect()
      .onSuccess(connection -> {
        // use the connection
        connection.disconnect();
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example6(Vertx vertx) {
    StompClient
      .create(vertx, new StompClientOptions().setHost("localhost").setPort(1234))
      .connect()
      .onSuccess(connection -> {
        // use the connection
        connection
          .errorHandler(frame ->
            System.out.println("ERROR frame received : " + frame));
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example8(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        // use the connection
        connection.subscribe("/queue", frame ->
          System.out.println("Just received a frame from /queue : " + frame));
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example9(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        // use the connection
        connection.subscribe("/queue", frame ->
          System.out.println("Just received a frame from /queue : " + frame));

        // ....

        connection.unsubscribe("/queue");
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example10(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        connection.send("/queue", headers, Buffer.buffer("Hello"));
        // No headers:
        connection.send("/queue", Buffer.buffer("World"));
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example11(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        connection.subscribe("/queue", frame -> {
          connection.ack(frame.getAck());
          // OR
          connection.nack(frame.getAck());
        });
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example12(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        Map<String, String> headers = new HashMap<>();
        headers.put("transaction", "my-transaction");
        connection.beginTX("my-transaction");
        connection.send("/queue", headers, Buffer.buffer("Hello"));
        connection.send("/queue", headers, Buffer.buffer("World"));
        connection.send("/queue", headers, Buffer.buffer("!!!"));
        connection.commit("my-transaction");
        // OR
        connection.abort("my-transaction");
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example13(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {
        connection
          .send("/queue", Buffer.buffer("Hello"))
          .onSuccess(frame ->
            System.out.println("Message processed by the server"));
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }

  public void example14(Vertx vertx) {
    StompClient.create(vertx)
      .connect()
      .onSuccess(connection -> {

        connection.connectionDroppedHandler(con -> {
          // The connection has been lost
          // You can reconnect or switch to another server.
        });

        connection.send("/queue", Buffer.buffer("Hello"))
          .onSuccess(frame -> System.out.println("Message processed by the server")
          );
      })
      .onFailure(err ->
        System.out.println(
          "Failed to connect to the STOMP server: " + err.toString()));
  }
}
