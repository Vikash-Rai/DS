package com.equabli.datascrubbing.dao;

import com.equabli.datascrubbing.entity.Account;
import com.equabli.domain.Response;

import java.util.Map;

public interface AccountDao {
    Response<Map<String, Object>> updateAccountDetails(Account account);
}
