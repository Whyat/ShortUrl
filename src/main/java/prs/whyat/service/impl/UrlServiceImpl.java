package prs.whyat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prs.whyat.dao.UrlDao;
import prs.whyat.entity.Url;
import prs.whyat.service.UrlService;
import prs.whyat.util.Converter;

import java.util.List;

/**
 * @ClassName: UrlServiceImpl
 * @Description: TODO
 * @Author QiuBo
 * @Date 2019/4/18 0:13
 */
@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    UrlDao urlDao;

    @Override
    public Integer converToShort(Url url) {
        //1.查询数据库是否存在该长连接记录
        Integer count = urlDao.countByLongUrl(url);
        //2.根据存在与否判断
        if (count == 0) {
            //不存在的话插入该长url并返回id
            Long.valueOf(urlDao.insertOne(url));
            //根据url获取id计算短链
            long id = url.getId();
            url.setShortUrl(Converter.shorten(id, url.getLength()));
            //判断短链是否存在，
            if (urlDao.countByShortUrl(url) > 0) {
                //存在则从url属性type=custom的记录取id计算短链
                List<Long> ids = urlDao.selectIdByCustomType();
                for (int i = 0; i < ids.size(); i++) {
                    url.setShortUrl(Converter.shorten(ids.get(i), url.getLength()));
                    if (urlDao.countByShortUrl(url) == 0) {
                        break;
                    }
                }
            }
            //更新短url到数据库
            urlDao.updateDynamically(url);
        } else {
            //存在的话直接查询相应短链并返回
            Url url1 = urlDao.selectShortUrlByLongUrl(url.getLongUrl());
            url.setShortUrl(url1.getShortUrl());
            url.setVisitCount(url1.getVisitCount());
            return 1;
        }
        return 0;
    }

    @Override
    public Integer customConverToShort(Url url) {
        //1.查询数据库是否存在该 长连接 记录
        Integer count = urlDao.countByLongUrl(url);
        //2.根据存在与否判断
        if (count == 0) {
            //判断 短链 是否存在于数据库
            int shortCount = urlDao.countByShortUrl(url);
            if (shortCount == 0) {
                //不存在的话插入该url记录
                Long.valueOf(urlDao.insertOne(url));
                return 0;
            }else{
                //自定义短码已被占用
                url.setShortUrl(null);
                return -2;
            }
        } else {
            //存在的话直接查询相应短链并返回
            Url url1 = urlDao.selectShortUrlByLongUrl(url.getLongUrl());
            url.setShortUrl(url1.getShortUrl());
            url.setVisitCount(url1.getVisitCount());
            return 1;
        }
    }

    @Override
    public Url getLongUrlByShortUrl(Url url) {
        return urlDao.selectByShortUrl(url);
    }

    @Override
    public boolean existsShortUrl(Url url) {
        //判断是否存在该记录根据短地址
        Integer count = urlDao.countByShortUrl(url);
        return count > 0;
    }

    @Override
    public boolean incrVisitCount(Url url) {
        //访问次数+1
        int newCount = url.getVisitCount() + 1;
        url.setVisitCount(newCount);
        //更新访问次数
        Integer count = urlDao.updateDynamically(url);
        return count > 0;
    }
}
