package starter.gradle;

import org.jooby.Results;
import org.jooby.Route;

public class HandlerForHomePage implements Route.ZeroArgHandler {

    @Override
    public Object handle() {
        return Results.html("index");
    }

}
