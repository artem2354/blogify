package com.fluffy.blogify;

import com.fluffy.blogify.controllers.*;
import com.fluffy.blogify.exceptions.HttpException;
import com.fluffy.blogify.middleware.CSRFFilter;
import com.fluffy.blogify.middleware.ModelFilter;
import com.fluffy.blogify.utils.ApplicationAccessManager;
import com.fluffy.blogify.utils.Configuration;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // ✅ Встановлюємо дефолтний шлях до config.properties, якщо не передано аргумент
        String configPath = args.length > 0 ? args[0] : "application.properties";

        try {
            Configuration.load(new File(configPath));
        } catch (Exception e) {
            System.err.println("❌ Не вдалося завантажити конфігурацію з " + configPath);
            e.printStackTrace();
            return;
        }

        // ✅ Створюємо застосунок
        Javalin application = Javalin.create(configuration -> {
            configuration.addStaticFiles("/public", Location.CLASSPATH);
            configuration.accessManager(new ApplicationAccessManager());
        });

        // ✅ Реєстрація обробника помилок
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController(application);
        application
                .before(ModelFilter::initializeModel)
                .before(CSRFFilter::verifyToken)
                .before(CSRFFilter::generateToken)
                .exception(HttpException.class, exceptionHandlerController::handleHttpException)
                .error(404, exceptionHandlerController::handlePageNotFoundError)
                .error(500, exceptionHandlerController::handleInternalServerError);

        // ✅ Ініціалізація контролерів
        new HomeController(application);
        new UserController(application);
        new PostController(application);
        new CommentController(application);

        // ✅ Старт додатку
        application.start(
                Configuration.get("application.host"),
                Configuration.getAsClass("application.port", Integer.class)
        );
    }
}
