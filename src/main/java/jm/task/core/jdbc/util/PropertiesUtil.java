package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//реализуйте настройку соеденения с БД
public final class PropertiesUtil {

    // файл Properties в который подгружаются наши пропертесы из фала который лежит в папке resoursers
    private static final Properties PROPERTIES = new Properties();

    // статический блок инициализации
    static {

        loadPropertiesJDBC();
    }

    // приватный конструктор
    private PropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    // метод считывает application.properties файл
    private static void loadPropertiesJDBC() {
        try (InputStream Inputstream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(Inputstream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // метод считывает конфигурацию из файла hibernate.properties который находится в папке resoursers

}

