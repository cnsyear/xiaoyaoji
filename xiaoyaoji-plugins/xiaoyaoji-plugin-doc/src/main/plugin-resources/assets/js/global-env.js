/**
 * User: zhoujingjie
 * Date: 17/4/25
 * Time: 21:37
 */

(function(){
    requirejs(['vue','utils'],function(Vue,utils){
        var app =new Vue({
            el:'#global-env',
            data:{
                editing:true,
                import:null,
                importValue:null,
                importModal:false,
                envModal:false,
                flag:{
                    tempEnv:null,
                    varname:'$变量$'
                },
                env:null,
                tempStatus:{}
            },
            created:function(){
                var env = xyj.page.global['cn.xiaoyaoji.plugin.global-env'];
                if(!env){
                    env=[];
                }else{
                    env=utils.toJSON(env);
                }
                this.env = env;
            },
            methods:{
                update:function(){
                    utils.post(xyj.ctx+'/plugin/req/cn.xiaoyaoji.plugin.global-env/update',{
                        projectId:xyj.page.projectId,
                        content:JSON.stringify(this.env)
                    });
                    xyj.page.global['cn.xiaoyaoji.plugin.global-env'] = this.env;
                },
                saveHttpEnvironment:function(){
                    submitProjectGlobal();
                },
                createEnv:function(){
                    this.flag.tempEnv={vars:[{}]};
                    this.envModal=true;
                },
                envNewLine: function (index) {
                    if (index === this.flag.tempEnv.vars.length - 1) {
                        this.flag.tempEnv.vars.push({});
                    }
                },
                envEdit: function (item) {
                    this.envModal = true;
                    this.flag.tempEnv=item;
                    if(!this.flag.tempEnv.vars || this.flag.tempEnv.vars.length === 0){
                        this.flag.tempEnv.vars=[{}];
                    }
                },
                envSave: function () {
                    var self = this;
                    if (!this.flag.tempEnv.name) {
                        toastr.error('请输入环境名称');
                        return false;
                    }
                    if (this.flag.tempEnv.vars) {
                        this.flag.tempEnv.vars = this.flag.tempEnv.vars.filter(function (item) {
                            return item.name != undefined && item.name != null && item.name != '';
                        });
                    }
                    if (!this.flag.tempEnv) {
                        this.flag.tempEnv = {vars:[]};
                    }
                    //表示修改
                    if (this.flag.tempEnv.t) {
                        var t = this.flag.tempEnv.t;
                        var index = this.flag.tempEnv.vars.findIndex(function (item) {
                            return item.t == t;
                        });
                        if (index != -1) {
                            this.env.$set(index, this.flag.tempEnv);
                        }
                    } else {
                        this.flag.tempEnv.t=Date.now();
                        this.env.push(this.flag.tempEnv);
                    }
                    this.env = this.env.map(function (item) {
                        return {name: item.name, t: item.t, vars: item.vars}
                    });
                    this.envModal= false;
                    this.update();
                },
                removeEnvironment:function(index){
                    this.env.splice(index);
                    this.update();
                },
                copyEnvironment: function (item) {
                    var temp = $.extend(true, {}, item);
                    temp.t = Date.now();
                    this.env.push(temp);
                    this.update();
                }
            }
        });
    });

})();
