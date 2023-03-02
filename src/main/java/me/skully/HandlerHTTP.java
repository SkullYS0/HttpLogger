package me.skully;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.skully.utils.Bot;
import me.skully.utils.MySQL;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class HandlerHTTP implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("New connection from" + exchange.getRemoteAddress().getAddress().getCanonicalHostName() + " with method " + exchange.getRequestMethod());
        sendMessage(Main.ownerID,"New connection from " + exchange.getRemoteAddress() + " with method " + exchange.getRequestMethod());
        if (MySQL.instance.getter("SELECT * FROM bans WHERE ban = \"" + exchange.getRemoteAddress().getAddress().getCanonicalHostName().split(":")[0] + "\";")) {
            sendMessage(Main.ownerID,"Banned user has been connected ("+exchange.getRemoteAddress().getAddress().getCanonicalHostName()+"/"+exchange.getRequestMethod()+")");
            String response = "You are banned";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            String uri = exchange.getRequestURI().toString();
            if (uri.equals("/")) {
                uri = "/index.html"; // по умолчанию отдаем index.html
            }

            // Поиск файла в корневой папке
            File file = new File("." + uri);
            if (file.exists()) {
                exchange.sendResponseHeaders(200, file.length());
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            } else {
                String response = "404 (Not Found)";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "405 (Method Not Allowed)";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    public static void sendMessage(int userId, String text) {
        try {
            URL obj = new URL("https://api.telegram.org/bot" + Main.token + "/sendMessage?chat_id=" + userId + "&text=" + text + "");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Значение по умолчанию - GET
            con.setRequestMethod("GET");

            // Добавляем заголовок запроса
            con.setRequestProperty("User-Agent", USER_AGENT);


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            con.disconnect();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
