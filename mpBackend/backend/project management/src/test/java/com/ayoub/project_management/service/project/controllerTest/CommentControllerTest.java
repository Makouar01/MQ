package com.ayoub.project_management.service.project.controllerTest;

import com.ayoub.project_management.controller.CommentController;
import com.ayoub.project_management.model.Comment;
import com.ayoub.project_management.model.User;
import com.ayoub.project_management.request.createCommentRequest;
import com.ayoub.project_management.responce.MessageResponse;
import com.ayoub.project_management.service.comment.CommentService;
import com.ayoub.project_management.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;

    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentController = new CommentController();
        commentController.commentService = commentService;
        commentController.userService = userService;
    }

    @Test
    void testCreateComment() throws Exception {
        String jwt = "Bearer sampleJwtToken";
        User mockUser = new User();
        mockUser.setId(1L);

        createCommentRequest mockRequest = new createCommentRequest();
        mockRequest.setIssueId(100L);
        mockRequest.setContent("This is a test comment");

        Comment mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setContent("This is a test comment");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(commentService.createComment(100L, 1L, "This is a test comment")).thenReturn(mockComment);

        ResponseEntity<Comment> response = commentController.createComment(mockRequest, jwt);

        assertEquals(201, response.getStatusCodeValue(), "Expected HTTP Status 201 CREATED");
        assertEquals(mockComment, response.getBody(), "Expected the created comment to match");
    }

    @Test
    void testDeleteComment() throws Exception {
        String jwt = "Bearer sampleJwtToken";
        User mockUser = new User();
        mockUser.setId(1L);

        Long commentId = 1L;

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        ResponseEntity<MessageResponse> response = commentController.deleteComment(commentId, jwt);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP Status 200 OK");
        assertEquals("Comment deleted successfully", response.getBody().getMessage(), "Expected success message");

        verify(commentService, times(1)).deleteComment(commentId, mockUser.getId());
    }

    @Test
    void testGetCommentsByIssueId() throws Exception {
        Long issueId = 100L;
        List<Comment> mockComments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");
        mockComments.add(comment1);
        mockComments.add(comment2);

        when(commentService.findCommentByIssueId(issueId)).thenReturn(mockComments);

        ResponseEntity<List<Comment>> response = commentController.getCommentsByIssueId(issueId);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP Status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(2, response.getBody().size(), "Expected two comments in the response");
        assertEquals(mockComments, response.getBody(), "Expected the comments to match");
    }
}