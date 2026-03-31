package com.equabli.datascrubbing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.ScrubWarning;

@Repository
public interface ScrubWarningRepository extends JpaRepository<ScrubWarning, Long> {
	
}