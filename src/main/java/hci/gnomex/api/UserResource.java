/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
package hci.gnomex.api;

import hci.ri.auth.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;

import hci.gnomex.api.dto.RoleDTO;
import hci.gnomex.api.dto.UserDTO;
import hci.gnomex.model.AppUser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * A resource for operating on user entities in the system.
 *
 * @author Cody Haroldsen <cody.haroldsen@hci.utah.edu>
 * @since 11/30/2016
 */
@Path("/user")
public class UserResource {
  private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

  @Inject
  private UserService userService;

  public UserResource() {
  }

  UserResource(UserService userService) {
    this.userService = userService;
  }

  /**
   * An endpoint to get a representation of the currently authenticated user domain entity.
   *
   * @param req the servlet request
   * @return a representation of this user which will end up communicating a success response (status 200) with the
   * representation of the authenticated user.
   */
  @GET
  @Path("/authenticated")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAuthenticatedUser(@Context HttpServletRequest req, @Context UriInfo uriInfo) throws Exception {

    Subject subject;
    try {
      subject = SecurityUtils.getSubject();
    } catch (IllegalStateException e) {
      LOG.error("BUG ALERT! Request made to secure endpoint without an authenticated subject.");
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    PrincipalCollection principalCollection = subject.getPrincipals();

    // TODO: JEH (10.27.17) - Is this the preferred way to get the User? Should it be in the UserService?
    AppUser user = null;
    if(principalCollection != null) {
      user = principalCollection.oneByType(AppUser.class);
    }

    Integer idUser = userService.getIdUser(principalCollection);

    Collection<RoleDTO> roles = userService.getRoles(principalCollection, null)
            .stream()
            .map(role -> RoleDTO.Builder.createRole().setRoleName(role).build())
            .collect(Collectors.toCollection(HashSet::new));

    // TODO: JEH (10-27-16) - Determine how to handle permissions, if they are necessary on the client.

    UserDTO userDTO = UserDTO.Builder.createUser()
            .setHref(calculateUserLocation(idUser, uriInfo.getAbsolutePathBuilder()))
            .setFirstname(user != null ? user.getFirstName() : null)
            .setLastname(user != null ? user.getLastName() : null)
            .setId(idUser)
            .setUsername(user != null ? ((user.getuNID() != null)?user.getuNID():user.getUserNameExternal()) : null)
            .setEmail(user != null ? user.getEmail() : null)
            .setRoles(roles)
            .build();

    return Response.ok(userDTO).build();
  }

  @GET
  @Path("/{id : \\d+}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") Integer idUser) throws Exception {
    //return Response.ok(userService.getUser(idUser)).build();
    return Response.ok("{}").build();
  }

  private URI calculateUserLocation(int userId, UriBuilder builder) {
    return builder.path(UserResource.class, "getUser")
            .resolveTemplate("id", userId)
            .build();
  }
}