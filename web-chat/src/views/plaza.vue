<template>
  <div style="overflow-y: scroll; height: 100%" id="plaza">
    <div
      class="b1-p1-mb-1 br-5 m-t-l-r-5"
      v-for="(item, index) in messageList"
      :key="index"
      @click="toGang(item[item.length - 1].gangId)" :class="{'active':gangId == item[item.length-1].gangId}"
    >
      <strong>{{ item[item.length - 1].gangName }}</strong>
      <span style="float: right">{{
        timeFromNow(item[item.length - 1].createTime)
      }}</span>
      <br />
      <p style="color: #8d8d8d;white-space:nowrap;margin: 0px;overflow:hidden;text-overflow:ellipsis;">
        {{ item[item.length - 1].fromName }}：{{item[item.length - 1].text }}
      </p>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";
export default {
  name: "plaza",
  data() {
    return {
      messageList: this.$store.state.message,
    };
  },
  computed: {},
  created() {},
  methods: {
    toGang(id) {
      this.$router.push('/gang/'+id)
    },
    timeFromNow(oldTime) {
      return oldTime;
    },
  },
  mounted() {},
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
.active{
  background: rgb(228, 228, 228);
}
</style>