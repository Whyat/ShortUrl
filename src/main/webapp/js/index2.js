var btnConvert2 = $('#btnConvert2');
var shortUrl2 = $('#shortUrl2');
var btnCopy2 = $('#btnCopy2');
var inputShortCode = $('#shortCode');
var shortCode = '';
var intputMaxCount2 = $('#maxCount2');
var maxCount2 = -1;
var inputPassword2 = $('#password2');
var password2 = '';
btnConvert2.on('click', function () {
    if ($('#longUrl2').val().length == 0 || inputShortCode.val().length == 0) {
        layer.msg('请完善信息', {icon: 0});
        return;
    }
    var data;
    if (inputShortCode.val().length != 0) {
        shortCode = inputShortCode.val().trim();
        data = {longUrl: $('#longUrl2').val(), shortUrl: shortCode}
    } else {
        layer.msg('请输入自定义短码!', {icon: 0});
        inputShortCode.focus();
        return;
    }
    if (intputMaxCount2.val().length !=0) {
        maxCount2 = parseInt(intputMaxCount2.val().trim());
        data.maxCount = maxCount2;
    }
    if (inputPassword2.val().length != 0) {
        password2 = inputPassword2.val().trim();
        data.password = password;
    }

    //加载层提示
    var loading = layer.load(2, {time: 10 * 1000});
    $.ajax({
        url: 'url/convert/short/custom',
        dataType: 'json',
        data: data,
        success: function (res) {
            layer.closeAll();
            switch (res.code) {
                case 0:
                    layer.msg('转化成功，该地址还没被访问过 ', {icon: 1});
                    shortUrl2.text(res.data.shortUrl);
                    btnCopy2.show();
                    break;
                case 1:
                    layer.msg('该地址已被转化过，已经访问了 ' + res.data.visitCount + ' 次', {icon: 1});
                    shortUrl.text(res.data.shortUrl);
                    btnCopy.show();
                    break;
                case -2:
                    layer.msg('该短码已被占用，请重新输入', {icon: 1});
                    break;
                default:
                    layer.msg('请输入正确格式的url', {icon: 0});
                    break;
            }
        },
        error: function (res) {
            layer.msg('服务器内部错误,请稍后再试', {icon: 0})
        }
    });
    shortCode = '';
    maxCount2 = -1;
});
//监听复制按钮
var clipboard2 = new Clipboard('#btnCopy2');
btnCopy2.on('click', function () {
    clipboard2.on('success', function (e) {
        layer.msg("已复制到剪贴板");
    });
    clipboard2.on('error', function (e) {
        console.log(e);
    });
});