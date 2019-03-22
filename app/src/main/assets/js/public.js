var fnSelene = {
	isTest: function(){
		return true;//本地模拟
//		return false;//测试环境
	},
	
	params: function(str){
//		console.log(str);
		if(str == "undefined" || str == "null" || str == null){
			str = "";
		}
		var param =  "?" + str + "t=" + Math.random();

		if(fnSelene.isTest()){
			param =  "?" + str + "ticket=chenfei&mallId=125&t=" + Math.random();
		}
		return param;
	},
	domain:function(){
		var add ='http://'+window.location.host;
		if(fnSelene.isTest()){
			add='http://web.test.hgjvip.cn';
		}
		return add;
	},
	setHeader : function (xhr){
		var headername = new Array('ticket','dcode','brand','model','ver','appver','appname','lon','lat');
	       $(headername).each(function(index,item) {
	       		var _value=$.cookie(headername[index]);
				xhr.setRequestHeader(headername[index],_value);
	       });
	}
}