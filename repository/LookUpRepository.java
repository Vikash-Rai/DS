package com.equabli.datascrubbing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.datascrubbing.entity.LookUp;
import com.equabli.domain.entity.ConfRecordStatus;

@Repository
public interface LookUpRepository extends JpaRepository<LookUp, Integer> {

	@Query("select new LookUp(lu.lookupId, lu.keycode, lu.keyvalue) "
			+ "from LookUp lu "
			+ "join LookUpGroup lug on lu.lookupGroupId = lug.lookupGroupId "
			+ "join RecordStatus rs on lu.recordStatusId = rs.recordStatusId and rs.shortName = '"+ConfRecordStatus.ENABLED+"' "
			+ "WHERE lug.keyvalue = :keyvalue")
	public List<LookUp> lookUpByGroupKeyValue(String keyvalue);
}