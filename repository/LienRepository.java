package com.equabli.datascrubbing.repository;

import com.equabli.datascrubbing.entity.Lien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LienRepository extends JpaRepository<Lien, Long> {
    @Query(value = "select lien from Lien lien "
            + "where lien.recordStatusId = :recordStatusId ")
    public Page<Lien> getLienToProcess(Integer recordStatusId, Pageable pageable);

}
