package com.equabli.datascrubbing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.AccountAdditionalInfo;

@Repository
public interface AccountAdditionalInfoRepository extends JpaRepository<AccountAdditionalInfo, Long>{

	@Query(value="select accountAdditionalInfo from AccountAdditionalInfo accountAdditionalInfo "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AccountAdditionalInfo> getAccountAdditionalInfoToProcess(Integer rawRecordStatusId, Pageable pageable);

}