package hm.binkley.labs.overrides;

import org.springframework.data.repository.CrudRepository;

interface ExampleOverrideWriteRepository
        extends CrudRepository<ExampleOverrideWrite, Long> {
}
