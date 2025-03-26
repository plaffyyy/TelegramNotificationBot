package backend.academy.scrapper.repositories;

import backend.academy.scrapper.entities.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {


}
