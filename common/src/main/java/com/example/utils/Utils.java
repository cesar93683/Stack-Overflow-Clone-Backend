package com.example.utils;

import static com.example.utils.Constants.DOWN_VOTE;
import static com.example.utils.Constants.NEUTRAL;

public class Utils {

    public static int getVoteDiff(int oldVoteType, int newVoteType) {
        if (oldVoteType == (newVoteType)) {
            return 0;
        }
        if (newVoteType == (DOWN_VOTE)) {
            if (oldVoteType == (NEUTRAL)) {
                return -1;
            } else { // UP_VOTE
                return -2;
            }
        } else if (newVoteType == (NEUTRAL)) {
            if (oldVoteType == (DOWN_VOTE)) {
                return 1;
            } else { // UP_VOTE
                return -1;
            }
        } else { // UP_VOTE
            if (oldVoteType == (DOWN_VOTE)) {
                return 2;
            } else { // NEUTRAL
                return 1;
            }
        }
    }
}
