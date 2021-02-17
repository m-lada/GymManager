package pl.maciek.app.activities;

import org.springframework.format.annotation.DateTimeFormat;
import pl.maciek.app.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String hallName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime term;

    @ManyToMany(mappedBy = "activities",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> participants;

    public Activity() {
    }

    public void addParticipant(User user) {
        this.participants.add(user);
        user.getActivities().add(this);
    }

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

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalDateTime getTerm() {
        return term;
    }

    public void setTerm(LocalDateTime date) {
        this.term = date;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", hallName='" + hallName + '\'' +
                ", term=" + term +
                '}';
    }
}
