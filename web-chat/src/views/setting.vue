<template>
  <div
    style="
      height: calc(100% - 21px);
      margin: auto;
      width: 100%;
      max-width: 800px;
    "
  >
    <div
      style="
        height: 100%;
        width: 100%;
        overflow-y: scroll;
        padding: 5px;
        border-bottom: 0px;
      "
    >
      <p style="margin: 0;">名称: <input :value="usernametwo" style="width: 100%;" :disabled="disabled"></p>
      <p style="margin: 0;">性别: <input :value="xbb" style="width: 100%;"  :disabled="disabled"></p>
      <p style="margin: 0;">私信: <input :value="letter" style="width: 100%;"  :disabled="disabled"></p>
      <p style="margin: 0;">简介: <textarea :value="introduction" style="width: 100%;"  :disabled="disabled"></textarea></p>
      
      <p style="margin: 0;">
        <button v-if="disabled" @click="xgai()">修改信息</button>
        <button v-if="!disabled" @click="baocun()">保存</button>
        <button @click="tc" style="float:right">退出</button>
      </p>
    </div>
  </div>
</template>
<script>
export default {
  name: "home",
  data() {
    return {
      disabled:true,
      groupId: this.$route.params.groupId,
      formData: {
        to: this.$route.params.groupId,
        messageText: "",
        type: "group",
      },
      usernametwo: this.$store.state.login.usernametwo,
      username: this.$store.state.login.username,
      xbb: "",
      introduction: this.$store.state.login.introduction,
      letter: "",
      time: this.$store.state.login.createTime,
    };
  },
  computed: {},
  created() {},
  methods: {
    xgai(){
      this.disabled = false
    },
    baocun(){
      this.disabled = true
    },
    toMessage(id) {
      this.$router.push({
        path: "/group/" + id,
      });
    },
     tc(){
      this.$router.push('/login')
    },
    hi() {
      alert("this.$store.state.login.gender");
    },
  },
  mounted() {
    if (this.$store.state.login.gender == null) {
      this.xbb = "未设置";
    } else {
      if (this.$store.state.login.gender == 1) {
        this.xbb = "男";
      } else {
        this.xbb = "女";
      }
    }
    if (this.$store.state.login.letter == true) {
      this.letter = "是";
    } else {
      this.letter = "否";
    }
    if (this.$store.state.login.createTime != null) {
      var time = new Date(this.$store.state.login.createTime);
      var y = time.getFullYear();
      var m = time.getMonth() + 1;
      var d = time.getDate();
      var h = time.getHours();
      var mm = time.getMinutes();
      var s = time.getSeconds();
      this.time = y + ":" + m + ":" + d + "  " + h + ":" + mm + ":" + s;
    }
  },
};
</script>

<style scoped>
.send-but {
  position: fixed;
  bottom: 0px;
}
.message-body {
  height: 500px;
  width: 300px;
  background: rgb(167, 166, 166);
}
.message {
  padding-bottom: 10px;
}
p {
  padding: 10px;
}
</style>