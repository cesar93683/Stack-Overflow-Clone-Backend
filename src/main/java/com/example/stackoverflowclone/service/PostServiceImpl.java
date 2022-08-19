package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.entity.*;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.stackoverflowclone.utils.Constants.DOWN_VOTE;
import static com.example.stackoverflowclone.utils.Constants.NEUTRAL;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostVoteRepository postVoteRepository;
    @Autowired
    private PostResponseRepository postResponseRepository;
    @Autowired
    private PostResponseVoteRepository postResponseVoteRepository;

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
    public int createPost(String title, String content, int userId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = new Post(title, content, user);
        return postRepository.save(post).getId();
    }

    @Override
    public void updatePost(int postId, String content, int userId) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        if (post.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create post with id: " + postId);
        }
        post.setContent(content);
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

    @Override
    public void votePost(int userId, int postId, String vote) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        PostVote postVote = postVoteRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElse(new PostVote(userId, postId, NEUTRAL));

        post.setVotes(post.getVotes() + getVoteDiff(postVote.getVote(), vote));
        postRepository.save(post);
        postVote.setVote(vote);
        postVoteRepository.save(postVote);
    }

    private int getVoteDiff(String oldVote, String newVote) {
        if (oldVote.equals(newVote)) {
            return 0;
        }
        if (newVote.equals(DOWN_VOTE)) {
            if (oldVote.equals(NEUTRAL)) {
                return -1;
            } else { // UP_VOTE
                return -2;
            }
        } else if (newVote.equals(NEUTRAL)) {
            if (oldVote.equals(DOWN_VOTE)) {
                return 1;
            } else { // UP_VOTE
                return -1;
            }
        } else { // UP_VOTE
            if (oldVote.equals(DOWN_VOTE)) {
                return 2;
            } else { // NEUTRAL
                return 1;
            }
        }
    }

    @Override
    public void createPostResponse(String content, int postId, int userId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        PostResponse postResponse = new PostResponse(content, user, post);
        postResponseRepository.save(postResponse);
    }

    @Override
    public void updatePostResponse(int postResponseId, String content, int userId) throws PostException {
        PostResponse postResponse = postResponseRepository.findById(postResponseId)
                .orElseThrow(() -> new PostException("PostResponse not found with id: " + postResponseId));
        if (postResponse.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create postResponse with id: " + postResponseId);
        }
        postResponse.setContent(content);
        postResponseRepository.save(postResponse);
    }

    @Override
    public void deletePostResponse(int postResponseId, int userId) throws PostException {
        PostResponse postResponse = postResponseRepository.findById(postResponseId)
                .orElseThrow(() -> new PostException("PostResponse not found with id: " + postResponseId));
        if (postResponse.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create post with id: " + postResponseId);
        }
        postResponseRepository.delete(postResponse);
    }

    @Override
    public void votePostResponse(int userId, int postResponseId, String vote) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        PostResponse postResponse = postResponseRepository.findById(postResponseId)
                .orElseThrow(() -> new PostException("PostResponse not found with id: " + postResponseId));
        PostResponseVote postResponseVote = postResponseVoteRepository.findByUserIdAndPostResponseId(
                user.getId(), postResponseId)
                .orElse(new PostResponseVote(userId, postResponseId, NEUTRAL));

        postResponse.setVotes(postResponse.getVotes() + getVoteDiff(postResponseVote.getVote(), vote));
        postResponseRepository.save(postResponse);
        postResponseVote.setVote(vote);
        postResponseVoteRepository.save(postResponseVote);
    }
}
