package ProjectAlkemy.service.imp;

import java.util.List;
import java.util.Optional;

import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Base;
import ProjectAlkemy.repository.BaseRepository;

public abstract class BaseServiceImpl<E extends Base,ID > implements IBaseService<E,ID> {

    protected BaseRepository<E,ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E,ID> baseRepository) {

        this.baseRepository = baseRepository;
    }

    @Override
    public E create(E entity) {
        E e =baseRepository.save(entity);
        return e;
    }

    @Override
    public List<E> getAll() throws ListNotFoundException{
        List<E> list = baseRepository.findAll();
        return Optional.ofNullable(list).orElseThrow(() -> new ListNotFoundException("No record to list"));
    }

    @Override
    public E getById(ID id) throws RecordNotExistException {

        Optional<E> e = baseRepository.findById(id);
        return e.orElseThrow(() -> new RecordNotExistException("The record was not found "+id));
    }

    @Override
    public E update(E entity) {
        E e= baseRepository.save(entity);
        return e;
    }

    @Override
    public void deleteById(ID id) throws RecordNotExistException {
        Optional<E> e = baseRepository.findById(id);
        e.orElseThrow(() -> new RecordNotExistException("Record was not found"));
        baseRepository.delete(e.get());
    }

}
