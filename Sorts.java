package com.desaysv.edi.infrastructure.system;

import org.springframework.data.domain.Sort;

public class Sorts {
	
	public static Sort asc(String... properties) {
		return Sort.by(Sort.Direction.ASC, properties);
	}
	
	public static Sort desc(String... properties) {
		return Sort.by(Sort.Direction.DESC, properties);
	}
}
