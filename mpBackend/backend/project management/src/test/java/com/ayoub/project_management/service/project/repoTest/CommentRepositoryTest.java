package com.ayoub.project_management.service.project.repoTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ayoub.project_management.Repository.CommentRepository;
import com.ayoub.project_management.model.Comment;
import com.ayoub.project_management.model.Issue;
import com.ayoub.project_management.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

 class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;

    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Create some dummy data for testing
        comment1 = new Comment(1L, "This is a comment.", LocalDateTime.now(), new User(), new Issue());
        comment2 = new Comment(2L, "This is another comment.", LocalDateTime.now().plusMinutes(10), new User(), new Issue());
    }

    @Test
     void testFindCommentByIssueId() {
        // Arrange
        Long issueId = 1L;
        when(commentRepository.findCommentByIssueId(issueId)).thenReturn(Arrays.asList(comment1, comment2));

        // Act
        List<Comment> comments = commentRepository.findCommentByIssueId(issueId);

        // Assert
        assertNotNull(comments);
        assertEquals(2, comments.size()); // Ensure two comments are returned
        assertEquals("This is a comment.", comments.get(0).getContent()); // Ensure the content matches
        assertEquals("This is another comment.", comments.get(1).getContent());
    }

    @Test
     void testFindCommentByIssueId_NoCommentsFound() {
        // Arrange
        Long issueId = 2L;
        when(commentRepository.findCommentByIssueId(issueId)).thenReturn(Arrays.asList());

        // Act
        List<Comment> comments = commentRepository.findCommentByIssueId(issueId);

        // Assert
        assertNotNull(comments);
        assertEquals(0, comments.size()); // Ensure no comments are returned for this issue ID
    }

    @Test
     void testFindCommentByIssueId_SingleComment() {
        // Arrange
        Long issueId = 1L;
        when(commentRepository.findCommentByIssueId(issueId)).thenReturn(Arrays.asList(comment1));

        // Act
        List<Comment> comments = commentRepository.findCommentByIssueId(issueId);

        // Assert
        assertNotNull(comments);
        assertEquals(1, comments.size()); // Ensure only one comment is returned
        assertEquals("This is a comment.", comments.get(0).getContent()); // Ensure the content matches
    }
}
