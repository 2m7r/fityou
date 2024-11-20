package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workout.model.dao.CommentDao;
import com.workout.model.dto.Comment;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public int addComment(Comment comment) {
        return commentDao.insertComment(comment);
    }

    @Override
    public int removeComment(long commentId) {
        return commentDao.deleteComment(commentId);
    }

    @Override
    public List<Comment> getCommentsByTarget(long targetId, String targetType) {
        return commentDao.selectCommentsByTarget(targetId, targetType);
    }
    
    @Override
    public int updateComment(Comment comment) {
        return commentDao.updateComment(comment);  // 댓글 수정
    }
}