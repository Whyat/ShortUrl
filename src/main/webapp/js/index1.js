var btnConvert = $('#btnConvert');
var shortUrl = $('#shortUrl');
var btnCopy = $('#btnCopy');
var inputLength = $('#length');
var length = 4;
var intputMaxCount = $('#maxCount');
var maxCount = -1;
var inputPassword = $('#password');
var password = '';
btnConvert.on('click', function () {
    if ($('#longUrl').val().length == 0) {
        layer.msg('请填写要转化的网址', {icon: 0});
        return;
    }
    var data = {};
    data.longUrl = $('#longUrl').val();
    data.length = 4;
    if (inputLength.val().length != 0) {
        length = parseInt(inputLength.val().trim());
        data.length = length;
    }
    if (intputMaxCount.val().length != 0) {
        maxCount = parseInt(intputMaxCount.val().trim());
        data.maxCount = maxCount;
    }
    if (inputPassword.val().length != 0) {
        password =inputPassword.val().trim();
        data.password = password;
    }
    //加载层提示
    var loading = layer.load(2, {time: 10 * 1000});
    $.ajax({
        url: 'url/convert/short',
        dataType: 'json',
        data: data,
        success: function (res) {
            layer.closeAll();
            switch (res.code) {
                case 0:
                    layer.msg('转化成功，该地址还没被访问过 ', {icon: 1});
                    shortUrl.text(res.data.shortUrl);
                    btnCopy.show();
                    break;
                case 1:
                    layer.msg('该地址已被转化过，已经访问了 ' + res.data.visitCount + ' 次', {icon: 1});
                    shortUrl.text(res.data.shortUrl);
                    btnCopy.show();
                    break;
                default:
                    layer.msg('请输入正确格式的url', {icon: 0});
                    break;
            }
        }
        ,
        error: function (res) {
            layer.msg(res.msg, {icon: 0})
        }
    });
    length = 4;
    maxCount = -1;
});
layui.use('element', function () {
    var element = layui.element;
});
//监听复制按钮
var clipboard = new Clipboard('#btnCopy');
btnCopy.on('click', function () {
    clipboard.on('success', function (e) {
        layer.msg("已复制到剪贴板");
    });
    clipboard.on('error', function (e) {
        console.log(e);
    });
});