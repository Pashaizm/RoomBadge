package com.eu.habbo.RoomBadgeCommand;

import com.eu.habbo.Emulator;
import com.eu.habbo.RoomBadgeCommand.commands.RoomBadgeCommand;
import com.eu.habbo.habbohotel.commands.CommandHandler;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.HabboPlugin;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends HabboPlugin implements EventListener {
    public static Main INSTANCE = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Emulator.getPluginManager().registerEvents(this, this);

        if (Emulator.isReady)
            this.checkDatabase();

        Emulator.getLogging().logStart("[Online] Started Roombadge Plugin!");
    }

    @Override
    public void onDisable() {
        Emulator.getLogging().logShutdownLine("[Online] Stopped Roombadge Plugin!");
    }

    @EventHandler
    public static void onEmulatorLoaded(EmulatorLoadedEvent event) {
        INSTANCE.checkDatabase();
    }

    @Override
    public boolean hasPermission(Habbo habbo, String s) {
        return false;
    }

    public void checkDatabase() {
        boolean reloadPermissions = false;
        try(Connection connection = Emulator.getDatabase().getDataSource().getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE  `emulator_texts` CHANGE  `value`  `value` VARCHAR( 4096 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL");
        } catch (SQLException sQLException) {}
        reloadPermissions = this.registerPermission("cmd_roombadge", "'0', '1', '2'", "1", reloadPermissions);
        if (reloadPermissions)
            Emulator.getGameEnvironment().getPermissionsManager().reload();
        CommandHandler.addCommand(new RoomBadgeCommand());
    }

    private boolean registerPermission(String name, String options, String defaultValue, boolean defaultReturn) {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement("ALTER TABLE  `permissions` ADD  `" + name +"` ENUM(  " + options + " ) NOT NULL DEFAULT  '" + defaultValue + "'"))
            {
                statement.execute();
                return true;
            }
        }
        catch (SQLException e)
        {}
        return defaultReturn;
    }

    public static void main(String[] args) {
        System.out.println("Don't run this seperately");
    }
}