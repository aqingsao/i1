//获取baseURl
var Path = function(){
    var reg = /^\//;
    return {
        getUri : function(url){
            if(reg.test(url)){
                return url;
            } else {
                var pathName = window.document.location.pathname;
                return pathName.substring(0, pathName.substr(1).indexOf('/') + 1) + "/" + url;
            }
        },
        getOrigin : function()
        {
            return window.document.location.origin;

        } ,
        refresh : function()
        {
            window.document.location.reload();
        }
    };
}();
/**
 * 组装查询Uri时使用的工具类，组装时会忽略掉没有值的字段；
 * 对于其他特殊过滤条件的情况，需要提供接口函数，有待增强。
 * 使用方式：
 * var url = QueryUriBuilder.queryParam($scope.patientId, "patientId")
 *  .queryParam(null, "name")
 *  .queryParam($scope.pinyin, "pinyin")
 *  .build();
 *  返回组装好的字符串，结构为——?patientId=value&pinyin=value
 * @type {QueryUriParamBuilder}
 */
var QueryUriParamBuilder = (function () {
    var _urlStr = "";
    var _count = 0;
    var _acceptFilter = function (value2Check) {
        return value2Check!==undefined && value2Check!==null && value2Check!=="";
    };
    var _clearAndReturnGeneratedUrl = function () {
        var _generatedUrl = _urlStr;
        _urlStr = "";
        _count = 0;
        return _generatedUrl;
    };
    return {
        queryParam : function (value2Check, field2Check) {
            if(_acceptFilter(value2Check)) {
                if(_count === 0) {
                    _urlStr += "?" + field2Check + "=" + value2Check;
                } else {
                    _urlStr += "&" + field2Check + "=" + value2Check;
                }
                _count++;
            }
            return this;
        },
        build : function () {
            return _clearAndReturnGeneratedUrl();
        }
    };
})();

var HerenClient = {
    readHerenClient: function(messags,herenClientCallback) {
    $.ajax({
        type: 'get',
        url: 'http://localhost:8089/heren-client',
        dataType: "jsonp",
        data: ''+messags+'',
        jsonp: "callback",
        jsonpCallback: "jsonpCallback",
        success: function (data) {
            herenClientCallback(data);
        },
        error: function (data) {
            alert('p');
            console.log(JSON.stringify(data));
        }
    });
}
};
var HerenMedicare = {
    callHerenMedicare: function(message,herenMedicareCallback) {
    $.ajax({
        type: 'post',
        url: 'http://localhost:8089/heren-medicare',
        dataType: "json",
        data: ''+message+'',
//        jsonp: "callback",
//        jsonpCallback: "jsonpCallback",
        success: function (data) {
            herenMedicareCallback(data);
        },
        error: function (data) {
            alert('error');
            console.log(JSON.stringify(data));
        }
    });
}
};


var userInfo = function(){
    return{
        getUserInfo: function(){
            return window.parent.StaffDictVO;
        },
        getUserId: function(){
            return window.parent.StaffDictVO.empId;
        },
        getUserNo : function(){
            return window.parent.StaffDictVO.empNo;
        } ,
        getUserName:function(){
            return window.parent.StaffDictVO.staffName;
        },
        getDoctorInfo :function(){
            return window.parent.StaffDictVO.doctorDictVO;
        }
    };
}();