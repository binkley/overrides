package hm.binkley.labs.overrides;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface ExampleRepository
        extends CrudRepository<Example, Long> {
}
