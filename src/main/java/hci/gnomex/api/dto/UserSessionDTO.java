/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
package hci.gnomex.api.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * A thread-safe DTO implementation for marshalling user session data to and from the REST api.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 10/3/16
 */
public final class UserSessionDTO implements Serializable {
  private final Serializable id;
  private final URI href;
  private final String username;
  private final String password;

  public UserSessionDTO(Serializable id, URI href, String username, String password) {
    this.id = id;
    this.href = href;
    this.username = username;
    this.password = password;
  }

  public Serializable getId() {
    return id;
  }

  public URI getHref() {
    return href;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserSessionDTO that = (UserSessionDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(href, that.href) &&
        Objects.equals(username, that.username) &&
        Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, username, password);
  }

  @Override
  public String toString() {
    return "UserSessionDTO{" +
        "id='" + id + '\'' +
        ", href=" + href +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

  /**
   * A builder for the immutable user session DTO providing a fluent api.
   */
  public static class Builder {
    private Serializable id;
    private URI href;
    private String username;
    private String password;

    private Builder() {
    }

    public static Builder createUserSession() {
      return new Builder();
    }

    public Builder setId(Serializable id) {
      this.id = id;
      return this;
    }

    public Builder setHref(URI href) {
      this.href = href;
      return this;
    }

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public UserSessionDTO build() {
      return new UserSessionDTO(id, href, username, password);
    }
  }
}
