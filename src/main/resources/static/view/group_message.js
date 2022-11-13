let template = // html
`
<div style='height:calc(100% - 100px)'>
asdfjkasldjfklajsdf
asdfjkasldjfklajsdfasd
floatasd
floatasdf
</div>
<div style='height:50px'>
<input type='text' v-model='form.text' style='width: calc(100% - 100px);height:100%'>
<button @click="sendMessage()" style='width:100px;height:100%'>发送</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                type:"message",
                text:""
            }
        }
    },
    methods: {
        sendMessage(){
            this.$store.state.socketGroupMap[this.$route.params.groupName].send(JSON.stringify(this.form))
        }
    },
    created() {
		
	}
}
