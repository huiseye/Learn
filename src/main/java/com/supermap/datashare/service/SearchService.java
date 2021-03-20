package com.supermap.datashare.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SearchService {
	
Map<String, String> getTCXX(String data) throws IOException;
List<HashMap<String, String>> getFWZT(String relationid,String bdcdylx)throws IOException;
String swxxByHTH(String hth);

}
