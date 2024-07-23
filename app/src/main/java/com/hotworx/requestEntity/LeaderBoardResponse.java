package com.hotworx.requestEntity  ;



import java.io.Serializable;
import java.util.ArrayList;

public class LeaderBoardResponse extends BaseModel<LeaderBoardResponse> implements Serializable {

    private ArrayList<LeaderBoardPOJO> leaderboard;

    public ArrayList<LeaderBoardPOJO> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(ArrayList<LeaderBoardPOJO> leaderboard) {
        this.leaderboard = leaderboard;
    }
}
