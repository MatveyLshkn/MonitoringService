package by.matvey.lshkn;

import by.matvey.lshkn.in.application.Application;
import by.matvey.lshkn.util.LoggerUtil;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Application application = new Application();
        application.start();
        LoggerUtil.log(LocalDateTime.now() + " Application started");
    }
}

