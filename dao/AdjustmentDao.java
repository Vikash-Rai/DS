package com.equabli.datascrubbing.dao;

import com.equabli.datascrubbing.entity.Adjustment;
import com.equabli.domain.Response;

import java.util.Map;

public interface AdjustmentDao {
    Response<Map<String,Object>> insertBalanceAdjustment(Adjustment adjustment);
}
