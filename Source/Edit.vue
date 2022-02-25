<template>
  <el-form :rules="source.rules" :model="editData" :label-position="source.label.position" :label-width="source.label.width" :label-suffix="source.label.suffix" size="mini">
    <el-row v-for="(row, rowIndex) in source.items" :key="rowIndex" :gutter="source.gutter">
      <el-col v-for="(column, columnIndex) in row" :key="columnIndex" :span="column.span || 24 / row.length">
        <template v-if="handleExist(column.exist)">
          <slot v-if="column.item && column.slot" :name="column.slot" :data="editData" :item="column" />
          <el-form-item v-else :label="$t(column.label)" :prop="column.field">
            <el-button-group v-if="column.toolkit">
              <el-button
                v-for="(tool, key) in column.toolkit"
                :key="key"
                :type="tool.type || 'primary'"
                size="mini"
                @click="() => {
                  if (tool.click) {
                    tool.click(editData, column)
                  }
                }"
              >{{ $t(tool.label) }}</el-button>
            </el-button-group>
            <slot v-if="column.slot" :name="column.slot" :data="editData" :item="column" />
            <div v-else-if="column.html" v-html="column.html" />
            <template v-else-if="column.options">
              <el-checkbox-group
                v-if="column.type === 'checkbox'"
                v-model="editData[column.field]"
                @change="() => {
                  if (column.edit && column.edit.change) {
                    column.edit.change(editData, column)
                  } else if (column.change) {
                    column.change(editData, column)
                  }
                }"
              >
                <el-checkbox v-for="(value, key) in column.options" :key="key" :label="key">{{ $t(value) }}</el-checkbox>
              </el-checkbox-group>
              <el-select v-else-if="column.type === 'select'" v-model="editData[column.field]" :placeholder="column.placeholder || $t('请选择' + column.label)" filterable style="width: 100%">
                <template v-if="Array.isArray(column.options)">
                  <el-option v-for="(option, key) in column.options" :key="key" :label="option.label" :value="option.value" />
                </template>
                <template v-else>
                  <el-option v-for="(value, key) in column.options" :key="key" :label="value" :value="key" />
                </template>
              </el-select>
              <el-cascader
                v-else-if="column.type === 'cascader'"
                v-model="editData[column.field]"
                :placeholder="column.placeholder || $t('请选择' + column.label)"
                :options="column.options"
                filterable
                @change="() => {
                  if (column.edit && column.edit.change) {
                    column.edit.change(editData)
                  } else if (column.change) {
                    column.change(editData)
                  }
                }"
              />
              <el-radio-group
                v-else
                v-model="editData[column.field]"
                @change="() => {
                  if (column.edit && column.edit.change) {
                    column.edit.change(editData, column)
                  } else if (column.change) {
                    column.change(editData, column)
                  }
                }"
              >
                <template v-if="Array.isArray(column.options)">
                  <el-radio v-for="(option, key) in column.options" :key="key" :label="option.value">{{ $t(option.label) }}</el-radio>
                </template>
                <template v-else>
                  <el-radio v-for="(value, key) in column.options" :key="key" :label="key">{{ $t(value) }}</el-radio>
                </template>
              </el-radio-group>
            </template>
            <el-switch
              v-else-if="column.type === 'switch'"
              v-model="editData[column.field]"
              @change="() => {
                if (column.edit && column.edit.change) {
                  column.edit.change(editData)
                } else if (column.change) {
                  column.change(editData)
                }
              }"
            />
            <el-transfer
              v-else-if="column.type === 'transfer'"
              v-model="editData[column.field]"
              :data="column.data"
              filterable
              :filter-placeholder="column.placeholder"
              :titles="column.titles"
              :button-texts="column.texts"
            />
            <template v-else-if="column.type === 'time'">
              <el-time-picker
                v-if="column.range"
                v-model="editData[column.field]"
                is-range
                :range-separator="$t('至')"
                :start-placeholder="$t('开始时间')"
                :end-placeholder="$t('结束时间')"
                :placeholder="$t('请选择时间范围')"
                value-format="HH:mm:ss"
                @change="data => {
                  editData[column.field + 'Start'] = data ? data[0] : null
                  editData[column.field + 'End'] = data ? data[1] : null
                }"
              />
              <el-time-picker v-else v-model="editData[column.field]" :placeholder="column.placeholder || $t('请选择' + column.label)" />
            </template>
            <el-date-picker v-else-if="column.type === 'date'" v-model="editData[column.field]" type="date" :value-format="column.format || 'yyyy-MM-dd'" :placeholder="column.placeholder || $t('请选择' + column.label)" />
            <el-date-picker v-else-if="column.type === 'datetime'" v-model="editData[column.field]" type="datetime" :value-format="column.format || 'yyyy-MM-dd hh:mm:ss'" :placeholder="column.placeholder || $t('请选择' + column.label)" />
            <Icon
              v-else-if="column.type === 'icon'"
              :source="{
                field: column.field,
                data: editData
              }"
            />
            <Area
              v-else-if="column.type === 'area'"
              v-model="editData[column.field]"
              @change="(value) => {
                editData[column.field] = value
                if (column.change) {
                  column.change(value, editData)
                }
              }"
            />
            <Photo
              v-else-if="column.type === 'photo'"
              v-model="editData[column.field]"
              @change="(value) => {
                editData[column.field] = value
                if (column.change) {
                  column.change(value, editData)
                }
              }"
            />
            <Album
              v-else-if="column.type === 'album'"
              v-model="editData[column.field]"
              @change="(value) => {
                editData[column.field] = value
                if (column.change) {
                  column.change(value, editData)
                }
              }"
            />
            <File
              v-else-if="column.type === 'file'"
              v-model="editData[column.field]"
              @change="(value) => {
                editData[column.field] = value
                if (column.change) {
                  column.change(value, editData)
                }
              }"
            />
            <el-input-number v-else-if="column.type === 'number'" v-model="editData[column.field]" :min="column.min" :max="column.max" :step="column.step" :precision="column.precision" :disabled="handleDisabled(column.disabled, column)" :placeholder="handleDisabled(column.disabled, column) ? '' : column.placeholder || $t('请输入' + column.label)" />
            <el-input v-else v-model="editData[column.field]" :type="column.type" :rows="column.rows" :disabled="handleDisabled(column.disabled, column)" :placeholder="handleDisabled(column.disabled, column) ? '' : column.placeholder || $t('请输入' + column.label)">
              <template v-if="column.append" slot="append">
                <el-button v-if="column.append.click" :type="column.append.type" :icon="column.append.icon" @click="column.append.click(editData)">{{ $t(column.append.label) }}</el-button>
              </template>
            </el-input>
            <div v-if="column.note" class="field-note">{{ $t(column.note) }}</div>
          </el-form-item>
        </template>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>

