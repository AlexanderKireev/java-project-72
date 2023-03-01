package hexlet.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum Env {

    DEVELOPMENT("development"),
    PRODUCTION("production");

    private final String mode;

}
