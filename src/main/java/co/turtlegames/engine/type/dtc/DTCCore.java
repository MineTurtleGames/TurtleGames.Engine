package co.turtlegames.engine.type.dtc;

import co.turtlegames.core.util.TRegion;
import co.turtlegames.engine.engine.game.GameTeam;
import co.turtlegames.engine.engine.game.player.GamePlayer;

public class DTCCore extends DTCGenerator {

    public DTCCore(DefendTheCoreGame instance, TRegion region, GameTeam team) {
        super(instance, region, team);
    }

    @Override
    public void attemptDamage() {

        if(this.getDtcInstance().isShielded(this.getTeam()))
            return;

        super.attemptDamage();

    }

    @Override
    public void destroy() {

        super.destroy();

        DefendTheCoreGame gameInstance = this.getDtcInstance();
        GameTeam gameTeam = this.getTeam();

        GameTeam winningTeam;
        if(gameTeam == gameInstance.RED_TEAM)
            winningTeam = gameInstance.BLUE_TEAM;
        else
            winningTeam = gameInstance.RED_TEAM;

        gameInstance.endGameWithTeam(winningTeam);

    }

}
