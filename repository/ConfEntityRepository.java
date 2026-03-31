package com.equabli.datascrubbing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.ConfEntity;

@Repository
public interface ConfEntityRepository extends JpaRepository<ConfEntity, Long>  {

	@Query(value = " select new ConfEntity(entityId, shortName, fullName) from ConfEntity ")
	public List<ConfEntity> getAllConfEntities();
}
