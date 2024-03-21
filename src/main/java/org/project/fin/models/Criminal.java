package org.project.fin.models;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedNativeQuery;
import org.project.fin.DTO.CriminalDTO;
import org.project.fin.DTO.CriminalDetailsDTO;
import org.project.fin.models.enums.AttributeType;
import org.project.fin.models.enums.CriminalProfession;

import java.time.LocalDate;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "criminals")
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "internalBuilder")
@DynamicInsert
public class Criminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", nullable = true)
    @ColumnDefault("'Unknown'")
    private String lastName;

    @Column(name = "first_name", nullable = true)
    @ColumnDefault("'Unknown'")
    private String firstName;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "last_case", nullable = false, columnDefinition = "TEXT")
    private String lastCase;

    @Column(name = "criminal_profession", nullable = false)
    @Enumerated(EnumType.STRING)
    private CriminalProfession criminalProfession;

    @ManyToMany(mappedBy = "criminals", cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 20)
    private Set<Language> languages = new HashSet<>();

    @OneToOne(mappedBy = "criminal", cascade = CascadeType.ALL, optional = true)
    private Archive archive;

    @OneToMany(mappedBy = "criminal", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 7)
    private Set<CriminalDetails> criminalDetails = new HashSet<>();

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 5)
    private Set<CrimeGroup> crimeGroups = new HashSet<>();

}
