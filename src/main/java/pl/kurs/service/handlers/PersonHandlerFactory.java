package pl.kurs.service.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.exception.UnsupportedTypeException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersonHandlerFactory {

    private final Map<String, PersonHandler> handlersMap;

    public PersonHandlerFactory(List<PersonHandler> handlers) {
        this.handlersMap = handlers
                .stream()
                .collect(Collectors.toMap(PersonHandler::supportsType, Function.identity()));
    }

    public boolean isSupportedType(String type) {
        return handlersMap.containsKey(type);
    }

    public PersonHandler getHandler(String type) {
        return Optional.ofNullable(handlersMap.get(type))
                .orElseThrow(() -> new UnsupportedTypeException("Unsupported person type: " + type));
    }
}
