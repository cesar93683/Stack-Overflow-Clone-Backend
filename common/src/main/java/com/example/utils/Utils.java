package com.example.utils;

import static com.example.utils.Constants.DOWN_VOTE;
import static com.example.utils.Constants.NEUTRAL;

public class Utils {

    public static int getVoteDiff(String oldVoteType, String newVoteType) {
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
