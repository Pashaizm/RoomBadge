package com.eu.habbo.RoomBadgeCommand.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.HabboBadge;
import com.eu.habbo.habbohotel.users.inventory.BadgesComponent;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.users.AddUserBadgeComposer;
import gnu.trove.map.hash.THashMap;

public class RoomBadgeCommand extends Command {

    public RoomBadgeCommand() {
        super("cmd_roombadge", Emulator.getTexts().getValue("commands.keys.cmd_roombadge").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] strings) throws Exception {
        final Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

        String badgeCode = strings[1];
        for (Habbo habbo : gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbos()) {

            if (!habbo.getInventory().getBadgesComponent().hasBadge(badgeCode)) {
                HabboBadge b = BadgesComponent.createBadge(badgeCode, habbo);
                habbo.getClient().sendResponse(new AddUserBadgeComposer(b));
                habbo.whisper(Emulator.getTexts().getValue("Yeni bir rozet kazandin!"));
                } else {
                habbo.whisper(Emulator.getTexts().getValue("Yeni bir rozet kazanildi, fakat sende zaten mevcut!"));
                 }
            }
        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("cmd_roombadge.sucess"));
        return true;
    }
}