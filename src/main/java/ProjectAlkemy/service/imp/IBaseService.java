package ProjectAlkemy.service.imp;

import java.util.List;

import ProjectAlkemy.model.Base;

public interface IBaseService <E extends Base, ID > {

    public E create(E entity) throws Exception;
    public List<E> getAll() throws Exception;
    public E getById(ID id) throws Exception;
    public E update(E entity) throws Exception;
    public void deleteById(ID id) throws Exception;
}
