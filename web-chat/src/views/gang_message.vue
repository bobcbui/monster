<template>
  <div style="height: calc(100% - 22px);  margin: auto;width: 100%;max-width: 800px;">
    <div style="height: calc(100% - 30px);width:100%; overflow-y: scroll;border:1px solid black;background: #ebd3a6;">
      <p v-for="(item, index) in $store.state.message[gangId]" :key="index" style="    border: 1px solid black;
    margin: 5px;
    border-radius: 5px;">
        <strong>{{ item.from }}</strong> <br> {{ item.text }}
      </p>
    </div>
    <div style="height: 30px;">
      <input type="text" v-model="formData.text" style="    width: calc(100% - 35px);padding:2px;background: #e7e7e7;
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
  name: "gang_message",
  data() {
    return {
      gangId: this.$route.params.gangId,
      formData: {
        to: this.$route.params.gangId,
        text: "",
        type: "gang",
      }
    };
  },
  computed: {},
  created() {},
  methods: {
    send() {
      if(this.formData.text == ""){
        return;
      }
      this.$store.state.ws.send(JSON.stringify(this.formData));
	    this.formData.text = "";
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