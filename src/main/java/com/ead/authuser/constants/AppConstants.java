package com.ead.authuser.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Banco {
        public static final String TABELA_AUTH_USER = "TB_USERS";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Controller {
        public static final String USERS = "/users";
        public static final String AUTHENTICATION = "/auth";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data {
        public static final String PADRAO_DATA = "dd-MM-yyyy HH:mm:ss";
        public static final String UTC = "UTC";

    }
}
