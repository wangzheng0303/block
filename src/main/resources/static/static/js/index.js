function register() {
    layui.use('layer',function(){

        var layer = layui.layer;
        layer.open({
            type:2,
            title:false,
            area:['450px','480px'],
            closeBtn:0,
            scrollbar : false,
            resize:false,
            move : false,
            shade : 0.8,
            shadeClose :true,
            content : '/user/register.html'

        })

    })

}

function login() {
    layui.use('layer',function(){

        var layer = layui.layer;
        layer.open({
            type:2,
            title:false,
            area:['450px','370px'],
            closeBtn:0,
            scrollbar : false,
            resize:false,
            move : false,
            shade : 0.8,
            shadeClose :true,
            content : '/user/login.html'

        })

    })

}