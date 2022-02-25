package com.desaysv.edi.infrastructure.system.entity;

import com.desaysv.edi.infrastructure.system.Dict;
import com.desaysv.edi.infrastructure.system.tool.utils.Excel;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 代码生成业务字段对象
 */
@Data
@Entity
@Table(name = "edi_gen_field")
public class GenField implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** 归属表编号 */
    @Excel(name = "归属表编号")
    @Column(name = "entity_name")
    private String entityName;

    /** 列名称 */
    @Excel(name = "列名称")
    @Id
    @Column(name = "column_name", length = 200)
    private String columnName = "";

    /** 字段名称 */
    @Excel(name = "字段名称")
    @Column(name = "field_name", length = 200)
    private String fieldName = "";
    
    /** 类型 */
    @Excel(name = "字段类型")
    @Column(name = "field_type", length = 100)
    private FieldType fieldType = FieldType.String;
    
    /** 默认值 */
    @Excel(name = "默认值")
    @Column(name = "default_value", length = 100)
    private FieldType defaultValue;

    /** 描述 */
    @Excel(name = "描述")
    @Column(name = "column_comment", length = 500)
    private String columnComment = "";

    /** 长度 */
    @Excel(name = "长度")
    @Column(name = "column_length")
    private Integer columnLength = 255;

    /** 精度（有效数字位数） */
    @Excel(name = "精度")
    @Column(name = "column_precision")
    private Integer columnPrecision = 0;

    /** 刻度（小数位数） */
    @Excel(name = "刻度")
    @Column(name = "column_scale")
    private Integer columnScale = 0;

    /** 唯一 */
    @Excel(name = "唯一")
    @Column(name = "is_unique")
    private Boolean isUnique = false;

    /** 可空 */
    @Excel(name = "可空")
    @Column(name = "is_nullable")
    private Boolean isNullable = true;

    /** 插入 */
    @Excel(name = "插入")
    @Column(name = "is_insertable")
    private Boolean isInsertable = true;

    /** 更新 */
    @Excel(name = "更新")
    @Column(name = "is_updatable")
    private Boolean isUpdatable = true;
    
    /** 连接表 */
    @Excel(name = "连接表")
    @Column(name = "join_table", length = 100)
    private String joinTable;
    
    /** 连接列 */
    @Excel(name = "连接列")
    @Column(name = "join_column", length = 100)
    private String joinColumn;
    
    /** 连接类型 */
    @Excel(name = "连接类型")
    @Column(name = "join_type", length = 100)
    private JoinType joinType;
    
    /** 是否主键 */
    @Excel(name = "主键")
    @Column(name = "is_pk")
    private Boolean isPk;
    
    @Excel(name = "查询")
    @Column(name = "is_query")
    private Boolean isQuery;
    
    @Excel(name = "列表")
    @Column(name = "is_list")
    private Boolean isList;
    
    @Excel(name = "编辑")
    @Column(name = "is_edit")
    private Boolean isEdit;
    
    @Excel(name = "详情")
    @Column(name = "is_detail")
    private Boolean isDetail;
    
    @Column(name = "sort_num")
    @ColumnDefault("0")
    private Integer sortNum;
    
    public enum FieldType implements Dict {
        String,
        Double,
        Integer,
        Long,
        Date,
        Clob,
        Boolean;
    
        @Override
        public java.lang.String getLabel() {
            return name();
        }
    
        @Override
        public java.lang.String getValue() {
            return name();
        }
    }
    
    public enum JoinType implements Dict {
        OneToOne,
        OneToMany,
        ManyToOne,
        ManyToMany;
    
        @Override
        public String getLabel() {
            return name();
        }
    
        @Override
        public String getValue() {
            return name();
        }
    }
}
