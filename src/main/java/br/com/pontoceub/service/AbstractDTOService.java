package br.com.pontoceub.service;

import br.com.pontoceub.domain.entities.BaseEntity;
import br.com.pontoceub.exceptions.IdNotFoundException;
import br.com.pontoceub.utils.EntityDTOMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractDTOService<DTO, T extends BaseEntity, ID, REPO extends JpaRepository<T, ID>> implements BaseService<DTO, ID> {

    private Class<T> entityClazz;
    private Class<DTO> dtoClazz;
    protected EntityDTOMapper entityDTOService = EntityDTOMapper.getInstance();

    protected REPO repo;

    public AbstractDTOService(REPO repo) {
        this.repo = repo;
    }

    private Class<T> getEntityClazz() {
        if (entityClazz == null) {
            entityClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return entityClazz;
    }

    protected Class<DTO> getDTOClazz() {
        if (dtoClazz == null) {
            dtoClazz = (Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return dtoClazz;
    }

    public DTO entityToDTO(T source) {
        return entityDTOService.entityToDTO(source, getDTOClazz());
    }

    public T dtoToEntity(Object source) {
        return entityDTOService.dtoToEntity(source, getEntityClazz());
    }

    @Override
    public Page<DTO> findAll(Pageable pageable, DTO search) {
        return repo.findAll(getExamples(entityDTOService.
                dtoToEntity(search, getEntityClazz())), pageable).map(e -> entityDTOService.entityToDTO(e, getDTOClazz()));
    }

    protected Example<T> getExamples(T search) {
        return Example.of(search);
    }

    @Override
    public void delete(ID id) {
        repo.deleteById(id);
    }

    @Override
    public DTO save(DTO dto) {
        return entityDTOService.entityToDTO(repo.save(entityDTOService.dtoToEntity(dto, getEntityClazz())), getDTOClazz());
    }

    @Override
    public DTO findById(ID id) {
        return entityDTOService.entityToDTO(findEntityById(id), getDTOClazz());
    }

    protected T findEntityById(ID id) {
        return repo.findById(id).orElseThrow(() -> new IdNotFoundException(getEntityClazz().getSimpleName(), id));
    }

    public void existsById(ID id) {
        if (!repo.existsById(id)) {
            throw new IdNotFoundException(getEntityClazz().getSimpleName(), id);
        }
    }

    @Override
    @Transactional
    public DTO update(DTO dto, ID id) {
        T entity = findEntityById(id);
        validateOnUpdate(dto, entity);
        entityDTOService.setEntityAttributes(dto, entity);
        entity.setId(id);
        return entityDTOService.entityToDTO(repo.save(entity), getDTOClazz());
    }

    protected void validateOnUpdate(DTO dto, T entity) {
        // override
    }
}
