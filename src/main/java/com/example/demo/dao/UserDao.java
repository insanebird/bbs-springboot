package com.example.demo.dao;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    @Insert("insert into user (name,pwd) values(#{name},#{pwd})")
    @Options(useGeneratedKeys = true, keyProperty = "uid", keyColumn = "uid")
    public Integer regist(User user);

    @Select("select * from user where name=#{name} and pwd=#{pwd}")
    public User login(String name, String pwd);

    @Insert("insert into article (title,author,content,time,level,tag) values(#{title},#{author},#{content},#{time},#{level},#{tag})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public Integer publish(Article article);

    @Select("select * from article")
    public List<Article> getThemes();

    @Select("select * from article where id=#{id}")
    public Article getTheme(Integer id);

    @Insert("insert into subtitle (content,author,time,tid) values(#{content},#{author},#{time},#{tid})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public Integer publishSubtitle(Subtitle subtitle);

    @Select("select * from subtitle where tid=#{tid}")
    public List<Subtitle> getSubtitles(Integer tid);

    @Insert("insert into comment (content,author,time,sid,target,tid) values(#{content},#{author},#{time},#{sid},#{target},#{tid})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public Integer publishComment(Comment subtitle);

    @Select("select * from comment where sid=#{sid}")
    public List<Comment> getComments(Integer sid);

    @Select("select * from level")
    public List<Level> getLevels();

    @Select("select * from tag")
    public List<Tag> getTags();


    @Insert("insert into tag_article values(#{tid},#{aid})")
    Integer insertTagsArticle(Integer tid, Integer aid);

    @Update("update user set level=#{level},exp=#{exp} where uid=#{id}")
    Integer updateLevelexp(Integer level, Integer exp, Integer id);

    @Select("select * from level where id=#{id}")
    public Level getLevel(Integer id);

    @Select("select * from user where uid=#{id}")
    public User getUser(Integer id);

    @Select("select tag.name from tag where id=#{id}")
    public String getTagName(Integer id);

    @Select("select name as authorName from user where uid=#{id}")
    public String getAuthorName(Integer id);

    @Select("select * from user where name=#{name}")
    public User checkName(String name);

    @Select("select a.id,a.title,a.author,a.content,a.time,a.level,a.tag from article as a,tag_user where tag_user.uid=#{id} and tag_user.tid=tag")
    public List<Article> getArticleByModule(Integer id);

    @Select("select identity from user where uid=#{id}")
    public Integer getIdentity(Integer id);

    @Delete("delete from comment where id=#{id}")
    public Integer deleteComment(Integer id);

    @Delete("delete from subtitle where id=#{id}")
    public Integer deleteSubtitle(Integer id);

    @Delete("delete from article where id=#{id}")
    public Integer deleteArticle(Integer id);

    @Select("select * from user where identity!=2")
    public List<User> getUsers();

    @Select("select name from tag,tag_user where tag_user.tid=tag.id and tag_user.uid=#{id}")
    public List<String> getTagNames(Integer id);

    @Select("select tid as id from tag_user where uid=#{id}")
    public List<Integer> getTagIdByUserId(Integer id);

    @Delete("delete from tag_user where uid=#{id}")
    public Integer concealManage(Integer id);

    @Insert({"<script>",
            "insert into tag_user values ",
            "<foreach collection='list' item='item' index='index' separator=','>",
            "(#{item}, #{uid})",
            "</foreach>",
            "</script>"})
    public Integer insertTagUser(List<Integer> list, Integer uid);

    @Update("update user set identity=#{identity} where uid=#{id}")
    public Integer updateIdentity(Integer id, Integer identity);

    @Select("select tag.id,tag.name,tag.state from tag,tag_user where tag.id=tag_user.tid and tag_user.uid=#{id}")
    public List<Tag> getTagByUserId(Integer id);

    @Select("select * from tag where state=0")
    public List<Tag> getUntag();

    @Update("update tag set state=1 where id in(select tid from tag_user)")
    public Integer updateTagState();

    @Update("update tag set state=0")
    public Integer updateTagSetZero();

    @Update({"<script>",
            "update tag set state=0 where id in ",
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    public Integer updateTagByList(List<Integer> list);

    @Select("select user.name as uname from tag,tag_user,user where tag_user.tid=tag.id and tag_user.uid=user.uid and tag.id=#{id}")
    public String getTagOwnerById(Integer id);

    @Insert("insert into tag (name) values(#{name})")
    public Integer addTag(String name);

    @Delete("delete from tag where id=#{id}")
    public Integer deleteTag(Integer id);

    @Update("update tag set name=#{name} where id=#{id}")
    public Integer updateTag(Integer id, String name);
}