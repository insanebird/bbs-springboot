package com.example.demo.controller;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Transactional
public class UserController {

    @Autowired
    UserDao userDao;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @PostMapping("/regist")
    public CommonResult regist(@RequestBody User user) {
        int res = userDao.regist(user);
        if (res > 0)
            return new CommonResult(200, "成功", user);
        else {
            return new CommonResult(404, "失败", null);
        }
    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody User user) {
        User login = userDao.login(user.getName(), user.getPwd());
        if (login != null) {
            return new CommonResult(200, "SUCCESS", login);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @PostMapping("/publish")
    public CommonResult publish(@RequestBody Article article) {
        article.setTime(new Date());
        int publish = userDao.publish(article);
        int i = userDao.insertTagsArticle(article.getTag(), article.getId());
        User user = userDao.getUser(article.getAuthor());
        updateexpLevel(user, 10);
        if (publish > 0 && i > 0) {
            return new CommonResult(200, "SUCCESS", userDao.getUser(article.getAuthor()));
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getThemes")
    public CommonResult getThemes() {
        List<Article> themes = userDao.getThemes();
        for (Article article : themes) {
            article.setTagName(userDao.getTagName(article.getTag()));
            article.setAuthorName(userDao.getAuthorName(article.getAuthor()));
            article.setDateTime(simpleDateFormat.format(article.getTime()));
            article.setIdentity(userDao.getIdentity(article.getAuthor()));
        }
        if (themes.size() > 0) {
            return new CommonResult(200, "SUCCESS", themes);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/themes/{id}")
    public CommonResult getTheme(@PathVariable Integer id) {
        Article theme = userDao.getTheme(id);
        theme.setDateTime(simpleDateFormat.format(theme.getTime()));
        theme.setAuthorName(userDao.getAuthorName(theme.getAuthor()));
        theme.setIdentity(userDao.getIdentity(theme.getAuthor()));
        if (theme != null) {
            return new CommonResult(200, "SUCCESS", theme);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @PostMapping("/publishSubtitle")
    public CommonResult publishSubtitle(@RequestBody Subtitle subtitle) {
        subtitle.setTime(new Date());
        int i = userDao.publishSubtitle(subtitle);
        subtitle.setIdentity(userDao.getIdentity(subtitle.getAuthor()));
        Article theme = userDao.getTheme(subtitle.getTid());
        User user = userDao.getUser(theme.getAuthor());
        updateexpLevel(user, 5);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", subtitle);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getSubtitles/{id}")
    public CommonResult getSubtitles(@PathVariable Integer id) {
        List<Subtitle> subtitles = userDao.getSubtitles(id);
        for (Subtitle subtitle : subtitles) {
            subtitle.setDateTime(simpleDateFormat.format(subtitle.getTime()));
            subtitle.setAuthorName(userDao.getAuthorName(subtitle.getAuthor()));
            subtitle.setIdentity(userDao.getIdentity(subtitle.getAuthor()));
        }
        if (subtitles.size() > 0) {
            return new CommonResult(200, "SUCCESS", subtitles);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @PostMapping("/publishComment")
    public CommonResult publishComment(@RequestBody Comment comment) {
        comment.setTime(new Date());
        int i = userDao.publishComment(comment);
        Article theme = userDao.getTheme(comment.getTid());
        User user = userDao.getUser(theme.getAuthor());
        updateexpLevel(user, 2);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", comment);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getComments/{id}")
    public CommonResult getComments(@PathVariable Integer id) {
        List<Comment> comments = userDao.getComments(id);
        for (Comment comment : comments) {
            comment.setDateTime(simpleDateFormat.format(comment.getTime()));
            comment.setAuthorName(userDao.getAuthorName(comment.getAuthor()));
            comment.setTargetName(userDao.getAuthorName(comment.getTarget()));
            comment.setIdentity(userDao.getIdentity(comment.getAuthor()));
        }
        if (comments.size() > 0) {
            return new CommonResult(200, "SUCCESS", comments);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getLevels")
    public CommonResult getLevels() {
        List<Level> levels = userDao.getLevels();
        if (levels.size() > 0) {
            return new CommonResult(200, "SUCCESS", levels);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getTags")
    public CommonResult getTags() {
        List<Tag> tags = userDao.getTags();
        if (tags.size() > 0) {
            return new CommonResult(200, "SUCCESS", tags);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    public void updateexpLevel(User user, Integer score) {
        Level level = userDao.getLevel(user.getLevel() + 1);
        if (user.getExp() + score >= level.getExp()) {
            userDao.updateLevelexp(user.getLevel() + 1, user.getExp() + score - level.getExp(), user.getUid());
        } else {
            userDao.updateLevelexp(user.getLevel(), user.getExp() + score, user.getUid());
        }
    }

    @GetMapping("/checkName/{name}")
    public CommonResult checkName(@PathVariable String name) {
        User user = userDao.checkName(name);
        if (user != null) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getArticleByModule/{id}")
    public CommonResult getArticleByModule(@PathVariable Integer id) {
        List<Article> articleByModule = userDao.getArticleByModule(id);
        for (Article article : articleByModule) {
            article.setDateTime(simpleDateFormat.format(article.getTime()));
            article.setAuthorName(userDao.getAuthorName(article.getAuthor()));
            article.setTagName(userDao.getTagName(article.getTag()));
            article.setIdentity(userDao.getIdentity(article.getAuthor()));
        }
        if (articleByModule.size() > 0) {
            return new CommonResult(200, "SUCCESS", articleByModule);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/deleteComment/{id}")
    public CommonResult deleteComment(@PathVariable Integer id) {
        int i = userDao.deleteComment(id);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/deleteSubtitle/{id}")
    public CommonResult deleteSubtitle(@PathVariable Integer id) {
        int i = userDao.deleteSubtitle(id);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/deleteArticle/{id}")
    public CommonResult deleteArticle(@PathVariable Integer id) {
        int i = userDao.deleteArticle(id);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getUsers")
    public CommonResult getUsers() {
        List<User> users = userDao.getUsers();
        for (User user : users) {
            user.setTagName(userDao.getTagNames(user.getUid()));
        }
        if (users.size() > 0) {
            return new CommonResult(200, "SUCCESS", users);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/concealManage/{id}/{identity}")
    public CommonResult concealManage(@PathVariable Integer id, @PathVariable Integer identity) {
        int i = userDao.updateIdentity(id, identity);
        List<Integer> tagIdByUserId = userDao.getTagIdByUserId(id);
        int j = userDao.concealManage(id);
        if (tagIdByUserId.size() > 0) userDao.updateTagByList(tagIdByUserId);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @PostMapping("/updateManage")
    public CommonResult updateManage(@RequestBody TransformTag tags) {
        int i = 0, j = 0;
        i = userDao.concealManage(tags.getId());
        if (tags.getTags().size() > 0) j = userDao.insertTagUser(tags.getTags(), tags.getId());
        userDao.updateTagSetZero();
        userDao.updateTagState();
        if (i > 0 || j > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getTagByUserId/{id}")
    public CommonResult getTagByUserId(@PathVariable Integer id) {
        List<Tag> tagByUserId = userDao.getTagByUserId(id);
        if (tagByUserId.size() > 0) {
            return new CommonResult(200, "SUCCESS", tagByUserId);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getUntag")
    public CommonResult getUntag() {
        List<Tag> untag = userDao.getUntag();
        if (untag.size() > 0) {
            return new CommonResult(200, "SUCCESS", untag);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/updateIdentity/{id}/{identity}")
    public CommonResult updateIdentity(@PathVariable Integer id, @PathVariable Integer identity) {
        int i = userDao.updateIdentity(id, identity);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/getTagList")
    public CommonResult getTagList() {
        List<Tag> tags = userDao.getTags();
        for (Tag tag : tags) {
            tag.setUname(userDao.getTagOwnerById(tag.getId()));
        }
        if (tags.size() > 0) {
            return new CommonResult(200, "SUCCESS", tags);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/addTag/{name}")
    public CommonResult addTag(@PathVariable String name) {
        int i = userDao.addTag(name);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/deleteTag/{id}")
    public CommonResult deleteTag(@PathVariable Integer id) {
        int i = userDao.deleteTag(id);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @GetMapping("/updateTag/{id}/{name}")
    public CommonResult updateTag(@PathVariable Integer id, @PathVariable String name) {
        int i = userDao.updateTag(id, name);
        if (i > 0) {
            return new CommonResult(200, "SUCCESS", null);
        } else {
            return new CommonResult(404, "FAILED", null);
        }
    }

    @PostMapping("/upload")
    public CommonResult upload(HttpServletRequest request) throws IOException {
        List<String> images = new ArrayList<>();
        List<MultipartFile> multipartFiles = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = "/software/apache-tomcat-8.5.51/webapps/bbs/static/upload/";
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        for (int i = 0; i < multipartFiles.size(); i++) {
            MultipartFile file = multipartFiles.get(i);
            if (file.isEmpty()) {
                return new CommonResult(404, "FAILED", "上传第" + (i++) + "个文件失败");
            }
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            try {
                file.transferTo(Paths.get(filePath + fileName));
                images.add("http://47.93.56.90:8080/" + "bbs/static/upload/" + fileName);
            } catch (IOException e) {
                return new CommonResult(404, "FAILED", "上传第" + (i++) + "个文件失败");
            }
        }
        return new CommonResult(200, "SUCCESS", images);
    }
}