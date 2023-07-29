let template = // html
`
<button @click='show = !show' class='h-100'>{{buttonName}}</button>
<div class='mode' v-if='show'>
	<div class='mode-body'>
		<div class='header p-10 border-b-1' style='background: rgb(255, 222, 252);'>
            <button style='width:100%;' @click='close()'>关闭</button>
        </div>
        <div class='body p-10' style='background: rgb(255, 248, 248);height:100%;text-align: left'>
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
    data: () => {
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