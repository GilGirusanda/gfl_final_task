package org.project.fin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "crime_group")
@AllArgsConstructor
@NoArgsConstructor
public class CrimeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "criminal_crimegroup",
    joinColumns = {@JoinColumn(name = "crimegroup_id")},
    inverseJoinColumns = {@JoinColumn(name = "criminal_id")})
    private Set<Criminal> members = new HashSet<>();

}
