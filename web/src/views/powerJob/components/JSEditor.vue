<template>
  <div class="code-edit">
    <MonacoEditor
      :key="randomKey"
      :code="code"
      :height="300"
      :options="options"
      theme="vs"
      @codeChange="onCodeChange"
      @mounted="onMounted"
    ></MonacoEditor>
  </div>
</template>
<script>
import MonacoEditor from 'vue-monaco-editor'

export default {
  name: 'WorkflowEditor',
  components: { MonacoEditor },
  props: ['code'],
  data() {
    return {
      /** 代码组件 */
      editor: null,
      options: {
        selectOnLineNumbers: false
      },
      randomKey: 1231231
    }
  },
  methods: {
    onMounted(editor) {
      this.editor = editor
    },
    onCodeChange() {
      this.$emit('onCodeChange', this.editor.getValue())
    }
  }
}
</script>
<style>
.code-edit {
  border: 1px solid #f0f0f0;
}

.code-edit .margin-view-overlays.monaco-editor-background {
  width: 0px !important;
}

.code-edit .margin-view-overlays.monaco-editor-background .view-line .line-numbers {
  width: 24px !important;
  left: 0px !important;
}

.code-edit .margin-view-overlays.monaco-editor-background .glyph-margin {
  width: 0px !important;
}

.code-edit .monaco-scrollable-element.editor-scrollable {
  left: 32px !important;
}
</style>

