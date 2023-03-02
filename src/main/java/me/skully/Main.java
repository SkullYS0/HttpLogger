package me.skully;

import com.sun.net.httpserver.HttpServer;
import me.skully.utils.Bot;
import me.skully.utils.MySQL;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static ArrayList<String> banned = new ArrayList<>();

    private static Main instance;

    public static String token = "";
    public static String botUsername = "";
    public static int ownerID = 1;
    public static String dbHost = "";
    public static String dbName = "";
    public static String dbUser = "";
    public static String dbPort = "";
    public static String dbPassword = "";
    public static int serverPort = 80;


    public static void main(String[] args) throws Exception {
        init();
        MySQL.instance.getConnection(Main.dbHost,Main.dbPort,Main.dbUser,Main.dbPassword,Main.dbName);

            System.out.println("\n" +
                    "   ╭╮    ╭╮╭╮\n" +
                    "   ┃┃    ┃┃┃┃\n" +
                    "╭━━┫┃╭┳╮╭┫┃┃┃╭╮ ╭┳━━┳━━┳━━┳━━┳━━╮\n" +
                    "┃━━┫╰╯┫┃┃┃┃┃┃┃┃ ┃┃━━┫╭╮┃╭╮┃╭━┫┃━┫\n" +
                    "┣━━┃╭╮┫╰╯┃╰┫╰┫╰━╯┣━━┃╰╯┃╭╮┃╰━┫┃━┫\n" +
                    "╰━━┻╯╰┻━━┻━┻━┻━╮╭┻━━┫╭━┻╯╰┻━━┻━━╯\n" +
                    "             ╭━╯┃   ┃┃\n" +
                    "             ╰━━╯   ╰╯\n" +
                    "");


        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);

            api.registerBot(new Bot());
        } catch (Exception e) {
            System.out.println("БОТ НЕ СМОГ ЗАПУСТИТЬСЯ");
        }
        HttpServer server = null;
        if(args.length == 0) {
            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        }else{
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
        }

        // Обработчик запросов
        server.createContext("/", new HandlerHTTP());

        // Запуск сервера
        server.start();
    }

    public static void init(){
        Yaml yaml = new Yaml();
        try {
            FileInputStream inputStream = new FileInputStream("config.yml");
            Map<String, Object> data = yaml.load(inputStream);
            token = (String) data.get("token");
            ownerID = (int) data.get("ownerID");
            botUsername = (String) data.get("botUsername");
            dbHost = (String) data.get("dbHost");
            dbName = (String) data.get("dbName");
            dbUser = (String) data.get("dbUser");
            dbPassword = (String) data.get("dbPassword");
            dbPort = (String) data.get("dbPort");
            serverPort = (int) data.get("serverPort");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Main getInstance(){
        return instance;
    }

}