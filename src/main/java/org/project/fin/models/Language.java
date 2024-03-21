package org.project.fin.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "languages")
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "language")
    private String language;

    @ManyToMany
    @JoinTable(
            name = "criminal_language",
            joinColumns = {@JoinColumn(name = "language_id")},
            inverseJoinColumns = {@JoinColumn(name = "criminal_id")})
    private Set<Criminal> criminals = new HashSet<>();
}
