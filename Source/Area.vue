<template>
  <el-cascader
    v-model="model"
    filterable
    clearable
    class="cascader"
    :disabled="disabled"
    :placeholder="placeholder"
    :options="options"
    :props="{
      expandTrigger: 'hover',
      value: 'value',
      label: 'label'
    }"
    @change="handleChange"
  />
</template>

<script>
import areaList from './store/area-list'

export default {
  props: {
    value: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      disabled: false,
      placeholder: this.$t('请选择区域'),
      options: areaList,
      model: this.value ? this.value.split(',') : null
    }
  },
  methods: {
    handleChange(value) {
      this.model = value
      this.$emit('change', this.model.join(','), value)
    }
  }
}
</script>
