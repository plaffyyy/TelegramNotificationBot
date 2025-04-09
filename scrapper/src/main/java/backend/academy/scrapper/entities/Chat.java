package backend.academy.scrapper.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    public Chat(Long id) {
        this.id = id;
    }

    @Id
    private Long id;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Link> links;
}
