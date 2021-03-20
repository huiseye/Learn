package com.supermap.datashare.service;

import java.util.List;
import java.util.Map;

public interface QueryService {
	

	String getLeaseInfo(String lessorName, String lessorCode, String lesseeName, String lesseeCode,String type);

}
