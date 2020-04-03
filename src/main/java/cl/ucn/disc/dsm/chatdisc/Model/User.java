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

public class User {

  /**
   * The id.
   */
  private String id;
  /**
   * The username.
   */
  private String username;
  /**
   * The imageUrl.
   */
  private String imageURL;
  /**
   * The status.
   */
  private String status;
  /**
   * The search.
   */
  private String search;

  /**
   * The Constructor.
   *
   * @param id
   * @param username
   * @param imageURL
   * @param status
   * @param search
   */
  public User(String id, String username, String imageURL, String status, String search) {
    this.id = id;
    this.username = username;
    this.imageURL = imageURL;
    this.status = status;
    this.search = search;
  }

  //Get's and Set's

  public User() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }
}
