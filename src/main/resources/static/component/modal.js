let template = // html
`
<button @click='show = !show'>{{buttonName}}</button>
<div class='mode' v-if='show'>
	<div class='mode-body'>
		<div class='header p-10 border-b-1'>
            <button style='width:100%;' @click='close()'>关闭</button>
        </div>
        <div class='body p-10'>
		    <slot></slot>
        </div>
	</div>
</div>
`
export default {
    props: {
        buttonName:{
            type: String,
            default: '按钮'
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