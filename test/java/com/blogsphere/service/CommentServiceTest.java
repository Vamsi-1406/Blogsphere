package com.blogsphere.service;

import com.blogsphere.model.Comment;
import com.blogsphere.model.Post;
import com.blogsphere.model.User;
import com.blogsphere.repository.CommentRepository;
import com.blogsphere.repository.PostRepository;
import com.blogsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private CommentService commentService;

    private User testUser;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, postRepository, userRepository);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setAuthor(testUser);

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test Comment");
        testComment.setAuthor(testUser);
        testComment.setPost(testPost);
    }

    @Test
    void createComment_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.createComment(1L, 1L, testComment);

        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        assertEquals(testUser, result.getAuthor());
        assertEquals(testPost, result.getPost());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_Success() {
        Comment updatedComment = new Comment();
        updatedComment.setContent("Updated comment");

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.updateComment(1L, updatedComment);

        assertNotNull(result);
        assertEquals(updatedComment.getContent(), result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(testComment);
    }

    @Test
    void getCommentsByPost_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(testComment));
        
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.findByPostOrderByCreatedAtDesc(testPost, pageable)).thenReturn(commentPage);

        Page<Comment> result = commentService.getCommentsByPost(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testComment, result.getContent().get(0));

        verify(postRepository).findById(1L);
        verify(commentRepository).findByPostOrderByCreatedAtDesc(testPost, pageable);
    }

    @Test
    void getCommentsByPost_PostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentService.getCommentsByPost(1L, PageRequest.of(0, 10));
        });

        verify(postRepository).findById(1L);
        verify(commentRepository, never()).findByPostOrderByCreatedAtDesc(any(), any());
    }
} 