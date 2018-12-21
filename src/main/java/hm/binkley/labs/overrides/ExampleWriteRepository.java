package hm.binkley.labs.overrides;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "directs", path = "directs")
interface ExampleWriteRepository
        extends CrudRepository<ExampleWrite, Long> {
}
