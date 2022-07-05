package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDTO implements Comparable<LeaderboardDTO> {

    private String nameAndSurname;
    private float points;


    @Override
    public int compareTo(LeaderboardDTO o) {
        if (this.points < o.getPoints()) {
            return -1;
        }
        if (this.points == o.getPoints()) {
            return 0;
        }
        if (this.points > o.getPoints()) {
            return 1;
        }
        return 0;

    }
}
