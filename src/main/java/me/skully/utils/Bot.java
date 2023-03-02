package me.skully.utils;

import me.skully.Main;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.toggle.CustomToggle;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends AbilityBot {

    public static final String BOT_TOKEN = Main.token;
    public static final String BOT_USERNAME = Main.botUsername;
    public Bot() {
        super(BOT_TOKEN, BOT_USERNAME, toggle);

    }

    @Override
    public void onUpdateReceived(Update update) {
        String[] args = update.getMessage().getText().split(" ");
        MessageContext ctx = MessageContext.newContext(update, update.getMessage().getFrom(), update.getMessage().getChatId(), this, args);
        //silent.send(update.getMessage().getText(), update.getMessage().getChatId()); // REPLY MODE
        if(ctx.update().getMessage().getText().startsWith("/start")) {
            startcmd(ctx);
        }
        if (ctx.update().getMessage().getText().startsWith("/ban")) {
            banCmd(ctx);
        }else if(ctx.update().getMessage().getText().startsWith("/unban")) {
            unbanCmd(ctx);
        }
    }
    public void startcmd(MessageContext ctx) {
        if (!MySQL.instance.getter("SELECT * FROM users WHERE chatid = \"" + ctx.chatId() + "\";")) {
            MySQL.instance.execute("INSERT INTO users(nick,role,lvl,chatid) VALUES(\"" + ctx.user().getUserName() + "\",\"Пользователь\",\"1\",\"" + ctx.chatId() + "\");");
            System.out.println("У нас новый пользователь! " + ctx.user().getUserName() + " / " + ctx.chatId());
            silent.send("\uD83E\uDD73 У нас новый пользователь! " + ctx.user().getUserName() + " / " + ctx.chatId(), Main.ownerID);
            silent.send("\uD83D\uDE07 Регистрация прошла успешно, посмотреть список команд: /help", ctx.chatId());
        } else {
            silent.send("\uD83E\uDD14 Вы уже зарегистрированы, нужна помощь - вводите /help", ctx.chatId());
        }
    }

    public void banCmd(MessageContext ctx){
        if (MySQL.instance.getInt("lvl","SELECT * FROM USERS WHERE chatid = \""+ctx.chatId()+"\"") >= 2) {
            String[] splitter = ctx.update().getMessage().getText().split(" ");
            MySQL.instance.execute("INSERT INTO bans(who,ban) VALUES(\"" + ctx.chatId() + "\",\"" + splitter[1] + "\");");
            silent.send("\uD83D\uDE07 Успешно забанил " + splitter[1], ctx.chatId());
        }else{
            silent.send("\uD83E\uDD14 У вас нет прав",ctx.chatId());
        }
    }

    public void unbanCmd(MessageContext ctx) {
        if (MySQL.instance.getInt("lvl", "SELECT * FROM USERS WHERE chatid = \"" + ctx.chatId() + "\"") >= 2) {
            String[] splitter = ctx.update().getMessage().getText().split(" ");
            MySQL.instance.execute("DELETE FROM `bans` WHERE ban = \"" + splitter[1] + "\";");
            silent.send("\uD83E\uDD73 " + splitter[1] + " был разбанен", ctx.chatId());
        }else{
            silent.send("\uD83E\uDD14 У вас нет прав",ctx.chatId());
        }
    }

    @Override
    public long creatorId() {
        return Main.ownerID;
    }

    private static final CustomToggle toggle = new CustomToggle()
            .turnOff("ban")
            .turnOff("demote")
            .turnOff("stats")
            .turnOff("unban")
            .turnOff("upgrade")
            .turnOff("promote")
            .turnOff("commands")
            .turnOff("backup")
            .turnOff("claim")
            .turnOff("recover")
            .turnOff("report");

}
