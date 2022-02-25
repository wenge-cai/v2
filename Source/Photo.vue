<template>
  <el-upload
    class="photo"
    :action="FILE_SERVICE_BASE_URL + 'upload'"
    :show-file-list="false"
    :on-success="handleSuccess"
  >
    <el-image v-if="value" :src="FILE_SERVICE_STATIC_URL + value" fit="contain" class="photo-show" @click.stop="handleChange('')" />
    <i v-else class="el-icon-plus photo-plus" />
  </el-upload>
</template>

<script>

export default {
  name: 'Photo',
  props: {
    value: {
      type: String,
      default: null
    }
  },
  methods: {
    handleChange(value) {
      this.$emit('change', value)
    },
    handleSuccess({ code, data, message }) {
      if (this.ok(code)) {
        this.handleChange(data.md5)
      } else {
        this.$message.error(message)
      }
    }
  }
}
</script>

<style>
  .photo .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 5px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .photo .el-upload:hover {
    border-color: #409EFF;
  }
  .photo-plus {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.1);
  }
  .photo-delete {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    background-color: rgba(0, 0, 0, 0.1);
  }
  .photo-show {
    width: 100px;
    height: 100px;
    display: block;
    background-color: rgba(0, 0, 0, 0.1);
  }
</style>
