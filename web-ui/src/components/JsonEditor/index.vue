<template>
  <div class="jsonEditor">
    <vue-json-editor
      v-model="val"
      style="width:90%"
      :style="{'height': height + 'px !important'}"
      :mode="'code'"
      :show-btns="true"
      lang="zh"
      @json-change="onJsonChange"
      @json-save="onJsonSave"
      @has-error="onError"
    />
  </div>

</template>
<script>
import vueJsonEditor from 'vue-json-editor'

export default {
  name: 'JsonEditor',
  components: {vueJsonEditor},
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default() {
        return ''
      }
    },
    height: {
      type: Number,
      default: 260
    }
  },
  data() {
    return {
      val: ''
    }
  },
  computed: {},
  mounted() {
    this.val = this.value
  },
  methods: {
    onJsonChange(value) {
      console.log('更改value:', value)
      // 实时保存
      this.onJsonSave(value)
    },
    onJsonSave(value) {
      console.log('保存value:', value)
      if (this.checkJson()) {
        this.$emit('saveVal', value)
      }
      this.hasJsonFlag = true
    },
    onError(value) {
      console.log('json错误了value:', value)
      this.hasJsonFlag = false
    },
    // 检查json
    checkJson() {
      if (this.hasJsonFlag === false) {
        // console.log("json验证失败")
        this.$notify.error({
          title: '错误',
          message: 'json验证失败'
        })
        return false
      } else {
        console.log("json验证成功")
        // this.$notify.success({
        //   title: '提示',
        //   message: 'json验证成功'
        // })
        return true
      }
    }

  }
}
</script>
<style>
/* jsoneditor右上角默认有一个链接,加css去掉了 */
.jsoneditor-poweredBy {
  display: none;
}

.jsoneditor-vue {
  height: 100%;
}

</style>
