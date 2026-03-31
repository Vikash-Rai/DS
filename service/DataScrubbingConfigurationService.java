package com.equabli.datascrubbing.service;

import java.util.List;
import java.util.Map;

import com.equabli.datascrubbing.entity.ConfEntity;
import com.equabli.domain.Response;

public interface DataScrubbingConfigurationService {
	
	Response<Map<String, Object>> getErrorWarnMessageDetailsForClient(Map<String, String> dataScrubbingSearch);
	Response<String> updateErrorWarnMessageDetailsForClient(Map<String, Object> dataScrubbingSearch);
	Response<List<ConfEntity>> getAllConfEntities();
}
