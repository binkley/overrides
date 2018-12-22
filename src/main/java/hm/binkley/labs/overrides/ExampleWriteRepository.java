package hm.binkley.labs.overrides;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "directs", path = "directs")
interface ExampleWriteRepository
        extends CrudRepository<ExampleWrite, Long> {
    @Query("SELECT id, name FROM OVERRIDES.p_Examples WHERE name = :name")
    Optional<ExampleWrite> findByName(@Param("name") String name);
}
