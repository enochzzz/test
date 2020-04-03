package com.ptteng.common.skill.util;

import com.gemantic.dal.config.helper.GroupHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xu on 16/4/27.
 */
public class ArticleUtil {

    private static final Log log = LogFactory.getLog(ArticleUtil.class);

    // 获取上一篇下一篇
    public static List<Long> getBeforeNextArticleId(int type, Long typeId, Long did) {
        log.info("type: " + type + " ,typeId: " + typeId + " ,did: " + did);

        List<Long> ids = new ArrayList<>();
        try {
            DataSource dataSource = GroupHelper.getDataSource("common_skill");
            log.info("getBeforeNextArticleId datasource: " + dataSource);

            SimpleJdbcTemplate simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);

            StringBuilder sql = null;

            String typeName = "id";
            if (type == 1) {
                typeName = "uid";
            }
            if (type == 2) {
                typeName = "cid";
            }
            if (type == 3) {
                typeName = "oid";
            }





            // 查询上一条
            sql = new StringBuilder("SELECT id FROM article WHERE " + typeName + " = ? AND id > ? ORDER BY id ASC  LIMIT 0,1 ");
            log.info("before sql: " + sql.toString());
            long before = -1L;
            try {
                before = simpleJdbcTemplate.queryForLong(sql.toString(), typeId, did);
            } catch (DataAccessException e) {
                log.info("don't have before ");
            }
            log.info("before: " + before);

            // 查询下一条
            sql = new StringBuilder("SELECT id FROM article WHERE " + typeName + " = ? AND id < ? ORDER BY id DESC LIMIT 0,1 ");
            log.info("next sql: " + sql.toString());
            long next = -1L;
            try {
                next = simpleJdbcTemplate.queryForLong(sql.toString(), typeId, did);
            } catch (DataAccessException e) {
                log.info("don't have next ");
            }
            log.info("next: " + next);









            ids.add(before);
            ids.add(next);

            log.info("ids: " + ids);
        } catch (Exception e) {
            log.error("getBeforeNextArticleId error ", e);
        }
        return ids;
    }


    public static void main(String[] args) {
        List<Long> beforeNextArticleId = ArticleUtil.getBeforeNextArticleId(3, 5L, 153L);

    }


}
