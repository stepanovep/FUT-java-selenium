package stepanovep.fut21.utils;

/**
 * Утилитный класс для енамов
 */
public class Enums {

    /**
     * Итерфейс енама со строковым значением
     */
    public interface StringRepr {
        String getCode();
    }

    /**
     * Интерфейс енама с числовым значением
     */
    public interface IntRepr {
        int getCode();
    }

}
