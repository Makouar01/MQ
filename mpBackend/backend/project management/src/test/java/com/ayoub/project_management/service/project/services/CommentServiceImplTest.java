package com.ayoub.project_management.service.project.services;

import com.ayoub.project_management.Repository.CommentRepository;
import com.ayoub.project_management.Repository.IssueRepository;
import com.ayoub.project_management.Repository.UserRepository;
import com.ayoub.project_management.model.Comment;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.service.comment.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateComment_Success() throws Exception {
        Long issueId = 1L;
        Long userId = 2L;
        String content = "Test comment";

        Issue issue = new Issue();
        User user = new User();
        Comment comment = new Comment();
        comment.setContent(content);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.createComment(issueId, userId, content);

        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testCreateComment_Failure_IssueNotFound() {
        Long issueId = 1L;
        Long userId = 2L;
        String content = "Test comment";

        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            commentService.createComment(issueId, userId, content);
        });

        assertEquals("Issue not found with issue id 1", exception.getMessage());
    }

    @Test
    void testCreateComment_Failure_UserNotFound() {
        Long issueId = 1L;
        Long userId = 2L;
        String content = "Test comment";

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(new Issue()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            commentService.createComment(issueId, userId, content);
        });

        assertEquals("User not found with userid 2", exception.getMessage());
    }

    @Test
    void testDeleteComment_Success() throws Exception {
        Long commentId = 1L;
        Long userId = 2L;

        Comment comment = new Comment();
        User user = new User();
        user.setId(userId);
        comment.setUser(user);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        commentService.deleteComment(commentId, userId);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testDeleteComment_Failure_CommentNotFound() {
        Long commentId = 1L;
        Long userId = 2L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            commentService.deleteComment(commentId, userId);
        });

        assertEquals("Comment not found with issue id 1", exception.getMessage());
    }

    @Test
    void testDeleteComment_Failure_UserNotAuthorized() {
        Long commentId = 1L;
        Long userId = 2L;

        Comment comment = new Comment();
        User user = new User();
        user.setId(3L); // Different user ID

        comment.setUser(user);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(Exception.class, () -> {
            commentService.deleteComment(commentId, userId);
        });

        assertEquals("Comment not deleted from issue id 1", exception.getMessage());
    }

    @Test
    void testFindCommentByIssueId_Success() throws Exception {
        Long issueId = 1L;
        List<Comment> comments = List.of(new Comment(), new Comment());

        when(commentRepository.findCommentByIssueId(issueId)).thenReturn(comments);

        List<Comment> result = commentService.findCommentByIssueId(issueId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findCommentByIssueId(issueId);
    }
}

