package backend.academy.scrapper.repositories;

import backend.academy.scrapper.entities.Chat;
import backend.academy.scrapper.entities.Link;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {


    Set<Link> getAllByChats(List<Chat> chats);

    List<Link> findAllByUrl(@NotNull String url);
}
