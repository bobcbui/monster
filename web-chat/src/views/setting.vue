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
        border: 1px solid black;
        border-bottom: 0px;
      "
    >
      <p>用户名: {{ usernametwo }}</p>
      <p>性别：{{ xbb }}</p>
      <p>简介：{{ introduction }}</p>
      <p>是否可以私信：{{ letter }}</p>
    </div>
  </div>
</template>
<script>
export default {
  name: "home",
  data() {
    return {
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
    };
  },
  computed: {},
  created() {},
  methods: {
    toMessage(id) {
      this.$router.push({
        path: "/group/" + id,
      });
    },
    hi() {
      alert(this.$store.state.login.gender);
    },
  },
  mounted() {
    console.log(JSON.stringify(this.$store.state.login));
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