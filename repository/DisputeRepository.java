package com.equabli.datascrubbing.repository;

import com.equabli.datascrubbing.entity.Client;
import com.equabli.datascrubbing.entity.Dispute;
import com.equabli.domain.entity.ConfRecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {

	@Query(value="select dispute from Dispute dispute "
    		+ "where dispute.recordStatusId = :rawRecordStatusId ")
    public Page<Dispute> getDisputeToProcess(Integer rawRecordStatusId, Pageable pageable);

	@Query(value = "select new Client(cl.clientId, cl.shortName) from Client cl join RecordStatus rs on cl.recordStatusId = rs.recordStatusId and rs.shortName = '"+ ConfRecordStatus.ENABLED + "' " +
			" where cl.clientId = :clientId")
	Client findClientById(Integer clientId);

	@Query("SELECT new Dispute(d.disputeId) " +
			"FROM Dispute d JOIN RecordStatus rs ON d.recordStatusId = rs.recordStatusId AND rs.shortName = 'Enabled' " +
			" WHERE d.clientId = :clientId and d.clientAccountNumber = :clientAccountNumber and d.clientDisputeId = :clientDisputeId")
	Dispute getDataDispute(Integer clientId, String clientAccountNumber, Long clientDisputeId);
}