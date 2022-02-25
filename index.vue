<template>
  <Page>
    <List :source="tableList" />
    <Layer :source="tableEdit">
      <Edit :source="tableEdit" />
    </Layer>
    <Layer :source="columnList">
      <List :source="columnList" />
    </Layer>
    <Layer :source="columnEdit">
      <Edit :source="columnEdit" />
    </Layer>
    <Sort :source="columnSort" />
  </Page>
</template>

<script>
import { listTable, listColumn, saveTable, saveColumn, sortColumn, previewTable, delTable, genCode, synchDb } from "@/api/tool/gen";

const genderOptions = [
  { label: '男', value: 'man' },
  { label: '女', value: 'woman' }
]

export default {
  name: "Gen",
  data() {
    return {
      tableList: {
        page: true,
        number: true,
        query: listTable,
        filter: {
          items: [
            {
              field: 'entityName',
              label: '实体名称'
            },
            {
              field: 'entityType',
              label: '实体类型',
              type: 'select',
              options: this.dict.EntityType
            },
            {
              field: 'tablePrefix',
              label: '表前缀'
            },
            {
              field: 'tableComment',
              label: '表描述'
            }
          ]
        },
        action: [
          {
            label: '新建',
            icon: 'el-icon-edit',
            click: () => {
              this.tableEdit.open('新建表', {})
            }
          }
        ],
        operation: [
          {
            label: '列管理',
            icon: 'el-icon-edit',
            click: data => {
              this.columnList.param.entityId = data.id
              this.columnList.open('列管理')
            }
          },
          {
            label: '编辑',
            icon: 'el-icon-edit',
            click: data => {
              this.tableEdit.open('编辑表', data)
            }
          },
          {
            label: '删除',
            icon: 'el-icon-edit',
            click: data => {
              deletetable(data.id).then(({ code }) => {
                if (this.ok(code)) {
                  this.$message.success('删除成功')
                  this.tableList.reload(true)
                }
              })
            }
          }
        ],
        items: [
          {
            field: 'id',
            label: '编号'
          },
          {
            field: 'entityName',
            label: '实体名称'
          },
          {
            field: 'entityType',
            label: '实体类型',
            type: 'select',
            options: this.dict.EntityType
          },
          {
            field: 'tablePrefix',
            label: '表前缀'
          },
          {
            field: 'tableComment',
            label: '表描述'
          },
          {
            field: 'moduleName',
            label: '模块名'
          },
          {
            field: 'businessName',
            label: '业务名'
          },
          {
            field: 'functionAuthor',
            label: '作者'
          },
          {
            field: 'genType',
            label: '生成方式',
            type: 'select',
            options: this.dict.GenType
          },
          {
            field: 'remark',
            label: '备注'
          }
        ]
      },
      tableEdit: {
        action: [
          {
            label: '提交',
            click: data => {
              saveTable(data).then(({ code }) => {
                if (this.ok(code)) {
                  this.$message.success('提交成功')
                  this.tableEdit.close()
                  this.tableList.reload(true)
                }
              })
            }
          }
        ],
        items: [
          {
            field: 'entityName',
            label: '实体名称'
          },
          {
            field: 'tablePrefix',
            label: '表前缀'
          },
          {
            field: 'columnName',
            label: '表名称'
          },
          {
            field: 'entityType',
            label: '实体类型',
            type: 'radio',
            options: this.dict.EntityType
          },
          {
            field: 'tableComment',
            label: '表描述'
          },
          [
            {
              field: 'moduleName',
              label: '模块名'
            },
            {
              field: 'businessName',
              label: '业务名'
            },
          ],
          {
            field: 'functionAuthor',
            label: '作者'
          },
          {
            field: 'genType',
            label: '生成方式',
            type: 'radio',
            options: this.dict.GenType
          },
          {
            field: 'genPath',
            label: '生成路径'
          },
          {
            field: 'remark',
            label: '备注'
          }
        ]
      },
      columnList: {
        page: false,
        number: true,
        query: listColumn,
        param: {},
        action: [
          {
            label: '新建',
            icon: 'el-icon-edit',
            click: () => {
              this.columnEdit.open('新建列', {
                entityId: this.columnList.param.entityId,
                fieldType: 'String',
                isUnique: false,
                isNullable: true,
                isInsertable: true,
                isUpdatable: true,
                isPk: false,
                isQuery: true,
                isList: true,
                isEdit: true,
                isDetail: true
              })
            }
          },
          {
            label: '排序',
            icon: 'el-icon-edit',
            click: data => {
              this.columnSort.open('排序列', data)
            }
          }
        ],
        operation: [
          {
            label: '编辑',
            icon: 'el-icon-edit',
            click: data => {
              this.columnEdit.open('编辑列', data)
            }
          },
          {
            label: '删除',
            icon: 'el-icon-edit',
            click: data => {
              deletetable(data.id).then(({ code }) => {
                if (this.ok(code)) {
                  this.$message.success('删除成功')
                  this.tableList.reload(true)
                }
              })
            }
          }
        ],
        items: [
          {
            field: 'id',
            label: '编号'
          },
          {
            field: 'fieldName',
            label: '字段名称'
          },
          {
            field: 'fieldType',
            label: '字段类型',
            type: 'select',
            options: this.dict.FieldType
          },
          {
            field: 'columnComment',
            label: '列描述'
          },
          {
            field: 'isPk',
            label: '主键',
            type: 'switch'
          },
          {
            field: 'isQuery',
            label: '查询',
            type: 'switch'
          },
          {
            field: 'isList',
            label: '列表',
            type: 'switch'
          },
          {
            field: 'isEdit',
            label: '编辑',
            type: 'switch'
          },
          {
            field: 'isList',
            label: '列表',
            type: 'switch'
          },
          {
            field: 'isDetail',
            label: '详情',
            type: 'switch'
          }
        ]
      },
      columnEdit: {
        action: [
          {
            label: '提交',
            click: data => {
              saveColumn(data).then(({ code }) => {
                if (this.ok(code)) {
                  this.$message.success('提交成功')
                  this.columnEdit.close()
                  this.columnList.reload(true)
                }
              })
            }
          }
        ],
        items: [
          [
            {
              field: 'fieldName',
              label: '字段名称'
            },
            {
              field: 'columnName',
              label: '列名称'
            },
            {
              field: 'fieldType',
              label: '字段类型',
              type: 'select',
              options: this.dict.FieldType
            },
            {
              field: 'defaultValue',
              label: '默认值'
            }
          ],
          {
            field: 'columnComment',
            label: '列描述'
          },
          [
            {
              field: 'columnLength',
              label: '长度',
              type: 'number'
            },
            {
              field: 'columnPrecision',
              label: '精度',
              type: 'number'
            },
            {
              field: 'columnScale',
              label: '刻度',
              type: 'number'
            }
          ],
          [
            {
              field: 'isPk',
              label: '主键',
              type: 'switch'
            },
            {
              field: 'isUnique',
              label: '唯一',
              type: 'switch'
            },
            {
              field: 'isNullable',
              label: '可空',
              type: 'switch'
            },
            {
              field: 'isInsertable',
              label: '可插入',
              type: 'switch'
            },
            {
              field: 'isUpdatable',
              label: '可更新',
              type: 'switch'
            }
          ],
          [
            {
              field: 'isQuery',
              label: '查询',
              type: 'switch'
            },
            {
              field: 'isList',
              label: '列表',
              type: 'switch'
            },
            {
              field: 'isEdit',
              label: '编辑',
              type: 'switch'
            },
            {
              field: 'isList',
              label: '列表',
              type: 'switch'
            },
            {
              field: 'isDetail',
              label: '详情',
              type: 'switch'
            }
          ],
          [
            {
              field: 'joinTable',
              label: '连接表'
            },
            {
              field: 'joinColumn',
              label: '连接外键'
            },
            {
              field: 'joinType',
              label: '连接类型',
              type: 'select',
              options: this.dict.JoinType
            }
          ]
        ]
      },
      columnSort: {
        field: 'fieldName',
        submit: sortColumn
      }
    }
  },
  created() {
    
  },
  methods: {
    
  }
};
</script>
