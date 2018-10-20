package com.h8.nh.nhoodlocationsvc.repositories;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationEntryRepository extends CrudRepository<LocationEntry, Long> {
}
