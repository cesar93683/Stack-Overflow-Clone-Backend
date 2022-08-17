package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.entity.PostResponse;
import com.example.stackoverflowclone.entity.PostVote;
import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.repository.PostRepository;
import com.example.stackoverflowclone.repository.PostResponseRepository;
import com.example.stackoverflowclone.repository.PostVoteRepository;
import com.example.stackoverflowclone.repository.UserRepository;
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
}
