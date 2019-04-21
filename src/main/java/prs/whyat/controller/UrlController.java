package prs.whyat.controller;

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

import javax.servlet.http.HttpServletRequest;
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

    //转短连接
    @RequestMapping("/url/convert/short")
    @ResponseBody
    public ResultBean<Url> convert(Url url) {
        url.setLongUrl(url.getLongUrl().toLowerCase());
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(url.getLongUrl().trim());
        isurl = mat.matches();//判断是否匹配
        ResultBean<Url> resultBean = new ResultBean<>();
        int code = -1;
        if (isurl) {
            code = urlService.converToShort(url);
            url.setShortUrl("http://47.102.200.145:8080/UrlConvert/r/" + url.getShortUrl());
            resultBean.setData(url);
        } else {
            //不符合url的格式
            resultBean.setCode(-1);
        }
        //设置状态码
        resultBean.setCode(code);
        return resultBean;
    }

    //转短连接 自定义短码
    @RequestMapping("/url/convert/short/custom")
    @ResponseBody
    public ResultBean<Url> customConvert(Url url) {
        url.setLongUrl(url.getLongUrl().toLowerCase());
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(url.getLongUrl().trim());
        isurl = mat.matches();//判断是否匹配
        ResultBean<Url> resultBean = new ResultBean<>();
        int code = -1;
        if (isurl) {
            //自定义短码,设置成自定义类型
            url.setType("custom");
            url.setLength(url.getShortUrl().length());
            code = urlService.customConverToShort(url);
            url.setShortUrl("http://47.102.200.145:8080/UrlConvert/r/" + url.getShortUrl());
            resultBean.setCode(code);
            resultBean.setData(url);
        } else {
            //不符合url的格式
            resultBean.setCode(-1);
        }
        //设置状态码
        resultBean.setCode(code);
        return resultBean;
    }

    @RequestMapping(value = "/r/{shortUrl}")
    public ModelAndView redirectToLong(@PathVariable("shortUrl") String shortUrl, HttpServletRequest req) {
        String longUrl = "";
        Integer maxCount = -1;
        String psw = null;
        Url url = new Url();
        url.setShortUrl(shortUrl);
        if (urlService.existsShortUrl(url)) {
            Url url1 = urlService.getLongUrlByShortUrl(url);
            longUrl = url1.getLongUrl();
            maxCount = url1.getMaxCount();
            psw = url1.getPassword();
            //若设置了密码返回设置密码页面
            if (psw != null) {
                ModelAndView mv = new ModelAndView("pswRequire.jsp");
                // mv.addObject("shortUrl", url1.getShortUrl());
                req.getSession().setAttribute("shortUrl", url1.getShortUrl());
                return mv;
            }
            //若设置了访问次数上限
            if (maxCount != -1 && url1.getVisitCount() >= maxCount) {
                return new ModelAndView("exceedMaxCount.html");
            }
            //访问数实现+1
            urlService.incrVisitCount(url1);
            //如果不包含http://则添加
            if (!longUrl.contains("http://")) {
                longUrl = "http://" + longUrl;
            }
            return new ModelAndView(new RedirectView(longUrl));
        } else {
            return new ModelAndView("404.html");
        }
    }

    /**
     * 带密码的跳转
     * @param url
     * @return
     */
    @RequestMapping(value = "/r")
    public ModelAndView redirectToLongWithPsw(Url url,HttpServletRequest req) {
        String longUrl = "";
        Integer maxCount = -1;
        //session中获取shortUrl
        url.setShortUrl((String) req.getSession().getAttribute("shortUrl"));
        Url url1 = urlService.getLongUrlByShortUrl(url);
        if (!url.getPassword().equals(url1.getPassword())) {
            return new ModelAndView("wrongPsw.html");
        }else{
            longUrl = url1.getLongUrl();
            maxCount = url1.getMaxCount();
            //若设置了访问次数上限
            if (maxCount != -1 && url1.getVisitCount() >= maxCount) {
                return new ModelAndView("exceedMaxCount.html");
            }
            //访问数实现+1
            urlService.incrVisitCount(url1);
            //如果不包含http://则添加
            if (!longUrl.contains("http://")) {
                longUrl = "http://" + longUrl;
            }
            return new ModelAndView(new RedirectView(longUrl));
        }
    }
}
