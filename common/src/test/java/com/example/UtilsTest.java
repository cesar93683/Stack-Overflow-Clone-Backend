package com.example;

import org.junit.jupiter.api.Test;

import static com.example.utils.Utils.getVoteDiff;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    private final int UP_VOTE = 1;
    private final int DOWN_VOTE = -1;
    private final int NEUTRAL = 0;

    @Test
    void testWithUpVoteUpVote() {
        int voteDiff = getVoteDiff(UP_VOTE, UP_VOTE);
        assertEquals(0, voteDiff);
    }

    @Test
    void testWithUpVoteNeutral() {
        int voteDiff = getVoteDiff(UP_VOTE, NEUTRAL);
        assertEquals(-1, voteDiff);
    }

    @Test
    void testWithUpVoteDownVote() {
        int voteDiff = getVoteDiff(UP_VOTE, DOWN_VOTE);
        assertEquals(-2, voteDiff);
    }

    @Test
    void testWithNeutralUpVote() {
        int voteDiff = getVoteDiff(NEUTRAL, UP_VOTE);
        assertEquals(1, voteDiff);
    }

    @Test
    void testWithNeutralNeutral() {
        int voteDiff = getVoteDiff(NEUTRAL, NEUTRAL);
        assertEquals(0, voteDiff);
    }

    @Test
    void testWithNeutralDownVote() {
        int voteDiff = getVoteDiff(NEUTRAL, DOWN_VOTE);
        assertEquals(-1, voteDiff);
    }

    @Test
    void testWithDownVoteUpVote() {
        int voteDiff = getVoteDiff(DOWN_VOTE, UP_VOTE);
        assertEquals(2, voteDiff);
    }

    @Test
    void testWithDownVoteNeutral() {
        int voteDiff = getVoteDiff(DOWN_VOTE, NEUTRAL);
        assertEquals(1, voteDiff);
    }

    @Test
    void testWithDownVoteDownVote() {
        int voteDiff = getVoteDiff(DOWN_VOTE, DOWN_VOTE);
        assertEquals(0, voteDiff);
    }

}
