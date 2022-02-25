package com.desaysv.edi.infrastructure.system.entity;

import com.desaysv.edi.infrastructure.system.Dict;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 代码生成业务对象
 */
@Data
@Entity
@Table(name = "edi_gen_entity")
public class GenEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 实体类名称 */
    @Id
    @Column(name = "entity_name", length = 50)
    private String entityName;
    
    /** 表名称 */
    @Column(name = "table_name", length = 50)
    private String tableName;
    
    /** 表前缀 */
    @Column(name = "table_prefix", length = 20)
    private String tablePrefix;
    
    /** 表描述 */
    @Column(name = "table_comment", length = 200)
    private String tableComment;
    
    /** 使用的模板（crud单表操作 tree树表操作） */
    @Column(name = "entity_type", length = 10)
    private EntityType entityType;
    
    /** 生成包路径 */
    @Column(name = "package_name", length = 100)
    private String packageName;
    
    /** 生成模块名 */
    @Column(name = "module_name", length = 30)
    private String moduleName;
    
    /** 生成功能名 */
    @Column(name = "function_name", length = 50)
    private String functionName;
    
    /** 生成功能作者 */
    @Column(name = "function_author", length = 50)
    private String functionAuthor;
    
    /** 生成代码方式（0zip压缩包 1自定义路径） */
    @Column(name = "gen_type", length = 1)
    private GenType genType;
    
    /** 生成路径（不填默认项目路径） */
    @Column(name = "gen_path", length = 200)
    private String genPath;
    
    /** 备注 */
    @Column(name = "remark", length = 500)
    private String remark;

    @OneToMany
    @JoinColumn(name = "entity_name")
    private List<GenField> fields;
    
    public enum EntityType implements Dict {
        
        normal("一般"),
        tree("树结构"),
        join("中间表");
        
        private String label;
        
        EntityType(String label) {
            this.label = label;
        }
    
        @Override
        public String getLabel() {
            return label;
        }
    
        @Override
        public String getValue() {
            return name();
        }
    }
    
    public enum GenType implements Dict {
        
        zip("Zip压缩包"),
        
        custom("自定义");
        
        private String label;
        
        GenType(String label) {
            this.label = label;
        }
    
        @Override
        public String getLabel() {
            return label;
        }
    
        @Override
        public String getValue() {
            return name();
        }
    }
}
