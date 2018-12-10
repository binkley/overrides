package hm.binkley.labs.overrides;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ExampleRepository
        extends PagingAndSortingRepository<Example, Long> {}