export default {
  props: {
    source: {
      required: true,
      type: Object
    }
  },
  data() {
    if (!this.source.data) {
      this.source.data = {}
    }
    const items = []
    for (const row of this.source.items) {
      if (Array.isArray(row)) {
        items.push(row)
        for (const column of row) {
          this.source.data[column.field] = this.source.data[column.field] || null
        }
      } else {
        items.push([row])
        this.source.data[row.field] = this.source.data[row.field] || null
      }
    }

    this.source.label = this.source.label || {}
    this.source.label.position = this.source.label.position || 'right'
    this.source.label.width = this.source.label.width || '120px'
    this.source.label.suffix = this.source.label.suffix || ''

    this.source.items = items
    this.source.data = this.clone(this.source.data)
    return {
      baseURL: process.env.VUE_APP_BASE_API,
      editTitle: '',
      editVisible: false,
      editData: this.source.data
    }
  },
  methods: {
    handleExist(exist) {
      if (exist) {
        return exist(this.source.data, this.source)
      }
      return true
    },
    handleDisabled(disabled) {
      if (disabled) {
        if (typeof disabled === 'function') {
          return !!disabled(this.source.data, this.source)
        }
      }
      return !!disabled
    }
  }
}
</script>

<style>
  .image-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 5px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .image-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .image-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
  }
  .image-uploader-show {
    width: 100px;
    height: 100px;
    display: block;
    background-color: rgba(0, 0, 0, 0.1);
  }
  .field-note {
    color: rgba(75, 86, 97, 0.5);
  }
  .el-input.is-disabled .el-input__inner, .el-textarea.is-disabled .el-textarea__inner {
    color: #606266;
  }
</style>
