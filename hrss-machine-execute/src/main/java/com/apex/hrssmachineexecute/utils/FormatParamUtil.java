package com.apex.hrssmachineexecute.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class FormatParamUtil {

	public static String formatUrlMap(Map<String, Object> paraMap) throws Exception {
		String buff = "";
		Map<String, Object> tmpMap = paraMap;
		List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(tmpMap.entrySet());
		// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
			@Override
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		// 构造URL 键值对的格式
		StringBuilder buf = new StringBuilder();
		for (Map.Entry<String, Object> item : infoIds) {

			if (!StringUtils.isEmpty(item.getKey())) {
				String key = item.getKey();
				String val = (String)item.getValue();
				buf.append(key + "=" + val);
				buf.append("&");
			}

		}
		buff = buf.toString();
		if (buff.isEmpty() == false) {
			buff = buff.substring(0, buff.length() - 1);
		}
		return buff;
	}
}
