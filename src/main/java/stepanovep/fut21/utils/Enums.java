package stepanovep.fut21.utils;

import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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

    // todo Optional by code
}
