(function(){
    require(['vue'],function(Vue){
        //传递消息给sidebar.js 
        function pushMessage(method,args){
            window.postMessage({type:'event',method:method,args:args},'*')
        }
        var app = new Vue({
            el:'#xd-header',
            data:{
                pages:[]
            },
            mounted:function(){
                $.get('http://www.xiaoyaoji.cn/test/pages.json?_t='+Date.now(),function(rs){
                    if(rs.code==0){
                        app.pages=rs.data;
                    }
                })
            },
            methods:{
                sidebar:function(method){
                    var newArgs=[];
                    if(arguments.length>1){
                        for(var i=1;i<arguments.length;i++){
                            newArgs[i-1] = arguments[i];
                        }
                    }
                    pushMessage(method,newArgs);
                }
            }
        })
    })
})();