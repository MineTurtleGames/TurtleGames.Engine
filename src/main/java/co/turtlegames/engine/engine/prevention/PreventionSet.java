package co.turtlegames.engine.engine.prevention;

public class PreventionSet {

    private boolean _blockPlaceAllowed = false;
    private boolean _blockBreakAllowed = false;
    private boolean _damageEnabled = false;
    private boolean _pvpEnabled = false;
    private boolean _pveEnabled = false;
    private boolean _evpEnabled = false;

    public boolean isBlockPlaceAllowed() {
        return _blockPlaceAllowed;
    }

    public void setBlockPlaceAllowed(boolean blockPlaceAllowed) {
        _blockPlaceAllowed = blockPlaceAllowed;
    }

    public boolean isBlockBreakAllowed() {
        return _blockBreakAllowed;
    }

    public void setBlockBreakAllowed(boolean blockBreakAllowed) {
        _blockBreakAllowed = blockBreakAllowed;
    }

    public boolean isDamageEnabled() {
        return _damageEnabled;
    }

    public void setDamageEnabled(boolean damageEnabled) {
        _damageEnabled = damageEnabled;
    }

    public boolean isPvpEnabled() {
        return _pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        _pvpEnabled = pvpEnabled;
    }

    public boolean isPveEnabled() {
        return _pveEnabled;
    }

    public void setPveEnabled(boolean pveEnabled) {
        _pveEnabled = pveEnabled;
    }

    public boolean isEvpEnabled() {
        return _evpEnabled;
    }

    public void setEvpEnabled(boolean evpEnabled) {
        _evpEnabled = evpEnabled;
    }
}
