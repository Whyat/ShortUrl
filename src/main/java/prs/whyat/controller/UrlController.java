package prs.whyat.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import prs.whyat.commons.ResultBean;
import prs.whyat.entity.Url;
import prs.whyat.service.UrlService;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: UrlController
 * @Description: TODO
 * @Author QiuBo
 * @Date 2019/4/17 23:41
 */
@Controller
public class UrlController {
    @Autowired
    private UrlService urlService;

    @RequestMapping(value = "/r/{shortUrl}")
    public ModelAndView redirectToLong(@PathVariable("shortUrl")String shortUrl, HttpServletResponse resp) throws Exception {
        String longUrl = "";
        Url url = new Url();
        url.setShortUrl(shortUrl);
        if (urlService.existsShortUrl(url)) {
            longUrl = urlService.getLongUrlByShortUrl(url).getLongUrl();
            //访问数实现+1
            urlService.incrVisitCount(url);
            //如果不包含http://则添加
            if (!longUrl.contains("http://")) {
                longUrl = "http://"+longUrl;
            }
            return new ModelAndView(new RedirectView(longUrl));
        }
        return null;
    }

    //转短连接
    @RequestMapping("/url/convert/short")
    @ResponseBody
    public ResultBean<Url> convert(Url url) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(url.getLongUrl().trim());
        isurl = mat.matches();//判断是否匹配
        ResultBean<Url> resultBean = new ResultBean<>();
        if (isurl) {
            url = urlService.converToShort(url);
            url.setShortUrl("http://118.24.17.183:8080/r/"+url.getShortUrl());
            resultBean.setCode(0);
            resultBean.setData(url);
        }else{
            //不符合url的格式
            resultBean.setCode(-1);
        }
        return resultBean;
    }

    //转短连接 自定义短码
    @RequestMapping("/url/convert/short/custom")
    @ResponseBody
    public ResultBean<Url> customConvert(Url url) {
        url.setLongUrl(url.getLongUrl());
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(url.getLongUrl().trim());
        isurl = mat.matches();//判断是否匹配
        ResultBean<Url> resultBean = new ResultBean<>();
        if (isurl) {
            //自定义短码,设置成自定义类型
            url.setType("custom");
            int code = urlService.customConverToShort(url);
            url.setShortUrl("http://118.24.17.183:8080/r/"+url.getShortUrl());
            resultBean.setCode(code);
            resultBean.setData(url);
        }else{
            //不符合url的格式
            resultBean.setCode(-1);
        }
        return resultBean;
    }
}
