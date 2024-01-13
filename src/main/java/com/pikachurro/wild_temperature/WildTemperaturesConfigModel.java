package com.pikachurro.wild_temperature;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = WildTemperature.MOD_ID)
@Config(name = "wild_temperature_config", wrapperName = "WildTemperaturesConfig", defaultHook = false, saveOnModification = true)
public class WildTemperaturesConfigModel {
    // server and client
    // global config
    public boolean doDebugLogs = false;

    // temperature manager
    public float transitionTimeTicks = 300; // 15 seconds

    // damage manager config
    public float EXTREME_HEAT_THRESHOLD = 1.98f;
    public float EXTREME_COLD_THRESHOLD = 0.07f;
    public float EXTREME_HEAT_DAMAGE = 1;
    public float EXTREME_COLD_DAMAGE = 1;
    @SectionHeader("clientSide")
    // client config
    public boolean leftHandHud = true;
    public boolean nonDeadlyOverlays = true;
    public boolean deadlyOverlays = true;
    public boolean temperatureHUD = true;
}
