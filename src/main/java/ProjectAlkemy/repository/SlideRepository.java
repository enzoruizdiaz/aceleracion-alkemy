package ProjectAlkemy.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ProjectAlkemy.model.Slide;

@Repository
public interface SlideRepository extends BaseRepository<Slide,Long> {

    @Modifying
    @Transactional
    @Query(value = "update slides SET image_url =?2, text =?3, slide_order =?4, organization_id =?5 WHERE id = ?1",nativeQuery = true)
    Integer update(Long id,String img,String text,Integer order,Long idOrganization);
    
    @Query(value = "SELECT order_slide FROM slides ORDER BY order_slide DESC LIMIT 1", nativeQuery = true)
    Integer getLastOrder();
    
    @Query(value = "SELECT * FROM slides WHERE order_slide =?1", nativeQuery = true)
    Optional<Slide> getSlideByOrder(Integer orderSlide);
    
    @Query(value = "SELECT * FROM slides ORDER BY order_slide ASC", nativeQuery = true)
    List<Slide> getSlidesOrderer();
    
    @Query(value = "SELECT * FROM slides s WHERE s.organization_id =?1 ORDER BY s.order_slide ASC", nativeQuery = true)
    List<Slide> slidesOrderId(Long id);
}
