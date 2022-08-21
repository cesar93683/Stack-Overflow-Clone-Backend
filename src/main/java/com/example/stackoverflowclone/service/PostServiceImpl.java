package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.entity.Vote;
import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.repository.PostRepository;
import com.example.stackoverflowclone.repository.VoteRepository;
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
    private VoteRepository voteRepository;

    @Override
    public List<PostDTO> getPosts(int page) {
        Pageable sortedById = PageRequest.of(page, 10, Sort.by("id").descending());
        return postRepository.findAllByPostResponseId(-1, sortedById)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostsByUserId(int userId, int page) {
        Pageable sortedById = PageRequest.of(page, 10, Sort.by("id").descending());
        return postRepository.findAllByUserIdAndPostResponseId(userId, -1, sortedById)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPost(int id) throws PostException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found with id: " + id));
        if (post.getPostResponseId() != -1) {
            throw new PostException("Post is a post response");
        }
        return new PostDTO(post);
    }

    @Override
    public List<PostDTO> getPostResponses(int postId, int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        return postRepository.findAllByPostResponseId(postId, pageable)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public int createPost(String title, String content, int postResponseId, int userId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = new Post(title, content, user, postResponseId);
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
    public void votePost(int userId, int postId, String voteType) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        Vote vote = voteRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElse(new Vote(userId, postId, -1, NEUTRAL));

        post.setVotes(post.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        postRepository.save(post);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }

    private int getVoteDiff(String oldVoteType, String newVoteType) {
        if (oldVoteType.equals(newVoteType)) {
            return 0;
        }
        if (newVoteType.equals(DOWN_VOTE)) {
            if (oldVoteType.equals(NEUTRAL)) {
                return -1;
            } else { // UP_VOTE
                return -2;
            }
        } else if (newVoteType.equals(NEUTRAL)) {
            if (oldVoteType.equals(DOWN_VOTE)) {
                return 1;
            } else { // UP_VOTE
                return -1;
            }
        } else { // UP_VOTE
            if (oldVoteType.equals(DOWN_VOTE)) {
                return 2;
            } else { // NEUTRAL
                return 1;
            }
        }
    }
}
