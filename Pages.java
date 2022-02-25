package com.desaysv.edi.infrastructure.system;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Data
public class Pages {
	
	private int pageNum = 1;
	
	private int pageSize = 10;
	
	public static PageRequest of(int page, int size) {
		return PageRequest.of(page -1, size);
	}
	
	public static PageRequest of(int page, int size, Sort sort) {
		return PageRequest.of(page -1, size, sort);
	}
	
	public static <A, B> PageRequest desc(int page, int size, Predicates.Getter<A, B>... getters) {
		String[] properties = Arrays.stream(getters).map(getter -> getter.property()).toArray(String[]::new);
		return Pages.of(page, size, Sorts.desc(properties));
	}
	
	public static <A, B> PageRequest asc(int page, int size, Predicates.Getter<A, B>... getters) {
		String[] properties = Arrays.stream(getters).map(getter -> getter.property()).toArray(String[]::new);
		return Pages.of(page, size, Sorts.asc(properties));
	}
	
	public static <A, B> PageRequest asc(Pages pages, Predicates.Getter<A, B>... getters) {
		return Pages.asc(pages.getPageNum(), pages.getPageSize(), getters);
	}
	
	public static <A, B> PageRequest desc(Pages pages, Predicates.Getter<A, B>... getters) {
		return Pages.desc(pages.getPageNum(), pages.getPageSize(), getters);
	}
	
	public PageRequest build() {
		return Pages.of(pageNum, pageSize);
	}
	
	public <A, B> PageRequest asc(Predicates.Getter<A, B>... getters) {
		return Pages.asc(pageNum, pageSize, getters);
	}
	
	public <A, B> PageRequest desc(Predicates.Getter<A, B>... getters) {
		return Pages.desc(pageNum, pageSize, getters);
	}
}
