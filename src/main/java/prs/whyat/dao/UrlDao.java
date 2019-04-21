package prs.whyat.dao;

import prs.whyat.entity.Url;

import java.util.List;

/**
 * @ClassName: UrlDao
 * @Description: TODO
 * @Author QiuBo
 * @Date 2019/4/17 23:56
 */
public interface UrlDao {
    Integer countByLongUrl(Url url);

    Url selectShortUrlByLongUrl(String longUrl);

    Integer insertOne(Url url);

    Integer updateDynamically(Url url);

    Integer countByShortUrl(Url url);

    Url selectByShortUrl(Url url);

    //查询自定义短码类型的id
    List<Long> selectIdByCustomType();
}
