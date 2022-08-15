package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.payload.post.UpdatePostRequest;
import com.example.stackoverflowclone.repository.PostRepository;
import com.example.stackoverflowclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PostDTO> getPosts(int page) {
        Pageable sortedById = PageRequest.of(page, 10, Sort.by("id").descending());
        return postRepository.findAll(sortedById)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostsByUserId(int userId, int page) {
        Pageable sortedById = PageRequest.of(page, 10, Sort.by("id").descending());
        return postRepository.findAllByUserId(userId, sortedById)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPost(int id) throws PostException {
        return new PostDTO(postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found with id: " + id)));
    }

    @Override
    public int createPost(CreatePostRequest createPostRequest, int userId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = new Post(createPostRequest, user);
        return postRepository.save(post).getId();
    }

    @Override
    public void updatePost(int postId, UpdatePostRequest updatePostRequest, int userId) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        if (post.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create post with id: " + postId);
        }
        post.setContent(updatePostRequest.getContent());
        postRepository.save(post);
    }

    @Override
    public void deletePost(int postId, int userId) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        if (post.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create post with id: " + postId);
        }
        postRepository.delete(post);
    }
}
