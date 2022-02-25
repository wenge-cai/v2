<template>
  <el-upload
    :action="FILE_SERVICE_BASE_URL + 'upload'"
    list-type="picture-card"
    :file-list="fileList"
    :on-remove="handleRemove"
    :on-success="handleSuccess"
  >
    <i class="el-icon-plus" />
  </el-upload>
</template>

<script>

export default {
  name: 'Album',
  props: {
    value: {
      type: String,
      default: null
    }
  },
  data() {
    const fileList = []
    let urlList = []
    if (this.value) {
      urlList = this.value.split(',')
      for (let index = 0; index < urlList.length; index++) {
        fileList.push({
          url: this.FILE_SERVICE_STATIC_URL + urlList[index]
        })
      }
    }
    return {
      urlList,
      fileList
    }
  },
  methods: {
    handleChange() {
      this.$emit('change', this.urlList.join(','))
    },
    handleRemove(data, fileList) {
      this.urlList = []
      for (let index = 0; index < fileList.length; index++) {
        this.urlList.push(fileList[index].url.replace(this.FILE_SERVICE_STATIC_URL, ''))
      }
      this.handleChange()
    },
    handleSuccess({ code, data, message }) {
      if (this.ok(code)) {
        this.urlList.push(data.md5)
        this.handleChange()
      } else {
        this.$message.error(message)
      }
    }
  }
}
</script>

<style>
  .el-upload--picture-card, .el-upload-list--picture-card .el-upload-list__item {
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 28px;
  }
  .el-upload-list--picture-card .el-upload-list__item-status-label {
    width: 48px;
    height: 24px;
    line-height: 24px;
  }
</style>
