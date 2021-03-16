package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersId implements Serializable {
    private Integer userid;

    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersId usersId = (UsersId) o;
        return Objects.equals(userid, usersId.userid) && Objects.equals(type, usersId.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, type);
    }
}
