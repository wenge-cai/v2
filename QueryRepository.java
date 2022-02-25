package com.desaysv.edi.infrastructure.system;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class QueryRepository {

    @PersistenceContext
    EntityManager manager;
    
    public <R> TypedQuery<R> find(Class<R> c, Holder<R> holder) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<R> query = builder.createQuery(c);
        holder.apply(builder, query);
        
        return manager.createQuery(query);
    }
    
    public <R> List<R> findAll(Class<R> type, Holder<R> holder) {
        return find(type, holder).getResultList();
    }
    
    public <R> R findOne(Class<R> type, Holder<R> holder) {
        return find(type, holder).getSingleResult();
    }
    
    public interface Holder<R> {
        public void apply(CriteriaBuilder builder, CriteriaQuery<R> query);
    }
}
