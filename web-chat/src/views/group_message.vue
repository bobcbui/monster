<template>
  <div style="height: calc(100% - 21px);  margin: auto;width: 100%;max-width: 800px;">
    <div style="height: calc(100% - 30px);width:100%; overflow-y: scroll;border:1px solid black">
      <p v-for="(item, index) in $store.state.message[groupId]" :key="index">
        {{ item.from }}:{{ item.text }}
      </p>
    </div>
    <div style="height: 30px;">
      <input type="text" v-model="formData.text" style="    width: calc(100% - 35px);padding:2px;
    height: 100%;
    border: 1px solid black;
    border-bottom: 0px;
    border-top: 0"/>
      <div @click="send()" style="float: right;
    height: 100%;
    border: 1px solid black;
    border-top: 0px;
    border-left: 0px;
    border-bottom: 0px;
    line-height: 30px;width: 35px;">发送</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "group_message",
  data() {
    return {
      groupId: this.$route.params.groupId,
      formData: {
        to: this.$route.params.groupId,
        text: "",
        type: "group",
      }
    };
  },
  computed: {},
  created() {},
  methods: {
    send() {
      this.$store.state.ws.send(JSON.stringify(this.formData));
    },
  },
  mounted() {},
};
</script>

<style scoped>

.srk{
  border: 0px ;
  height: 100%;
  width: 100%;
}
</style>