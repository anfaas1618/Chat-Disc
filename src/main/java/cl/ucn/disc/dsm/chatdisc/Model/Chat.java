/*
 * Copyright [2020] [Martin Osorio Bugueño]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package cl.ucn.disc.dsm.chatdisc.Model;

/**
 * @author Martin Osorio-Bugueño.
 */

public class Chat {

  /**
   * The sender.
   */
  private String sender;
  /**
   * The receiver.
   */
  private String receiver;
  /**
   * The message.
   */
  private String message;
  /**
   * The timestamp.
   */
  private String timestamp;

  private boolean isseen;

  /**
   * The Constructor.
   *
   * @param sender
   * @param receiver
   * @param message
   * @param timestamp
   */

  public Chat(String sender, String receiver, String message, boolean isseen, String timestamp) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
    this.isseen = isseen;
    this.timestamp = timestamp;
  }

  //Get's and Set's
  public Chat() {
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isIsseen() {
    return isseen;
  }

  public void setIsseen(boolean isseen) {
    this.isseen = isseen;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}

