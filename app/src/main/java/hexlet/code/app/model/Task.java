package hexlet.code.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotNull
    private Integer index;

    @NotNull
    private String description;

    @NotNull
    @OneToOne
    @JoinColumn(name = "status_id")
    private Status taskStatus;

    @ManyToOne
    private User assignee;

    @CreatedDate
    private LocalDate createdAt;
}
