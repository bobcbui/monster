let template = // html
`
<div class='mode' v-if='show'>
	<div class='mode-body'>
		<button style='width:100%;margin-bottom:5px;' @click='close()'>关闭</button>
		<slot></slot>
	</div>
</div>
`
export default {
    props: {
        title:{
            type: String,
            default: '标题'
        }
    },
    data: function () {
        return {
            show: false
        }
    },
    methods: {
        close(){
            this.show = false;
        },
        open(){
            this.show = true;
        }
    },
    template: template
}