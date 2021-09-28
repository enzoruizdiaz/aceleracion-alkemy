package ProjectAlkemy.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Testimonials;


@Repository
public interface TestimonialsRespository extends BaseRepository<Testimonials, Long> {

    Page<Testimonials> findAllByOrderByCreateAt(Pageable pageable);
    Page<Testimonials>findAllBySoftDeleteFalseOrderByCreateAt(Pageable pageable);
    Optional<Testimonials> findByName(String name);
}
