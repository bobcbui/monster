<template>
  <div class="message-body">
    asdfasdf
    <div class="message" v-for="(item,index) in $store.state.message[groupId]" :key="index">{{item.memberId}}:{{item.messageText}}</div>
  </div>
  <div class="send-but">
    <input type="text" v-model="formData.messageText"><button @click="send()">发送</button>
  </div>
</template>

<script>
import { inject } from "vue";
export default {
  name: "group_index",
  data() {
    return {
      groupId:this.$route.params.groupId,
      formData:{
        to:this.$route.params.groupId,
        messageText:"",
        type:"group"
      },
      ws:inject('ws')
    };
  },
  computed: {
    
  },
  created() {

  },
  methods: {
    send(){
      this.ws.send(JSON.stringify(this.formData))
    }
    
  },
  mounted(){
   // alert(JSON.stringify(this.$store.state.message))
  }
};
</script>

<style scoped>
.send-but{
  position:fixed;
  bottom: 0px;
}
.message-body{
  height: 500px;
  width: 300px;
  background: rgb(167, 166, 166);
}
.message{
  padding-bottom: 10px;
}

</style>