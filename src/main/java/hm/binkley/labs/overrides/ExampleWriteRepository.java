package hm.binkley.labs.overrides;

import org.springframework.data.repository.CrudRepository;

interface ExampleWriteRepository
        extends CrudRepository<ExampleWrite, Long> {
}
