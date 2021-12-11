<template>

    <div style="height: calc(100% - 200px);overflow-y: scroll;">
      <div
        class="message-item"
        v-for="(item, index) in $store.state.message[gangId]"
        :key="index"
        style=""
      >
        <strong>{{ item.fromName }}</strong> <br />
        {{ item.text }}
      </div>
    </div>
    <div style="height:200px;padding:5px;background: #d6d0c1;    border-top: 1px solid black;">
      <textarea v-model="formData.text" class="message-input"></textarea>
      <button @click="send()" class="send-but">发送</button>
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
      },
      usernametwo: this.$store.state.login.usernametwo,
    };
  },
  computed: {
    
  },
  created() {

  },
  activated() {

  },

  methods: {
    send() {
      if (this.formData.text == "") {
        return;
      }
      this.$store.state.ws.send(JSON.stringify(this.formData));
      this.formData.text = "";
    },
  },
  mounted() {
    
  },
};
</script>

<style scoped>
.message-item {
  border: 1px solid black;
  background: #dfd4c9;
  border-radius: 5px;
  margin: 4px;
}
.message-input {
  width: calc(100% - 55px);
  height: 100%;
}
.send-but{
  float: right;
  height: 100%;
  width: 50px;
}
</style>