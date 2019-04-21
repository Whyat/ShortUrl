package prs.whyat.service;

import prs.whyat.entity.Url;

/**
 * @ClassName: UrlService
 * @Description: TODO
 * @Author QiuBo
 * @Date 2019/4/18 0:11
 */
public interface UrlService {
    //转短链接
    Integer converToShort(Url url);

    //自定义转化短链接
    Integer customConverToShort(Url url);

    //转长链接
    Url getLongUrlByShortUrl(Url url);

    //判断是否存在该短地址
    boolean existsShortUrl(Url url);

    boolean incrVisitCount(Url url);


}
