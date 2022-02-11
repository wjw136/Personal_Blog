package com.william.blog.service.impl;

import com.william.blog.dao.ArticleCommentMapper;
import com.william.blog.dao.CommentMapper;
import com.william.blog.dto.ArticleCommentDto;
import com.william.blog.entity.ArticleComment;
import com.william.blog.entity.ArticleCommentExample;
import com.william.blog.entity.Comment;
import com.william.blog.entity.CommentExample;
import com.william.blog.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImp implements CommentService {

    @Resource
    CommentMapper commentMapper;
    @Resource
    ArticleCommentMapper articleCommentMapper;

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public void addArticleComment(ArticleCommentDto articleCommentDto) {
        Comment comment= new Comment();
        comment.setContent(articleCommentDto.getContent());
        comment.setEmail(articleCommentDto.getEmail());
        comment.setIp(articleCommentDto.getIp());
        comment.setName(articleCommentDto.getName());
        commentMapper.insertSelective(comment);
        CommentExample commentExample= new CommentExample();
        commentExample.setOrderByClause("id desc");
        Long commentId=commentMapper.selectByExample(commentExample).get(0).getId();

        ArticleComment articleComment= new ArticleComment();
        articleComment.setArticleId(articleCommentDto.getArticleId());
        articleComment.setCommentId(commentId);
        articleCommentMapper.insertSelective(articleComment);

    }

    @Override
    public void deleteCommentById(long id) {
        commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteArticleCommentById(long id) {
        ArticleCommentExample articleCommentExample= new ArticleCommentExample();
        articleCommentExample.or().andCommentIdEqualTo(id);
        List<ArticleComment>ans= articleCommentMapper.selectByExample(articleCommentExample);
        for(int i=0;i<ans.size();++i){
            articleCommentMapper.deleteByPrimaryKey(ans.get(i).getId());
        }
    }

    @Override
    public List<Comment> listAllComment() {
        CommentExample example=new CommentExample();
        return commentMapper.selectByExample(example);
    }

    @Override
    public List<Comment> listAllArticleCommentById(long id) {

        ArticleCommentExample articleCommentExample= new ArticleCommentExample();
        //保证返回的评论有序
        articleCommentExample.setOrderByClause("create_by DESC");
        articleCommentExample.or().andArticleIdEqualTo(id);
        List<ArticleComment> articleComments= articleCommentMapper.selectByExample(articleCommentExample);
        List<Comment> res= new ArrayList<>();
        for(int i=0;i<articleComments.size();++i) {
//            System.out.println(1111);
            Long commentId=articleComments.get(i).getCommentId();
//            System.out.println(commentId);
            Comment comment=commentMapper.selectByPrimaryKey(commentId);
            res.add(comment);
//            CommentExample example = new CommentExample();
//            example.or().andIdEqualTo(commentId);

        }
//        System.out.println(res.get(0).getCreateBy());
        return res;
    }
}
