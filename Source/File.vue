<template>
  <div>
    <el-button v-if="loading" :loading="loading" type="primary" circle />
    <el-upload
      v-else
      :action="FILE_SERVICE_BASE_URL + 'upload'"
      :show-file-list="false"
      :before-upload="handleLoading"
      :on-success="handleSuccess"
    >
      <el-button slot="trigger" :loading="loading" type="primary" icon="el-icon-upload2" circle />
      <template v-if="value">
        <el-link :href="FILE_SERVICE_STATIC_URL + value" style="margin: 0 10px">
          <el-button type="primary" icon="el-icon-download" circle />
        </el-link>
        <el-button type="danger" icon="el-icon-close" circle @click="handleChange('')" />
      </template>
    </el-upload>
  </div>
</template>

<script>

export default {
  name: 'File',
  props: {
    value: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      loading: false
    }
  },
  methods: {
    handleLoading() {
      this.loading = true
    },
    handleChange(value) {
      this.$emit('change', value)
    },
    handleSuccess({ code, data, message }) {
      if (this.ok(code)) {
        this.handleChange(data.md5)
      } else {
        this.$message.error(message)
      }
      this.loading = false
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
