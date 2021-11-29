<template>
  <table class="group-message-table" border="1" cellspacing="1" style="height: calc(100% - 27px);">
    <tr>
      <td colspan="2" style="">
        <div style="height: 100%;width:100%; overflow-y: scroll;">
          <p v-for="(item, index) in $store.state.message[groupId]" :key="index">
            {{ item.from }}:{{ item.text }}<br><br>
          </p>
        </div>
      </td>
    </tr>
    <tr>
      <td style="height: 40px;">
        <input class="srk" type="text" v-model="formData.messageText" />
      </td>
      <td style="width:50px" @click="send()"  align="center">发送</td>
    </tr>
  </table>
</template>

<script>
import { inject } from "vue";
export default {
  name: "group_message",
  data() {
    return {
      groupId: this.$route.params.groupId,
      formData: {
        to: this.$route.params.groupId,
        messageText: "",
        type: "group",
      },
      ws: inject("ws"),
    };
  },
  computed: {},
  created() {},
  methods: {
    send() {
      this.ws.send(JSON.stringify(this.formData));
    },
  },
  mounted() {},
};
</script>

<style scoped>

.group-message-table{
  margin: auto;
  width: 100%;
  max-width: 800px;
}
.srk{
  border: 0px ;
  height: 100%;
  width: 100%;
}
</style>