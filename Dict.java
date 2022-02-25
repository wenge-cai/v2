package com.desaysv.edi.infrastructure.system;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.util.*;

public interface Dict {
	
	String getLabel();
	
	String getValue();
	
	class Builder {
		
		private static final Map<String, List<Dict>> DATA = new HashMap<>();
		
		public static Map<String, List<Dict>> data() {
			if (DATA.isEmpty()) {
				try {
					ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
					provider.addIncludeFilter(new AssignableTypeFilter(Dict.class));
					Set<BeanDefinition> components = provider.findCandidateComponents(Dict.class.getPackage().getName());
					for (BeanDefinition component : components) {
						Class c = Class.forName(component.getBeanClassName());
						Method method = c.getMethod("values");
						Dict[] dicts = (Dict[]) method.invoke(null);
						put(dicts);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return DATA;
		}
		
		private static void put(Dict[] dicts) {
			List<Dict> dictList = new ArrayList<>(dicts.length);
			for (Dict dict : dicts) {
				dictList.add(new Dict() {
					@Override
					public String getLabel() {
						return dict.getLabel();
					}
					
					@Override
					public String getValue() {
						return dict.getValue();
					}
				});
			}
			DATA.put(dicts[0].getClass().getSimpleName(), dictList);
		}
	}
}
