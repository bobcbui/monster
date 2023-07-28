let template = // html
`
<div style='width:100%;border-bottom: 1px solid black;padding:10px;height: 44px;'>
    <strong>🙂{{title}}</strong>
    <div style='float:right'>
        <slot></slot>
    </div>
</div>
`
export default {
    props:{
        title: {
            type: String,
            default: '标题'
        }
    },
    template: template,
    methods:{
       
    }
}