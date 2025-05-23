package com.fluffy.blogify.middleware;

import com.fluffy.blogify.utils.ModelKey;
import com.fluffy.blogify.utils.ServerData;
import com.fluffy.blogify.utils.SessionKey;
import com.fluffy.blogify.utils.VelocityHelper;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public final class ModelFilter {
    private ModelFilter() {}

    public static void initializeModel(Context context) {
        Map<String, Object> model = new HashMap<>();
        ServerData serverData;
        context.sessionAttribute(SessionKey.MODEL, model);

        if (context.sessionAttribute(SessionKey.SERVER_DATA) != null) {
            serverData = context.sessionAttribute(SessionKey.SERVER_DATA);
        } else {
            serverData = new ServerData();
            context.sessionAttribute(SessionKey.SERVER_DATA, serverData);
        }

        model.put(ModelKey.CSRF, context.sessionAttribute(SessionKey.CSRF));
        model.put(ModelKey.USER, context.sessionAttribute(SessionKey.USER));
        model.put(ModelKey.SERVER_DATA, serverData);
        model.put("VelocityHelper", VelocityHelper.class);
    }
}
