var btnConvert = $('#btnConvert');
var shortUrl = $('#shortUrl');
var btnCopy = $('#btnCopy');
var inputLength = $('#length');
var length = 4;
btnConvert.on('click', function () {
    if ($('#longUrl').val().length == 0) {
        layer.msg('请填写要转化的网址', {icon: 0});
        return;
    }
    if (inputLength.val().length != 0) {
        length = parseInt(inputLength.val().trim())
    }
    $.ajax({
        url: 'url/convert/short',
        dataType: 'json',
        data: {longUrl: $('#longUrl').val(), length: length},
        success: function (res) {
            if (res.code == 0) {
                layer.msg('转化成功，可以开始访问了', {icon: 1});
                shortUrl.text(res.data.shortUrl);
                btnCopy.show();
            } else {
                layer.msg('请输入正确的url', {icon: 0});
            }
        },
        error: function (res) {
            layer.msg(res.msg, {icon: 0})
        }
    });
    length = 4;
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