package org.project.fin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "archive")
@AllArgsConstructor
@NoArgsConstructor
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "archive_date", nullable = false)
    private Date archiveDate;

    @OneToOne
    @JoinColumn(name = "criminal_id")
    private Criminal criminal;

}
