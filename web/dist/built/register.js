webpackJsonp([19],[function(t,e,a){"use strict";function s(t){return t&&t.__esModule?t:{"default":t}}var n=a(1),i=s(n),u=a(9),o=s(u);new i["default"]({el:"#register",data:{password:null,nickname:null,email:null},methods:{submit:function(){return this.$validate(!0),!!this.$form.valid&&void o["default"].post("/user/register.json",{email:this.email,password:this.password,nickname:this.nickname},function(t){o["default"].token(t.data.token),o["default"].user(t.data.user),toastr.success("注册成功!"),location.href=o["default"].config.ctx+"/dashboard/"})}}})}]);