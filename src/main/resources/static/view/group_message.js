let template = // html
`
<div>
<input type='text' v-model='form.text'>
<button @click="sendMessage()">发送</button>
</div>
`
export default {
    template: template,
    data: function(){
        return {
            form:{
                text:""
            }
        }
    },
    methods: {
        sendMessage(){
            
        }
    },
    created() {
		
	}
}
