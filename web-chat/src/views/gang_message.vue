<template>
    <div id="message-item-body">
      <div
        class="message-item"
        v-for="(item, index) in $store.state.message[gangId]"
        :key="index"
      >
        <strong>{{ item.fromName }}</strong> <br />
        {{ item.text }}
      </div>
    </div>
    <div id="message-srk">
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
      gangName:"",
      formData: {
        to: this.$route.params.gangId,
        text: "",
        type: "gang",
      },
      usernametwo: this.$store.state.login.usernametwo,
    };
  },
  watch:{
    $route(to, from){
      this.gangId = to.params.gangId
      this.formData.to = this.gangId
    }
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

</style>