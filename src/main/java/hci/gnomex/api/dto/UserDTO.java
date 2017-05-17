/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */

package hci.gnomex.api.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * A thread-safe DTO implementation for marshalling user data to and from the REST api.
 *
 * @author brandony
 * @since 8/22/16
 */
public final class UserDTO implements Serializable {
  private final int id;
  private final URI href;
  private final String username;
  private final String email;
  private final String firstname;
  private final String lastname;
  private final Collection<RoleDTO> roles;

  private UserDTO(int id,
                  URI href,
                  String username,
                  String email,
                  String firstname,
                  String lastname,
                  Collection<RoleDTO> roles) {
    this.id = id;
    this.href = href;
    this.username = username;
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roles = roles;
  }

  public int getId() {
    return id;
  }

  public URI getHref() {
    return href;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public Collection<RoleDTO> getRoles() {
    return Collections.unmodifiableCollection(roles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDTO userDTO = (UserDTO) o;
    return id == userDTO.id &&
        Objects.equals(href, userDTO.href) &&
        Objects.equals(username, userDTO.username) &&
        Objects.equals(email, userDTO.email) &&
        Objects.equals(firstname, userDTO.firstname) &&
        Objects.equals(lastname, userDTO.lastname) &&
        Objects.equals(roles, userDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, username, email, firstname, lastname, roles);
  }

  @Override
  public String toString() {
    return "UserDTO{" +
        "id='" + id + '\'' +
        ", href=" + href +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", firstname='" + firstname + '\'' +
        ", lastname='" + lastname + '\'' +
        ", roles=" + roles +
        '}';
  }

  /**
   * A builder for the immutable user DTO providing a fluent api.
   */
  public static class Builder {
    private int id;
    private URI href;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Collection<RoleDTO> roles = new HashSet<>();

    private Builder() {
    }

    public static Builder createUser() {
      return new Builder();
    }

    public Builder setId(int id) {
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

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder setFirstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder setLastname(String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Builder setRoles(Collection<RoleDTO> roles) {
      this.roles = roles;
      return this;
    }

    public Builder addRole(RoleDTO role) {
      roles.add(role);
      return this;
    }

    public Builder setUser(UserDTO userDTO) {
      id = userDTO.getId();
      href = userDTO.getHref();
      username = userDTO.getUsername();
      email = userDTO.getEmail();
      firstname = userDTO.getFirstname();
      lastname = userDTO.getLastname();
      roles = userDTO.getRoles();
      return this;
    }

    public UserDTO build() {
      return new UserDTO(id,
          href,
          username,
          email,
          firstname,
          lastname,
          roles);
    }
  }
}