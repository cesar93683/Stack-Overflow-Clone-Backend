package com.example.service;

import com.example.dto.PostDTO;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.Vote;
import com.example.exceptions.PostException;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.utils.Constants.*;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<PostDTO> getPosts(int page, boolean sortedByVotes, int userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<PostDTO> posts = postRepository.findAllByPostResponseId(-1, pageable)
                .stream()
                .map((Post post) -> new PostDTO(post, false))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updatePostsWithCurrVote(posts, userId);
        }
        return posts;
    }

    @Override
    public List<PostDTO> getPostsByUserId(int userId, int page, boolean sortedByVotes, int userIdIfExists) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<PostDTO> posts = postRepository.findAllByUserIdAndPostResponseId(userId, -1, pageable)
                .stream()
                .map((Post post) -> new PostDTO(post, false))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updatePostsWithCurrVote(posts, userId);
        }
        return posts;
    }

    @Override
    public PostDTO getPost(int postId, int userId) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        if (post.getPostResponseId() != -1) {
            throw new PostException("Post is a post response");
        }
        PostDTO postDTO = new PostDTO(post, true);
        if (userId != NO_USER_ID) {
            voteRepository.findByUserIdAndPostId(userId, postId)
                    .ifPresent(value -> postDTO.setCurrVote(value.getVoteType()));
        }
        return postDTO;
    }

    @Override
    public List<PostDTO> getPostResponses(int postId, int page, boolean sortedByVotes, int userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<PostDTO> posts = postRepository.findAllByPostResponseId(postId, pageable)
                .stream()
                .map((Post post) -> new PostDTO(post, true))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updatePostsWithCurrVote(posts, userId);
        }
        return posts;

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

    @Override
    public int createComment(String content, int postId, int userId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        Comment comment = new Comment(content, user, post);
        return commentRepository.save(comment).getId();
    }

    @Override
    public void updateComment(int commentId, String content, int userId) throws PostException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException("Comment not found with id: " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create comment with id: " + commentId);
        }
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(int commentId, int userId) throws PostException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException("Comment not found with id: " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new PostException("User with id: " + userId + " did not create comment with id: " + commentId);
        }
        commentRepository.delete(comment);
    }

    @Override
    public void voteComment(int userId, int commentId, String voteType) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PostException("User not found with id: " + userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException("Comment not found with id: " + commentId));
        Vote vote = voteRepository.findByUserIdAndCommentId(user.getId(), commentId)
                .orElse(new Vote(userId, -1, commentId, NEUTRAL));

        comment.setVotes(comment.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        commentRepository.save(comment);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }

    private void updatePostsWithCurrVote(List<PostDTO> posts, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndPostIdIn(userId, getPostIds(posts));
        for (Vote vote : votes) {
            posts.stream()
                    .filter(post -> post.getId() == vote.getPostId())
                    .findFirst()
                    .ifPresent(post -> post.setCurrVote(vote.getVoteType()));
        }
    }

    private List<Integer> getPostIds(List<PostDTO> posts) {
        return posts
                .stream()
                .map(PostDTO::getId)
                .collect(Collectors.toList());
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
