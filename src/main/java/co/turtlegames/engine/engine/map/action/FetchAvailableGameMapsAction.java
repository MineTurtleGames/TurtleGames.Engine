package co.turtlegames.engine.engine.map.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.engine.engine.game.GameType;
import co.turtlegames.engine.engine.map.MapToken;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FetchAvailableGameMapsAction implements IDatabaseAction<List<MapToken>> {

    private GameType _gameType;

    public FetchAvailableGameMapsAction(GameType gameType) {
        _gameType = gameType;
    }


    @Override
    public List<MapToken> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("SELECT * FROM `game_map` WHERE `game`=?");

        statement.setString(1, _gameType.toString());

        ResultSet rs = statement.executeQuery();
        List<MapToken> mapTokens = new ArrayList<>();

        while (rs.next()) {
            mapTokens.add(new MapToken(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                            .split("\n"),
                    GameType.valueOf(rs.getString("game")),
                    rs.getString("location")));
        }

        return mapTokens;

    }

}
