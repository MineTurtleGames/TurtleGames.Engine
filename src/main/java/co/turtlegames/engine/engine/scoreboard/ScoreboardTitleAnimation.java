package co.turtlegames.engine.engine.scoreboard;

import org.bukkit.ChatColor;

public class ScoreboardTitleAnimation {

    private static final int HOLD_TIME = 60;
    private static final int TICK_COUNT = 3;

    private enum MoveState {
        FORWARD,
        BACKWARD,
        HOLD;
    }

    private int _tickTillNextTick = TICK_COUNT;

    private int _clock = 0;
    private int _currentString = 0;

    private MoveState _moveState = MoveState.FORWARD;

    private String[] _strings = new String[] { ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle"
                                                    + ChatColor.GREEN.toString() + ChatColor.BOLD + "Games",
                                                        ChatColor.GREEN.toString() + ChatColor.BOLD + "turtlegames.co" };

    public String getValue() {

        if(_moveState == MoveState.FORWARD || _moveState == MoveState.BACKWARD)
            return ChatColor.getLastColors(_strings[_currentString])
                    + ChatColor.stripColor(_strings[_currentString]).substring(0, _clock + 1);
        else
            return _strings[_currentString];

    }

    public void tick() {

        _tickTillNextTick--;

        if(_tickTillNextTick > 0)
            return;

        _tickTillNextTick =TICK_COUNT;

        if(_moveState == MoveState.FORWARD || _moveState == MoveState.BACKWARD) {

            if(_moveState == MoveState.BACKWARD) {

                _clock--;

                if(_clock < 0) {

                    _moveState = MoveState.FORWARD;
                    _currentString++;

                    _clock = 0;

                    if(_currentString >= _strings.length)
                        _currentString = 0;

                    return;
                }

            }
            if(_moveState == MoveState.FORWARD) {

                _clock++;

                if((_clock+1) >= _strings[_currentString].length()) {
                    _moveState = MoveState.HOLD;
                    _clock = HOLD_TIME;
                    return;
                }

            }

            return;

        }

        _clock--;

        if(_clock <= 0) {

            _clock = _strings[_currentString].length() - 1;
            _moveState = MoveState.BACKWARD;

        }

    }


}
