package hci.gnomex.api.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * A thread-safe DTO for marshalling and user date to and from the REST api.
 *
 * @author jasonholmberg <jason.holmberg@hci.utah.edu>
 * @since 10/27/16.
 */
public class RoleDTO implements Serializable {
    private final String roleName;
    private final Collection<String> permissions;

    private RoleDTO(String role, Collection<String> permissions) {
        this.roleName = role;
        this.permissions = permissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(roleName, roleDTO.roleName) &&
                Objects.equals(permissions, roleDTO.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, permissions);
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "roleName='" + roleName + '\'' +
                ", permissions=" + permissions +
                '}';
    }

    public static class Builder {
        private String roleName;
        private Collection<String> permissions = new HashSet<>();

        private Builder() {
        }

        public static Builder createRole() {
            return new Builder();
        }

        public Builder setRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder setPermissions(Collection<String> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder addPermisson(String permission) {
            this.permissions.add(permission);
            return this;
        }

        public Builder setRole(RoleDTO roleDTO) {
            this.roleName = roleDTO.getRoleName();
            this.permissions = roleDTO.getPermissions();
            return this;
        }
        
        public RoleDTO build() {
            return new RoleDTO(this.roleName, this.permissions);
        }
    }
}
