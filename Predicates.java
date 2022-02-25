package com.desaysv.edi.infrastructure.system;

import cn.hutool.core.util.ReflectUtil;
import com.desaysv.edi.infrastructure.system.tool.StringTool;
import lombok.SneakyThrows;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Predicates extends ArrayList<Predicate> {
	
	private Root root;
	
	private CriteriaQuery query;
	
	private CriteriaBuilder builder;
	
	private Map<Class, Root> roots;
	
	private Predicates(Root root, CriteriaQuery query, CriteriaBuilder builder) {
		this.root = root;
		this.query = query;
		this.builder = builder;
	}
	
	public static Predicates of(Root root, CriteriaQuery query, CriteriaBuilder builder) {
		return new Predicates(root, query, builder);
	}
	
	private Root root(Class rootClass) {
		 if (roots != null) {
		 	  return roots.get(rootClass);
		 }
		 return root;
	}
	
	private <A, B> Path path(Getter<A, B> getter) {
		 return path(root, getter);
	}
	
	private <A, B> Path path(Class rootClass, Getter<A, B> getter) {
		 return path(root(rootClass), getter);
	}
	
	private <A, B> Path path(Root root, Getter<A, B> getter) {
		 return root.get(getter.property());
	}
	
	public Predicate and() {
		return builder.and(super.toArray(new Predicate[super.size()]));
	}
	
	public Predicates add(Predicate... predicates) {
		if (predicates != null && predicates.length > 0) {
			for (Predicate predicate : predicates) {
				if (predicate != null) {
					super.add(predicate);
				}
			}
		}
		return this;
	}
	
	public Predicates add(boolean flag, Predicate... predicates) {
		if (flag) {
			add(predicates);
		}
		return this;
	}
	
	public Predicates distinct(boolean distinct) {
		query.distinct(distinct);
		return this;
	}
	
	public Predicates from(Class... classes) {
		if (classes != null && classes.length > 0) {
			roots = new HashMap<>();
			for (Class rootClass : classes) {
				roots.put(rootClass, query.from(rootClass));
			}
		}
		return this;
	}
	
	public <A, B> Predicates asc(Getter<A, B>... getters) {
		if (getters != null && getters.length > 0) {
			query.orderBy(Arrays.stream(getters).map(getter ->  builder.asc(path(getter))).toArray(Order[]::new));
		}
		return this;
	}
	
	public <A, B> Predicates desc(Getter<A, B>... getters) {
		if (getters != null && getters.length > 0) {
			query.orderBy(Arrays.stream(getters).map(getter ->  builder.desc(path(getter))).toArray(Order[]::new));
		}
		return this;
	}
	
	public <A, B> Predicates isNull(Getter<A, B> getter) {
		super.add(builder.isNull(path(getter)));
		return this;
	}
	
	public <A, B> Predicates equal(Class rootClass, Getter<A, B> getter, Object value) {
		super.add(builder.equal(path(rootClass, getter), value));
		return this;
	}
	
	public <A, B, C, D> Predicates equal(Getter<A, B> getter, Class rootClass, Getter<C, D> otherGetter) {
		super.add(builder.equal(path(getter), path(rootClass, otherGetter)));
		return this;
	}
	
	public <A, B, C, D> Predicates equal(Class class1, Getter<A, B> getter1, Class class2, Getter<C, D> getter2) {
		super.add(builder.equal(path(class1, getter1), path(class2, getter2)));
		return this;
	}
	
	public <A, B> Predicates equal(Getter<A, B> getter, Object value) {
		super.add(builder.equal(path(getter), value));
		return this;
	}
	
	public <A, B> Predicates equal(boolean isNotEmpty, Getter<A, B> getter, Object value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				this.equal(getter, value);
			}
		} else {
			this.equal(getter, value);
		}
		return this;
	}
	
	public <A, B> Predicates like(boolean isNotEmpty, Getter<A, B> getter, String value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				super.add(builder.like(path(getter), "%" + value + "%"));
			}
		} else {
			super.add(builder.like(path(getter), "%" + value + "%"));
		}
		return this;
	}
	
	public <A, B, C extends Comparable<? super C>> Predicates gt(boolean isNotEmpty, Getter<A, B> getter, C value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				super.add(builder.greaterThan(path(getter), value));
			}
		} else {
			super.add(builder.greaterThan(path(getter), value));
		}
		return this;
	}
	
	public <A, B, C extends Comparable<? super C>> Predicates ge(boolean isNotEmpty, Getter<A, B> getter, C value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				super.add(builder.greaterThanOrEqualTo(path(getter), value));
			}
		} else {
			super.add(builder.greaterThanOrEqualTo(path(getter), value));
		}
		return this;
	}
	
	public <A, B, C extends Comparable<? super C>> Predicates lt(boolean isNotEmpty, Getter<A, B> getter, C value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				super.add(builder.lessThan(path(getter), value));
			}
		} else {
			super.add(builder.lessThan(path(getter), value));
		}
		return this;
	}
	
	public <A, B, C extends Comparable<? super C>> Predicates le(boolean isNotEmpty, Getter<A, B> getter, C value) {
		if (isNotEmpty) {
			if (!StringTool.isEmpty(value)) {
				super.add(builder.lessThanOrEqualTo(path(getter), value));
			}
		} else {
			super.add(builder.lessThanOrEqualTo(path(getter), value));
		}
		return this;
	}
	
	public <A, B> Predicates in(Getter<A, B> getter, Object... values) {
		if (values != null && values.length > 0) {
			CriteriaBuilder.In in = builder.in(path(getter));
			for (Object value : values) {
				in.value(value);
			}
			super.add(in);
		}
		return this;
	}
	
	public interface Getter<A, B> extends Function<A, B>, Serializable {
		
		@SneakyThrows
		default String property() {
			Method writeReplace = ReflectUtil.getMethodByName(this.getClass(), "writeReplace");
			writeReplace.setAccessible(true);
			SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(this);
			String methodName = serializedLambda.getImplMethodName();
			if (methodName.startsWith("get")) {
				methodName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
			} else if (methodName.startsWith("is")) {
				methodName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
			}
			return methodName;
		}
	}
}
