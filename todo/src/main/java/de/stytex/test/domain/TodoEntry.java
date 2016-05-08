package de.stytex.test.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TodoEntry.
 */
@Entity
@Table(name = "todo_entry")
public class TodoEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "percent_done")
    private Integer percentDone;

    @Column(name = "dead_line")
    private LocalDate deadLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(Integer percentDone) {
        this.percentDone = percentDone;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TodoEntry todoEntry = (TodoEntry) o;
        if(todoEntry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, todoEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TodoEntry{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", percentDone='" + percentDone + "'" +
            ", deadLine='" + deadLine + "'" +
            '}';
    }
}
