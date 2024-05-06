package br.com.pontoceub.utils;

import br.com.pontoceub.domain.entities.BaseEntity;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class EntityDTOMapper {
    private ModelMapper mm;

    @Getter
    private static final EntityDTOMapper instance = new EntityDTOMapper();
    public EntityDTOMapper() {
        mm = new ModelMapper();
        mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public <T extends BaseEntity, DTO> DTO entityToDTO(T source, Class<DTO> dtoClazz) {
        return mm.map(source, dtoClazz);
    }

    public <T extends BaseEntity, DTO> T dtoToEntity(DTO dto, Class<T> entityClass) {
        return mm.map(dto, entityClass);
    }

    public <T extends BaseEntity, DTO> void setEntityAttributes(DTO dto, T entity) {
        mm.map(dto, entity);
    }
}
