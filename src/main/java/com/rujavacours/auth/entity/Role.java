package com.rujavacours.auth.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "ROLE_DATA", schema = "tasklist", catalog = "postgres")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;


//    private Collection<UserRoleEntity> userRolesById;
//    @OneToMany(mappedBy = "roleDataByRoleId")
//    public Collection<UserRoleEntity> getUserRolesById() {
//        return userRolesById;
//    }
//
//    public void setUserRolesById(Collection<UserRoleEntity> userRolesById) {
//        this.userRolesById = userRolesById;
//    }
}
