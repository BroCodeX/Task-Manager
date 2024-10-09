package hexlet.code.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "statuses")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Min(value = 1)
    private String name;

    @NotBlank
    @Column(unique = true)
    @Min(value = 1)
    private String slug;

    @CreatedDate
    private LocalDate createdAt;
}
