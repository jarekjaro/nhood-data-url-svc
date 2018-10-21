package com.h8.nh.nhooddataurlsvc.repositories;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataUrlRepository extends CrudRepository<DataUrl, Long> {
}
