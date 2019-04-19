var btnConvert2 = $('#btnConvert2');
var shortUrl2 = $('#shortUrl2');
var btnCopy2 = $('#btnCopy2');
var inputShortCode = $('#shortCode');
var shortCode = '';

btnConvert2.on('click', function () {
    if ($('#longUrl2').val().length==0 || inputShortCode.val().length==0){
        layer.msg('请完善信息',{icon:0});
        return;
    }
    var data;
    if (inputShortCode.val().length != 0) {
        shortCode = inputShortCode.val().trim();
        data = {longUrl: $('#longUrl2').val(), shortUrl: shortCode}
    }else{
        layer.msg('请输入自定义短码!',{icon: 0});
        inputShortCode.focus();
        return;
    }
    $.ajax({
        url: 'url/convert/short/custom',
        dataType: 'json',
        data: data,
        success: function (res) {
            switch (res.code) {
                case 0:
                    layer.msg('转化成功，可以开始访问了', {icon: 1});
                    shortUrl2.text(res.data.shortUrl);
                    btnCopy2.show();
                    break;
                case -1:
                    layer.msg('请输入正确格式的url',{icon: 0});
                    break;
                default:
                    layer.msg('请输入正确的url', {icon: 0});
                    break;
            }
        },
        error: function (res) {
            layer.msg('服务器内部错误,请稍后再试', {icon: 0})
        }
    });
    shortCode = '';
});
//监听复制按钮
var clipboard = new Clipboard('#btnCopy2');
btnCopy2.on('click', function () {
    clipboard.on('success', function (e) {
        layer.msg("已复制到剪贴板");
    });
    clipboard.on('error', function (e) {
        console.log(e);
    });
});