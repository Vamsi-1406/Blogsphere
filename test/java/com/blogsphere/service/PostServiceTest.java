package com.blogsphere.service;

import com.blogsphere.model.Post;
import com.blogsphere.model.Tag;
import com.blogsphere.model.User;
import com.blogsphere.repository.PostRepository;
import com.blogsphere.repository.TagRepository;
import com.blogsphere.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;
    private Tag testTag;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testTag = new Tag();
        testTag.setId(1L);
        testTag.setName("test");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setTags(new HashSet<>(Collections.singletonList(testTag)));
    }

    @Test
    void createPost_Success() {
        Set<String> tagNames = Collections.singleton("test");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(testTag));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        Post createdPost = postService.createPost(1L, testPost, tagNames);

        assertNotNull(createdPost);
        assertEquals(testPost.getTitle(), createdPost.getTitle());
        assertEquals(testPost.getContent(), createdPost.getContent());
        assertEquals(testUser, createdPost.getAuthor());
        assertTrue(createdPost.getTags().contains(testTag));
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void updatePost_Success() {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated content");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        Post result = postService.updatePost(1L, updatedPost);

        assertNotNull(result);
        assertEquals(updatedPost.getTitle(), result.getTitle());
        assertEquals(updatedPost.getContent(), result.getContent());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void deletePost_Success() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));

        postService.deletePost(1L);

        verify(postRepository).delete(testPost);
    }

    @Test
    void likePost_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        postService.likePost(1L, 1L);

        assertTrue(testPost.getLikedBy().contains(testUser));
        verify(postRepository).save(testPost);
    }

    @Test
    void unlikePost_Success() {
        testPost.getLikedBy().add(testUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        postService.unlikePost(1L, 1L);

        assertFalse(testPost.getLikedBy().contains(testUser));
        verify(postRepository).save(testPost);
    }

    @Test
    void getPostsByAuthor_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(testPost));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(postRepository.findByAuthor(any(User.class), any(Pageable.class))).thenReturn(postPage);

        Page<Post> result = postService.getPostsByAuthor(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testPost, result.getContent().get(0));
    }

    @Test
    void getPostsByTag_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(testPost));
        when(postRepository.findByTagsName(anyString(), any(Pageable.class))).thenReturn(postPage);

        Page<Post> result = postService.getPostsByTag("test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testPost, result.getContent().get(0));
    }
} 