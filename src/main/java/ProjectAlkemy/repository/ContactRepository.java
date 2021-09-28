package ProjectAlkemy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Contact;

@Repository
public interface ContactRepository extends BaseRepository<Contact, Long>{

    List<Contact> findAllBySoftDeleteFalseOrderByCreateAt();
}
