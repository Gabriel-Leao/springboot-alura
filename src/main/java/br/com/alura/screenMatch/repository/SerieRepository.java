package br.com.alura.screenMatch.repository;

import br.com.alura.screenMatch.model.Genre;
import br.com.alura.screenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String title);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Genre genre);
}
