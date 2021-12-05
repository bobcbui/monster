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
	  <p>入驻时间: {{ time }} </p>
    <var-input
      placeholder="请输入文本"
      :rules="[v => v.length > 6 || '文本长度必须大于6']"
      v-model="value"
    />
<br>

    <var-collapse v-model="value" @change="changeHandle">
      <var-collapse-item title="标题" name="1">内容</var-collapse-item>
      <var-collapse-item title="标题" name="2">内容</var-collapse-item>
    </var-collapse>
    </div>
  </div>
</template>
<script>
import { ref } from 'vue'
export default {
  name: "home",
  data() {
    return {
      value:ref(['1']),
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
	//alert(this.$store.state.login);
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
    };
	if( this.$store.state.login.createTime != null){
		var time = new Date(this.$store.state.login.createTime);
		var y = time.getFullYear();
		var m = time.getMonth()+1;
		var d = time.getDate();
		var h = time.getHours();
		var mm = time.getMinutes();
		var s = time.getSeconds();
		this.time =y+':'+m+':'+d+'  '+h+':'+mm+':'+s;
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